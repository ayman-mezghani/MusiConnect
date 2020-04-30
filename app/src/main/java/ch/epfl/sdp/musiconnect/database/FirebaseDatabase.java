package ch.epfl.sdp.musiconnect.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.events.Event;

public class FirebaseDatabase extends Database {
    private static final String TAG = "DataBase";
    private FirebaseFirestore db;

    public FirebaseDatabase() {
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
        db.collection(collection).document(docName).set(entry, SetOptions.merge());
    }

    @Override
    public void addDoc(SimplifiedEvent simplifiedEvent, DbUserType userType) {
        db.collection("events").add(simplifiedEvent)
                .addOnSuccessListener(documentReference -> {
                    if(userType == DbUserType.Band) {
                        DbGenerator.getDbInstance().read(DbUserType.Band, simplifiedEvent.getCreatorMail(), new DbCallback() {
                            @Override
                            public void readCallback(User u) {
                                Band b = (Band) u;
                                b.addEvent(documentReference.getId());
                                DbGenerator.getDbInstance().add(userType, b);
                            }
                        });
                    } else if(userType == DbUserType.Musician) {
                        DbGenerator.getDbInstance().read(DbUserType.Musician, simplifiedEvent.getCreatorMail(), new DbCallback() {
                            @Override
                            public void readCallback(User u) {
                                Musician m = (Musician) u;
                                m.addEvent(documentReference.getId());
                                DbGenerator.getDbInstance().add(userType, m);
                            }
                        });
                    }

                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }



    @Override
    public void deleteDoc(String collection, String docName) {
        db.collection(collection).document(docName).delete()
                .addOnSuccessListener(bVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    @Override
    public void updateDoc(String collection, String docName, Map<String, Object> newValueMap) {
        db.collection(collection).document(docName).update(newValueMap)
                .addOnSuccessListener(cVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    @Override
    public void deleteFieldsInDoc(String collection, String docName, List<String> fields) {
        Map<String, Object> updates = new HashMap<>();
        for (String str : fields) {
            updates.put(str, FieldValue.delete());
        }
        this.updateDoc(collection, docName, updates);
    }

    @Override
    public void readDoc(String collection, String docName, DbCallback dbCallback) {
        db.collection(collection).document(docName).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data != null && data.size() > 0) {
                        if (collection.equals((DbUserType.Band.toString()))) {
                            fetchBandMembers(data, dbCallback);
                        } else if(collection.equals(DbUserType.Events.toString())) {
                            DbAdapter da = new DbAdapter(this);
                            da.read(DbUserType.Musician, (String) data.get("creatorMail"), new DbCallback() {
                                @Override
                                public void readCallback(User user) {
                                    Event e = new Event((Musician) user, docName);
                                    e.setAddress((String) data.get("adress"));
                                    e.setDateTime(new MyDate(((Timestamp) data.get("dateTime")).toDate()));
                                    e.setDescription((String) data.get("description"));
                                    e.setTitle((String) data.get("title"));

                                    for (String me : (List<String>) data.get("participants")) {
                                        da.read(DbUserType.Musician, me, new DbCallback() {
                                            @Override
                                            public void readCallback(User user) {
                                                try {
                                                    e.register((Musician) user);
                                                } catch (IllegalArgumentException e) {}
                                            }
                                        });
                                    }
                                    dbCallback.readCallback(e);
                                }
                            });
                        }
                        else {
                            SimplifiedMusician m = new SimplifiedMusician(data);
                            dbCallback.readCallback(m.toMusician());
                        }
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error reading document", e));
    }

    private void fetchBandMembers(Map<String, Object> data, DbCallback dbCallback) {

        DbAdapter da = DbGenerator.getDbInstance();

        da.read(DbUserType.Musician, (String) data.get("leader"), new DbCallback() {
            @Override
            public void readCallback(User user) {
                Band b = new Band((String) data.get("bandName"), (Musician) user);

                if (data.get("videoUrl") != null)
                    b.setVideoURL(data.get("videoUrl").toString());

                b.setMusicianEmailAdresses((ArrayList<String>) data.get("members"));
                b.setEvents((ArrayList<String>) data.get("events"));
                DbAdapter da = DbGenerator.getDbInstance();

                for (String me : b.getMusicianEmailsAdress()) {
                    da.read(DbUserType.Musician, me, new DbCallback() {
                        @Override
                        public void readCallback(User user) {
                            try {
                                b.addMember((Musician) user);
                            } catch (IllegalArgumentException e) {
                            }
                        }
                    });
                }
                dbCallback.readCallback(b);
            }
        });
    }

    @Override
    public void docExists(String collection, String docName, DbCallback dbCallback) {
        db.collection(collection).document(docName).get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        dbCallback.existsCallback(document.exists());
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });
    }

    @Override
    public void finderQuery(String collection, Map<String, Object> arguments, DbCallback dbCallback) {
        CollectionReference ref = db.collection(collection);
        unpack(ref, arguments).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> queryResult = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            Log.d("checkcheck", document.getId() + " => " + data);
                            if (collection.equals(DbUserType.Musician.toString())) {
                                SimplifiedMusician m = new SimplifiedMusician(document.getData());
                                queryResult.add(m.toMusician());
                            } else if (collection.equals((DbUserType.Band.toString()))) {
                                // @TODO can't fetch a list of bands !!!!
                            }
                        }
                        dbCallback.queryCallback(queryResult);
                    } else {
                        Log.d("checkcheck", "Error getting documents: ", task.getException());
                    }
                });
    }

    private Query unpack(CollectionReference ref, Map<String, Object> args) {
        if (args.isEmpty())
            return ref;
        else {
            ArrayDeque<Map.Entry> argEntries = new ArrayDeque<>(args.entrySet());
            Query res = singleQuery(ref, argEntries.pollFirst());
            while (!argEntries.isEmpty()) {
                res = singleQuery(res, argEntries.pollFirst());
            }
            return res;
        }
    }

    private Query singleQuery(Query prev, Map.Entry<String, Object> clause) {
        if (clause.getValue() != null) {
            Log.d("checkcheck", clause.getKey() + "=>" + clause.getValue() + " " + clause.getValue().getClass().getName());
            if (clause.getKey().equals("")) {
                return prev.whereArrayContains(clause.getKey(), clause.getValue());
            } else {
                return prev.whereEqualTo(clause.getKey(), clause.getValue());
            }
        } else return prev;
    }
}
package ch.epfl.sdp.musiconnect.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.User;

class FirebaseDatabase extends Database {
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
                        if (data.get("leader") != null) {

                            DbAdapter da = new DbAdapter(this);
                            da.read(DbUserType.Musician, (String) data.get("leader"), new DbCallback() {
                                @Override
                                public void readCallback(User user) {
                                    Band b = new Band((String) data.get("bandName"), (Musician) user);

                                    if (data.get("videoUrl") != null)
                                        b.setVideoURL(data.get("videoUrl").toString());

                                    b.setMusicianEmailAdresses((ArrayList<String>) data.get("members"));
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
                        } else {
                            SimplifiedMusician m = new SimplifiedMusician(data);
                            dbCallback.readCallback(m.toMusician());
                        }
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error reading document", e));
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
    public void query() {
        db.collection("newtest")
                .whereEqualTo("email", "musiconnectsdp@gmail.com")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }
}
package ch.epfl.sdp.musiconnect.database;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.User;

import static android.location.Location.distanceBetween;
import static ch.epfl.sdp.musiconnect.database.SimplifiedDbEntry.Fields;

public class FirebaseDatabase extends Database {
    private static final String TAG = "DataBase";
    // Source : https://en.wikipedia.org/wiki/Latitude#Length_of_a_degree_of_latitude
    private static final double LAT_TO_KM = 110.574;
    private FirebaseFirestore db;

    FirebaseDatabase() {
        this.db = FirebaseFirestore.getInstance();
    }

    FirebaseDatabase(FirebaseFirestore instance) {
        this.db = instance;
    }


    @Override
    void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
        db.collection(collection).document(docName).set(entry, SetOptions.merge())
                .addOnSuccessListener(bVoid -> Log.d(TAG, "DocumentSnapshot successfully added!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    @Override
    void addDoc(SimplifiedEvent simplifiedEvent, DbDataType hostUserType) {
        db.collection("events").add(simplifiedEvent)
                .addOnSuccessListener(documentReference -> {
                    if (hostUserType == DbDataType.Band) {
                        DbSingleton.getDbInstance().read(DbDataType.Band, simplifiedEvent.getHost(), new DbCallback() {
                            @Override
                            public void readCallback(User u) {
                                Band b = (Band) u;
                                b.addEvent(documentReference.getId());
                                DbSingleton.getDbInstance().add(hostUserType, b);
                            }
                        });
                    } else if (hostUserType == DbDataType.Musician) {
                        DbSingleton.getDbInstance().read(DbDataType.Musician, simplifiedEvent.getHost(), new DbCallback() {
                            @Override
                            public void readCallback(User u) {
                                Musician m = (Musician) u;
                                m.addEvent(documentReference.getId());
                                DbSingleton.getDbInstance().add(hostUserType, m);
                            }
                        });
                    }

                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }


    @Override
    void deleteDoc(String collection, String docName) {
        db.collection(collection).document(docName).delete()
                .addOnSuccessListener(bVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    @Override
    void updateDoc(String collection, String docName, Map<String, Object> newValueMap) {
        db.collection(collection).document(docName).update(newValueMap)
                .addOnSuccessListener(cVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    @Override
    void deleteFieldsInDoc(String collection, String docName, List<String> fields) {
        Map<String, Object> updates = new HashMap<>();
        for (String str : fields) {
            updates.put(str, FieldValue.delete());
        }
        this.updateDoc(collection, docName, updates);
    }

    @Override
    void readDoc(String collection, String docName, DbCallback dbCallback) {
        db.collection(collection).document(docName).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> data = documentSnapshot.getData();

                    if (data != null && data.size() > 0) {
                        if (collection.equals((DbDataType.Band.toString()))) {
                            SimplifiedBand sb = new SimplifiedBand(data);
                            dbCallback.readCallback(sb.toBand());
                        } else if (collection.equals(DbDataType.Events.toString())) {
                            SimplifiedEvent se = new SimplifiedEvent(data);
                            dbCallback.readCallback(se.toEvent(docName));
                        } else {
                            SimplifiedMusician m = new SimplifiedMusician(data);
                            dbCallback.readCallback(m.toMusician());
                        }
                    }

                    // Data is null!
                    else {
                        dbCallback.readFailCallback();
                        Log.w(TAG, "Error: the element does not exist in the database");
                    }
                })
                .addOnFailureListener(e -> {
                    dbCallback.readFailCallback();
                    Log.w(TAG, "Error reading document", e);
                });
    }

    @Override
    void docExists(String collection, String docName, DbCallback dbCallback) {
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
    void finderQuery(String collection, Map<String, Object> arguments, DbCallback dbCallback) {
        CollectionReference ref = db.collection(collection);
        Task<QuerySnapshot> t = DatabaseQueryHelpers.unpack(ref, arguments).get();
        t.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> queryResult = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Map<String, Object> data = document.getData();
                    if (collection.equals(DbDataType.Musician.toString())) {
                        SimplifiedMusician m = new SimplifiedMusician(data);
                        queryResult.add(m.toMusician());
                    } else if (collection.equals((DbDataType.Band.toString()))) {
                        SimplifiedBand sb = new SimplifiedBand(data);
                        queryResult.add(sb.toBand());
                    }
                }
                dbCallback.queryCallback(queryResult);
            } else {
                Log.d(TAG, "Failed with: ", task.getException());
            }
        });
    }

    @Override
    void locQuery(String collection, GeoPoint currentLocation, double distanceInKm, DbCallback dbCallback) {
        double maxLat = currentLocation.getLatitude() + distanceInKm / LAT_TO_KM;
        double minLat = currentLocation.getLatitude() - distanceInKm / LAT_TO_KM;

        db.collection(collection)
                .whereGreaterThanOrEqualTo(Fields.location.toString(), minLat)
                .whereLessThanOrEqualTo(Fields.location.toString(), maxLat)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List queryResult = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Map<String, Object> data = document.getData();
                            if (collection.equals(DbDataType.Musician.toString())) {
                                SimplifiedMusician m = new SimplifiedMusician(data);
                                if (isWithin(currentLocation, m.getLocation(), distanceInKm))
                                    queryResult.add(m.toMusician());
                            } else if (collection.equals((DbDataType.Events.toString()))) {
                                SimplifiedEvent e = new SimplifiedEvent(data);
                                if (isWithin(currentLocation, e.getLocation(), distanceInKm))
                                    queryResult.add(e.toEvent(document.getId()));
                            }
                        }
                        dbCallback.locationQueryCallback(queryResult);
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                });
    }

    private boolean isWithin(GeoPoint currentLocation, GeoPoint resultLocation, double distance) {
        Location start =  new Location("");
        start.setLongitude(currentLocation.getLongitude());
        start.setLatitude(currentLocation.getLatitude());
        Location dest =  new Location("");
        dest.setLongitude(resultLocation.getLongitude());
        dest.setLatitude(resultLocation.getLatitude());
        return start.distanceTo(dest) <= distance;
    }
}
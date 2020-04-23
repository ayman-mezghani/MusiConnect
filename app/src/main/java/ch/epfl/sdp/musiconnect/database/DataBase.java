package ch.epfl.sdp.musiconnect.database;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.User;

public class DataBase {
    private static final String TAG = "DataBase";
    private FirebaseFirestore db;


    public DataBase() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void addMusician(String collection, String docName, SimplifiedMusician m) {
        db.collection(collection).document(docName).set(m, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    public void addBand(String collection, String docName, SimplifiedBand b) {
        db.collection(collection).document(docName).set(b, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    public void deleteDoc(String collection, String docName) {
        db.collection(collection).document(docName).delete()
                .addOnSuccessListener(bVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    public void updateDoc(String collection, String docName, Map<String, Object> newValueMap) {
        db.collection(collection).document(docName).update(newValueMap)
                .addOnSuccessListener(cVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    public void deleteFieldsInDoc(String collection, String docName, List<String> fields) {
        Map<String, Object> updates = new HashMap<>();
        for (String str : fields) {
            updates.put(str, FieldValue.delete());
        }
        this.updateDoc(collection, docName, updates);
    }

    public void readDoc(String collection, String docName, DbCallback dbCallback) {
        db.collection(collection).document(docName).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data != null && data.size() > 0) {
                        if (data.get("leader") != null) {

                            DbAdapter da = new DbAdapter(this);
                            da.read("newtest", (String) data.get("leader"), new DbCallback() {
                                @Override
                                public void readCallback(User user) {
                                    Band b = new Band((String) data.get("bandName"), (Musician) user);

                                    if (data.get("videoUrl") != null)
                                        b.setVideoURL(data.get("videoUrl").toString());

                                    b.setMusicianEmailAdresses((ArrayList<String>) data.get("members"));
                                    DbAdapter da = new DbAdapter(new DataBase());

                                    for (String me : b.getMusicianEmailsAdress()) {
                                        da.read("newtest", me, new DbCallback() {
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
/*
                    if(documentSnapshot.getDocument().getKey().getPath() .toString().split("/")[0].equals("Band")){

                    }
*/
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error reading document", e));
    }

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
}
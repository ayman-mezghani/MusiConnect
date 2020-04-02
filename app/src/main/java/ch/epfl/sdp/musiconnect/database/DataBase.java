package ch.epfl.sdp.musiconnect.database;

import android.util.Log;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    private static final String TAG = "DataBase";
    private FirebaseFirestore db;

    public DataBase() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void addDoc(String docName, SimplifiedMusician m) {
        db.collection("newtest").document(docName).set(m)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    public void deleteDoc(String docName) {
        db.collection("users").document(docName).delete()
                .addOnSuccessListener(bVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    public void updateDoc(String docName, Map<String, Object> newValueMap) {
        db.collection("users").document(docName).update(newValueMap)
                .addOnSuccessListener(cVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    public void deleteFieldsInDoc(String docName, List<String> fields) {
        Map<String, Object> updates = new HashMap<>();
        for (String str : fields) {
            updates.put(str, FieldValue.delete());
        }
        this.updateDoc(docName, updates);
    }

    public void readDoc(String docName, DbCallback dbCallback) {
        db.collection("users").document(docName).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map data = documentSnapshot.getData();
                    dbCallback.onCallback(data);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error reading document", e));
    }
}
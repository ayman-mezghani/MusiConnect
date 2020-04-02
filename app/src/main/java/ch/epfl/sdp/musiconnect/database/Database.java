package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public interface Database {
    static FirebaseFirestore getProdDB() {
        return FirebaseFirestore.getInstance();
    }

    void addDoc(Map<String, Object> m, String docName);
//    void addDoc(User u, String docName);

    void deleteDoc(String docName);

    void updateDoc(String docName, Map<String, Object> newValueMap);

    void deleteFieldsInDoc(String docName, List<String> fields);

    void readDoc(String docName, DbCallback dbCallback);


}
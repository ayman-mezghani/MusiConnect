package ch.epfl.sdp.musiconnect.database;

import java.util.List;
import java.util.Map;

public interface Database {
    void addDoc(String docName, SimplifiedMusician m);

    void deleteDoc(String docName);

    void updateDoc(String docName, Map<String, Object> newValueMap);

    void deleteFieldsInDoc(String docName, List<String> fields);

    void readDoc(String docName, DbCallback dbCallback);

    void docExists(String docName, DbCallback dbCallback);
}

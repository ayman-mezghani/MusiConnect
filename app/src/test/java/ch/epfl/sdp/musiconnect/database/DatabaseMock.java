package ch.epfl.sdp.musiconnect.database;

import java.util.List;
import java.util.Map;

public class DatabaseMock implements Database {
    @Override
    public void addDoc(Map<String, Object> m, String docName) {

    }

    @Override
    public void deleteDoc(String docName) {

    }

    @Override
    public void updateDoc(String docName, Map<String, Object> newValueMap) {

    }

    @Override
    public void deleteFieldsInDoc(String docName, List<String> fields) {

    }

    @Override
    public void readDoc(String docName, DbCallback dbCallback) {

    }
}

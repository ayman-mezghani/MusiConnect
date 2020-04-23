package ch.epfl.sdp.musiconnect.database;

import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;

import static org.junit.Assert.assertEquals;

public class MockDatabaseForUT extends Database {

    private String expectedDocName;
    private SimplifiedMusician expectedSimplifiedMusician;
    private Map<String, Object> expectedNewValueMap;

    MockDatabaseForUT(String expectedDocName, SimplifiedMusician expectedSimplifiedMusician, Map<String, Object> expectedNewValueMap) {
        this.expectedDocName = expectedDocName;
        this.expectedSimplifiedMusician = expectedSimplifiedMusician;
        this.expectedNewValueMap = expectedNewValueMap;
    }

    public MockDatabaseForUT() {
    }

    @Override
    void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
        assertEquals(expectedDocName, docName);
        assertEquals(expectedSimplifiedMusician, entry);
    }

    @Override
    void deleteDoc(String collection, String docName) {
        assertEquals(expectedDocName, docName);

    }

    @Override
    void updateDoc(String collection, String docName, Map<String, Object> newValueMap) {
        assertEquals(expectedDocName, docName);
        assertEquals(expectedNewValueMap, newValueMap);
    }

    @Override
    void deleteFieldsInDoc(String collection, String docName, List<String> fields) {
        assertEquals(expectedDocName, docName);
    }

    @Override
    void readDoc(String collection, String docName, DbCallback dbCallback) {
        assertEquals(expectedDocName, docName);
        dbCallback.readCallback(expectedSimplifiedMusician.toMusician());
    }

    @Override
    void docExists(String collection, String docName, DbCallback dbCallback) {
        assertEquals(expectedDocName, docName);
        dbCallback.existsCallback(false);
    }
}

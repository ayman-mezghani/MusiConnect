package ch.epfl.sdp.musiconnect.database;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MockDatabaseForUT extends Database {

    private String expectedDocName;
    private SimplifiedDbEntry expectedEntry;
    private Map<String, Object> expectedMap;

    MockDatabaseForUT(String expectedDocName, SimplifiedDbEntry expectedEntry, Map<String, Object> expectedMap) {
        this.expectedDocName = expectedDocName;
        this.expectedEntry = expectedEntry;
        this.expectedMap = expectedMap;
    }

    public MockDatabaseForUT() {
    }

    @Override
    void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
        assertEquals(expectedDocName, docName);
        assertTrue(expectedEntry.equals(expectedEntry));
    }

    @Override
    public void addDoc(SimplifiedEvent simplifiedEvent, DbUserType userType) {

    }

    @Override
    void deleteDoc(String collection, String docName) {
        assertEquals(expectedDocName, docName);

    }

    @Override
    void updateDoc(String collection, String docName, Map<String, Object> newValueMap) {
        assertEquals(expectedDocName, docName);
        assertEquals(expectedMap, newValueMap);
    }

    @Override
    void deleteFieldsInDoc(String collection, String docName, List<String> fields) {
        assertEquals(expectedDocName, docName);
    }

    @Override
    void readDoc(String collection, String docName, DbCallback dbCallback) {
        assertEquals(expectedDocName, docName);
        if(collection == DbUserType.Musician.toString()) {
            dbCallback.readCallback(((SimplifiedMusician) expectedEntry).toMusician());
        }
    }

    @Override
    void docExists(String collection, String docName, DbCallback dbCallback) {
        assertEquals(expectedDocName, docName);
        dbCallback.existsCallback(false);
    }
}

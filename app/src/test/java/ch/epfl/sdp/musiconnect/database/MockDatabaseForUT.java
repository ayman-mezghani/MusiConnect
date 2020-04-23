package ch.epfl.sdp.musiconnect.database;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MockDatabaseForUT extends Database {

    private String expectedDocName;
    private SimplifiedMusician expectedSimplifiedMusician;
    private SimplifiedEvent expectedSimplifiedEvent;
    private DbUserType expectedDbType;
    private Map<String, Object> expectedNewValueMap;

    MockDatabaseForUT(String expectedDocName, SimplifiedMusician expectedSimplifiedMusician, Map<String, Object> expectedNewValueMap, SimplifiedEvent expectedSimplifiedEvent, DbUserType expectedDbType) {
        this.expectedDocName = expectedDocName;
        this.expectedSimplifiedMusician = expectedSimplifiedMusician;
        this.expectedNewValueMap = expectedNewValueMap;
        this.expectedSimplifiedEvent = expectedSimplifiedEvent;
        this.expectedDbType = expectedDbType;
    }

    public MockDatabaseForUT() {
    }

    @Override
    void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
        assertEquals(expectedDocName, docName);
        assertTrue(expectedEntry.equals(expectedEntry));
    }

    @Override
    void addDoc(SimplifiedEvent simplifiedEvent, DbUserType userType) {
        assertEquals(expectedDbType, userType);
        assertEquals(expectedSimplifiedEvent, simplifiedEvent);
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

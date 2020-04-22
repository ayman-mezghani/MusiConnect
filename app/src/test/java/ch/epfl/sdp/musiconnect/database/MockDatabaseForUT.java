package ch.epfl.sdp.musiconnect.database;

import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;

import static org.junit.Assert.assertEquals;

public class MockDatabaseForUT implements Database {

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
    public void addDoc(String docName, SimplifiedMusician m) {
        assertEquals(expectedDocName, docName);
        assertEquals(expectedSimplifiedMusician, m);
    }

    @Override
    public void deleteDoc(String docName) {
        assertEquals(expectedDocName, docName);
    }

    @Override
    public void updateDoc(String docName, Map<String, Object> newValueMap) {
        assertEquals(expectedDocName, docName);
        assertEquals(expectedNewValueMap, newValueMap);
    }

    @Override
    public void deleteFieldsInDoc(String docName, List<String> fields) {
        assertEquals(expectedDocName, docName);
    }

    @Override
    public void readDoc(String docName, DbCallback dbCallback) {
        assertEquals(expectedDocName, docName);
        dbCallback.readCallback(expectedSimplifiedMusician.toMusician());
    }

    @Override
    public void docExists(String docName, DbCallback dbCallback) {
        assertEquals(expectedDocName, docName);
        dbCallback.existsCallback(false);
    }
}

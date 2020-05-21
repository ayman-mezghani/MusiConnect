package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.User;

import static org.junit.Assert.assertEquals;

public class MockDatabaseForUT extends Database {

    private String expectedCollection;
    private String expectedDocName;
    private SimplifiedDbEntry expectedEntry;
    private Map<String, Object> expectedMap;

    MockDatabaseForUT(String collectionName, String expectedDocName, SimplifiedDbEntry expectedEntry, Map<String, Object> expectedMap) {
        this.expectedCollection = collectionName;
        this.expectedDocName = expectedDocName;
        this.expectedEntry = expectedEntry;
        this.expectedMap = expectedMap;
    }

    public MockDatabaseForUT() {
    }

    @Override
    void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
        assertEquals(expectedDocName, docName);
        assertEquals(expectedEntry, expectedEntry);
    }

    @Override
    public void addDoc(SimplifiedEvent simplifiedEvent, DbDataType userType) {

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
        if (collection == DbDataType.Musician.toString()) {
            dbCallback.readCallback(((SimplifiedMusician) expectedEntry).toMusician());
        }
    }

    @Override
    void docExists(String collection, String docName, DbCallback dbCallback) {
        assertEquals(expectedDocName, docName);
        dbCallback.existsCallback(false);
    }

    @Override
    public void finderQuery(String collection, Map<String, Object> arguments, DbCallback dbCallback) {
        assertEquals(expectedCollection, collection);
        assertEquals(expectedMap, arguments);
        List<User> l = new ArrayList<>();
        l.add(((SimplifiedMusician) expectedEntry).toMusician());
        dbCallback.queryCallback(l);
    }

    @Override
    void locQuery(String collection, GeoPoint currentLocation, double distance, DbCallback dbCallback) {

    }
}

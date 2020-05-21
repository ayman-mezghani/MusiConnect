package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Map;

abstract class Database {


    abstract void addDoc(String collection, String docName, SimplifiedDbEntry entry);

    abstract void addDoc(SimplifiedEvent simplifiedEvent, DbDataType userType);

    abstract void deleteDoc(String collection, String docName);

    abstract void updateDoc(String collection, String docName, Map<String, Object> newValueMap);

    abstract void deleteFieldsInDoc(String collection, String docName, List<String> fields);

    abstract void readDoc(String collection, String docName, DbCallback dbCallback);

    abstract void docExists(String collection, String docName, DbCallback dbCallback);

    abstract void finderQuery(String collection, Map<String, Object> arguments, DbCallback dbCallback);

    abstract void locQuery(String collection, GeoPoint currentLocation, double distance, DbCallback dbCallback);
}

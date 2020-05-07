package ch.epfl.sdp.musiconnect.database;

import java.util.List;
import java.util.Map;

abstract class Database {


    abstract void addDoc(String collection, String docName, SimplifiedDbEntry entry);

    abstract void addDoc(SimplifiedEvent simplifiedEvent, DbUserType userType);

    abstract void deleteDoc(String collection, String docName);

    abstract void updateDoc(String collection, String docName, Map<String, Object> newValueMap);

    abstract void deleteFieldsInDoc(String collection, String docName, List<String> fields);

    abstract void readDoc(String collection, String docName, DbCallback dbCallback);

    abstract void docExists(String collection, String docName, DbCallback dbCallback);

    public abstract void finderQuery(String collection, Map<String, Object> arguments, DbCallback dbCallback);
}

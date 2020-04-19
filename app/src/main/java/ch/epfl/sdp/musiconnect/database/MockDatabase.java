package ch.epfl.sdp.musiconnect.database;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;


class MockDatabase implements Database {


    private final String firstName = "bob";
    private final String lastName = "minion";
    private final String username = "bobminion";
    private final String email = "bobminion@gmail.com";
    private final MyDate birthday = new MyDate(2000, 1, 1);

    private SimplifiedMusician simplifiedMusician = new SimplifiedMusician(new Musician(firstName, lastName, username, email, birthday));

    MockDatabase() {
    }

    @Override
    public void addDoc(String docName, SimplifiedMusician m) {
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
        dbCallback.readCallback(simplifiedMusician.toMusician());
    }

    @Override
    public void docExists(String docName, DbCallback dbCallback) {
        dbCallback.existsCallback(false);
    }
}

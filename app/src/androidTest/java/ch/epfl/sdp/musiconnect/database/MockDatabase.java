package ch.epfl.sdp.musiconnect.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;

public class MockDatabase extends Database {
    
    private final String firstName = "bob";
    private final String lastName = "minion";
    private final String username = "bobminion";
    private final String email = "bobminion@gmail.com";
    private final MyDate birthday = new MyDate(2000, 1, 1);

    private SimplifiedMusician simplifiedMusician = new SimplifiedMusician(new Musician(firstName, lastName, username, email, birthday));

    private Map<String,SimplifiedMusician> content;

    public MockDatabase() {
        this.content = new HashMap<>();
    }

    @Override
    void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
    }

    @Override
    void deleteDoc(String collection, String docName) {
    }

    @Override
    void updateDoc(String collection, String docName, Map<String, Object> newValueMap) {
    }

    @Override
    void deleteFieldsInDoc(String collection, String docName, List<String> fields) {
    }

    @Override
    void readDoc(String collection, String docName, DbCallback dbCallback) {
        dbCallback.readCallback(simplifiedMusician.toMusician());
    }

    @Override
    void docExists(String collection, String docName, DbCallback dbCallback) {
        dbCallback.existsCallback(false);
    }
}

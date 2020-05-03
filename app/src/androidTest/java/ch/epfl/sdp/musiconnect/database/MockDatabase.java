package ch.epfl.sdp.musiconnect.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.events.Event;

public class MockDatabase extends Database {

    private final String firstName = "bob";
    private final String lastName = "minion";
    private final String username = "bobminion";
    private final String email = "bobminion@gmail.com";
    private final MyDate birthday = new MyDate(2000, 1, 1);

    private SimplifiedMusician defaultSm = new SimplifiedMusician(new Musician(firstName, lastName, username, email, birthday));

    private SimplifiedMusician dummy1 = new SimplifiedMusician(new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25)));
    private SimplifiedMusician dummy2 = new SimplifiedMusician(new Musician("Alice", "Bardon", "Alyx", "aymanmezghani97@gmail.com", new MyDate(1992, 9, 20)));
    private SimplifiedMusician dummy3 = new SimplifiedMusician(new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1)));

    private List<SimplifiedMusician> listOfMusicians;
    private Map<String, SimplifiedMusician> content;

    Event event;

    public MockDatabase() {
        this.content = new HashMap<>();
        listOfMusicians = new ArrayList<>();

        listOfMusicians.add(defaultSm);
        listOfMusicians.add(dummy1);
        listOfMusicians.add(dummy2);
        listOfMusicians.add(dummy3);

        Musician m1 = this.getDummyMusician(0);
        Musician m2 = this.getDummyMusician(3);

        event = new Event(m1, "1");
        event.setAddress("Westminster, London, England");
        event.setLocation(51.5007, 0.1245);
        event.setDateTime(new MyDate(2020, 9, 21, 14, 30));
        event.setTitle("Event at Big Ben!");
        event.setDescription("Playing at Big Ben, come watch us play!");
        event.register(m2);

        m1.addEvent(event.getEid());
    }

    public Musician getDummyMusician(int index) {
        return (listOfMusicians.get(index)).toMusician();
    }

    @Override
    void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
    }

    @Override
    void addDoc(SimplifiedEvent simplifiedEvent, DbUserType userType) {

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
        if(collection.equals(DbUserType.Events.toString())) {
            dbCallback.readCallback(event);
            return;
        }

        boolean found = false;
        for (SimplifiedMusician sm : listOfMusicians) {
            if (docName.equals(sm.getEmail())) {
                dbCallback.readCallback(sm.toMusician());
                found = true;
            }
        }

        if (!found && collection.equals(DbUserType.Musician.toString())) {
            dbCallback.readCallback(defaultSm.toMusician());
        }
    }

    @Override
    void docExists(String collection, String docName, DbCallback dbCallback) {
        dbCallback.existsCallback(false);
    }

    @Override
    public void finderQuery(String collection, Map<String, Object> arguments, DbCallback dbCallback) {
        List<User> l = new ArrayList<>();
        l.add(defaultSm.toMusician());
        dbCallback.queryCallback(l);
    }
}

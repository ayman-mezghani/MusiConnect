package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.TypeOfUser;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.events.Event;

public class MockDatabase extends Database {

    private final static String firstName = "bob";
    private final static String lastName = "minion";
    private final static String username = "bobminion";
    private final static String email = "bobminion@gmail.com";
    private final static MyDate birthday = new MyDate(2000, 1, 1);

    private SimplifiedMusician defaultSm;

    private List<SimplifiedMusician> listOfMusicians; // index 0
    private List<Event> listOfEvent;
    private Map<String, SimplifiedMusician> content;

    private Band b;

    public MockDatabase(boolean addBandUser) {
        this.content = new HashMap<>();
        listOfMusicians = new ArrayList<>();
        listOfEvent = new ArrayList<>();

        Musician m = new Musician(firstName, lastName, username, email, birthday);
        m.addEvent("1");
        if(addBandUser) {
            m.setTypeOfUser(TypeOfUser.Band);
        } else {
            m.setTypeOfUser(TypeOfUser.Musician);
        }
        defaultSm = new SimplifiedMusician(m);
        listOfMusicians.add(defaultSm);

        Musician musiConnect = new Musician("MusiConnect", "SDP", "musiConnect", "musiconnectsdp@gmail.com", new MyDate(1992, 9, 20));
        musiConnect.setTypeOfUser(TypeOfUser.Band);

        b = new Band("totofire", m.getEmailAddress());
        b.addMember(musiConnect .getEmailAddress());

        createAndAddDummyMusicians();
        listOfMusicians.add(new SimplifiedMusician(musiConnect));

        createAndAddDummyEvents();

        Event privateEventAndParticipant = createEvent(getDummyMusician(2), "5", "Private but visible event at Big Ben!", false);
        privateEventAndParticipant.register(m.getEmailAddress());
        listOfEvent.add(privateEventAndParticipant);
    }

    private void createAndAddDummyMusicians() {
        Musician m1 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
        m1.addEvent("2");

        Musician m2 = new Musician("Alice", "Bardon", "Alyx", "aymanmezghani97@gmail.com", new MyDate(1992, 9, 20));
        m2.addEvent("3");
        m2.addEvent("4");
        m2.addEvent("5");

        listOfMusicians.add(new SimplifiedMusician(m1));
        listOfMusicians.add(new SimplifiedMusician(m2));
        listOfMusicians.add(new SimplifiedMusician(new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1))));
    }

    private void createAndAddDummyEvents() {
        listOfEvent.add(createEvent(getDummyMusician(0), "1", "Event at Big Ben!", true));
        listOfEvent.add(createEvent(getDummyMusician(1), "2", "Event at Big Ben!", true));
        listOfEvent.add(createEvent(getDummyMusician(2), "3", "Public Event at Big Ben!", true));
        listOfEvent.add(createEvent(getDummyMusician(2), "4", "Private event at Big Ben!", false));
    }

    public Musician getDummyMusician(int index) {
        return (listOfMusicians.get(index)).toMusician();
    }

    public Event getDummyEvent(int index) {
        return listOfEvent.get(index);
    }

    private Event createEvent(User user, String eid, String title, boolean visible) {
        Musician m2 = getDummyMusician(3);

        Event event = new Event(user.getEmailAddress(), eid);
        event.setAddress("Westminster, London, England");
        event.setLocation(51.5007, 0.1245);
        event.setDateTime(new MyDate(2020, 9, 21, 14, 30));
        event.setTitle(title);
        event.setDescription("Playing at Big Ben, come watch us play!");
        event.setVisible(visible);
        event.register(m2.getEmailAddress());

        return event;
    }

    @Override
    void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
        if(collection.equals(DbDataType.Band.toString())) {
            this.b = ((SimplifiedBand)entry).toBand();
        }
    }

    @Override
    void addDoc(SimplifiedEvent simplifiedEvent, DbDataType userType) {

    }

    @Override
    void deleteDoc(String collection, String docName) {
    }

    @Override
    void updateDoc(String collection, String docName, Map<String, Object> newValueMap) {
        Object value = newValueMap.get(SimplifiedDbEntry.Fields.events.toString());
        Musician m = getDummyMusician(1);

        if (collection.equals(DbDataType.Musician.toString()) && docName.equals(m.getEmailAddress()) && value != null) {
            listOfMusicians.remove(1);
            m.setEvents((List<String>) value);
            listOfMusicians.add(1, new SimplifiedMusician(m));
        }


    }

    @Override
    void deleteFieldsInDoc(String collection, String docName, List<String> fields) {
    }

    @Override
    void readDoc(String collection, String docName, DbCallback dbCallback) {
        if(collection.equals(DbDataType.Events.toString())) {
            for (Event e : listOfEvent) {
                if (docName.equals(e.getEid())) {
                    dbCallback.readCallback(e);
                    return;
                }
            }

        }

        if (collection.equals(DbDataType.Musician.toString())) {
            for (SimplifiedMusician sm : listOfMusicians) {
                if (docName.equals(sm.getEmail())) {
                    dbCallback.readCallback(sm.toMusician());
                    return;
                }
            }
        }

        if(collection.equals(DbDataType.Band.toString())) {
            if(docName.equals("espresso@gmail.com"))
                dbCallback.readCallback(new Band("testBand", "espresso@gmail.com"));
            else
                dbCallback.readCallback(b);
            return;
        }

        dbCallback.readCallback(defaultSm.toMusician());
    }

    @Override
    void docExists(String collection, String docName, DbCallback dbCallback) {
        dbCallback.existsCallback(false);
    }

    @Override
    public void finderQuery(String collection, Map<String, Object> arguments, DbCallback dbCallback) {
        List<User> l = new ArrayList<>();
        if(collection.equals(DbDataType.Musician.toString())) {
            l.add(defaultSm.toMusician());
            dbCallback.queryCallback(l);
        } else if(collection.equals(DbDataType.Band.toString())) {

            Band b = new Band("totofire" ,defaultSm.getEmail());
            l.add(b);
            Band b2 = new Band("testBand", "espresso@gmail.com");
            l.add(b2);
            dbCallback.queryCallback(l);
        }
    }

    @Override
    void locQuery(String collection, GeoPoint currentLocation, double distance, DbCallback dbCallback) {

    }

    public Band getBand() {
        return this.b;
    }
}

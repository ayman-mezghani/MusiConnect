package ch.epfl.sdp.musiconnect.database;

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

    private List<SimplifiedMusician> listOfMusicians;
    private List<Event> listOfEvent;
    private Map<String, SimplifiedMusician> content;



    private Band b;

    public MockDatabase() {
        this.content = new HashMap<>();
        listOfMusicians = new ArrayList<>();
        listOfEvent = new ArrayList<>();

        Musician m = new Musician(firstName, lastName, username, email, birthday);
        m.addEvent("1");
        m.setTypeOfUser(TypeOfUser.Band);
        defaultSm = new SimplifiedMusician(m);
        listOfMusicians.add(defaultSm);

        Musician musiConnect = new Musician("MusiConnect", "SDP", "musiConnect", "musiconnectsdp@gmail.com", new MyDate(1992, 9, 20));
        musiConnect.setTypeOfUser(TypeOfUser.Band);

        b = new Band("totofire", m.getEmailAddress());
        b.addMember(musiConnect .getEmailAddress());

        Musician m1 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
        m1.addEvent("1");

        Musician m2 = new Musician("Alice", "Bardon", "Alyx", "aymanmezghani97@gmail.com", new MyDate(1992, 9, 20));
        m2.addEvent("2");

        listOfMusicians.add(new SimplifiedMusician(m1));
        listOfMusicians.add(new SimplifiedMusician(m2));
        listOfMusicians.add(new SimplifiedMusician(new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1))));
        listOfMusicians.add(new SimplifiedMusician(musiConnect));

        listOfEvent.add(createEvent(getDummyMusician(1), "1"));
        listOfEvent.add(createEvent(getDummyMusician(2), "2"));
    }

    public Musician getDummyMusician(int index) {
        return (listOfMusicians.get(index)).toMusician();
    }

    public Event getDummyEvent(int index) {
        return listOfEvent.get(index);
    }

    private Event createEvent(User user, String eid) {
        Musician m2 = getDummyMusician(3);

        Event event = new Event(user, eid);
        event.setAddress("Westminster, London, England");
        event.setLocation(51.5007, 0.1245);
        event.setDateTime(new MyDate(2020, 9, 21, 14, 30));
        event.setTitle("Event at Big Ben!");
        event.setDescription("Playing at Big Ben, come watch us play!");
        event.register(m2);

        return event;
    }

    @Override
    void addDoc(String collection, String docName, SimplifiedDbEntry entry) {
        if(collection.equals(DbUserType.Band.toString())) {
            this.b = ((SimplifiedBand)entry).toBand();
        }
    }

    @Override
    void addDoc(SimplifiedEvent simplifiedEvent, DbUserType userType) {

    }

    @Override
    void deleteDoc(String collection, String docName) {
    }

    @Override
    void updateDoc(String collection, String docName, Map<String, Object> newValueMap) {
        Object value = newValueMap.get(SimplifiedMusician.EVENTS);

        if (collection.equals(DbUserType.Musician.toString()) && docName.equals(getDummyMusician(1).getEmailAddress()) && value != null) {
            getDummyMusician(1).setEvents((List<String>) value);
        }


    }

    @Override
    void deleteFieldsInDoc(String collection, String docName, List<String> fields) {
    }

    @Override
    void readDoc(String collection, String docName, DbCallback dbCallback) {
        if(collection.equals(DbUserType.Events.toString())) {
            for (Event e : listOfEvent) {
                if (docName.equals(e.getEid())) {
                    dbCallback.readCallback(e);
                    return;
                }
            }

        }

        if (collection.equals(DbUserType.Musician.toString())) {
            for (SimplifiedMusician sm : listOfMusicians) {
                if (docName.equals(sm.getEmail())) {
                    dbCallback.readCallback(sm.toMusician());
                    return;
                }
            }
        }

        if(collection.equals(DbUserType.Band.toString())) {
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
        if(collection.equals(DbUserType.Musician.toString())) {
            l.add(defaultSm.toMusician());
            dbCallback.queryCallback(l);
        } else if(collection.equals(DbUserType.Band.toString())) {

            Band b = new Band("totofire" ,defaultSm.getEmail());
            l.add(b);
            Band b2 = new Band("testBand", "espresso@gmail.com");
            l.add(b2);
            dbCallback.queryCallback(l);
        }
    }

    public Band getBand() {
        return this.b;
    }
}

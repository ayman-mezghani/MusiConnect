package ch.epfl.sdp.musiconnect.database;

import java.util.Map;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.events.Event;

public class DbAdapter {

    private Database db;

    DbAdapter(Database db) {
        this.db = db;
    }

    public void add(DbUserType userType, User user) {
        if(userType.equals(DbUserType.Musician)) {
            Musician musician = (Musician) user;
            db.addDoc(userType.toString(), musician.getEmailAddress(), new SimplifiedMusician(musician));
        }
        else {
            Band band = (Band) user;
            db.addDoc(userType.toString(), band.getEmailAddress(), new SimplifiedBand(band));
        }
    }
    public void add(Event e, DbUserType userType) {
        db.addDoc(new SimplifiedEvent(e), userType);
    }

    public void delete(DbUserType userType, User user) {
        if(userType.equals(DbUserType.Musician)) {
            Musician musician = (Musician) user;
            db.deleteDoc(userType.toString(), musician.getEmailAddress());
        }
        else {
            Band band = (Band) user;
            db.deleteDoc(userType.toString(), band.getEmailAddress());
        }
    }

    public void delete(DbUserType userType, Event event) {
        db.deleteDoc(userType.toString(), event.getEid());
    }


    public void update(DbUserType userType, User user) {
        if(userType.equals(DbUserType.Musician)) {
            Musician musician = (Musician) user;
            db.updateDoc(userType.toString(), musician.getEmailAddress(), (new SimplifiedMusician(musician)).toMap());
        }
        else {
            Band band = (Band) user;
            db.updateDoc(userType.toString(), band.getEmailAddress(), (new SimplifiedBand(band)).toMap());
        }
    }

    public void update(DbUserType userType, Event e) {
        db.updateDoc(userType.toString(), e.getEid(), (new SimplifiedEvent(e)).toMap());
    }

//    public void deleteFieldsInDoc(String docName, List<String> fields) {
//        Map<String, Object> updates = new HashMap<>();
//        for (String str : fields) {
//            updates.put(str, FieldValue.delete());
//        }
//        this.updateDoc(docName, updates);
//    }

    public void read(DbUserType userType, String index, DbCallback dbCallback) {
        db.readDoc(userType.toString(), index, dbCallback);
    }

    public void exists(DbUserType collection, String index, DbCallback dbCallback) {
        db.docExists(collection.toString(), index, dbCallback);
    }

    public void query(DbUserType collection, Map<String, Object> filters, DbCallback dbCallback) {
        db.finderQuery(collection.toString(), filters, dbCallback);
    }
}
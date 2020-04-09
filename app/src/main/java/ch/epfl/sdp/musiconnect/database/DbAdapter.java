package ch.epfl.sdp.musiconnect.database;

import ch.epfl.sdp.musiconnect.Musician;

public class DbAdapter {

    private DataBase db;

    public DbAdapter(DataBase db) {
        this.db = db;
    }

    public void add(Musician musician) {
        db.addDoc(musician.getEmailAddress(), new SimplifiedMusician(musician));
    }

    public void delete(Musician musician) {
        db.deleteDoc(musician.getEmailAddress());
    }

    public void update(Musician musician) {
        db.updateDoc(musician.getEmailAddress(), (new SimplifiedMusician(musician)).toMap());
    }

//    public void deleteFieldsInDoc(String docName, List<String> fields) {
//        Map<String, Object> updates = new HashMap<>();
//        for (String str : fields) {
//            updates.put(str, FieldValue.delete());
//        }
//        this.updateDoc(docName, updates);
//    }

    public void read(String index, DbCallback dbCallback) {
        db.readDoc(index, dbCallback);
    }

    public void exists(String index, DbCallback dbCallback) {
        db.docExists(index, dbCallback);
    }
}
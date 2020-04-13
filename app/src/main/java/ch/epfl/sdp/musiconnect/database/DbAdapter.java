package ch.epfl.sdp.musiconnect.database;

import ch.epfl.sdp.musiconnect.Musician;

public class DbAdapter {

    private DataBase db;

    public DbAdapter(DataBase db) {
        this.db = db;
    }

    public void add(String collection, Musician musician) {
        db.addDoc(collection, musician.getEmailAddress(), new SimplifiedMusician(musician));
    }

    public void delete(String collection, Musician musician) {
        db.deleteDoc(collection, musician.getEmailAddress());
    }

    public void update(String collection, Musician musician) {
        db.updateDoc(collection, musician.getEmailAddress(), (new SimplifiedMusician(musician)).toMap());
    }

//    public void deleteFieldsInDoc(String docName, List<String> fields) {
//        Map<String, Object> updates = new HashMap<>();
//        for (String str : fields) {
//            updates.put(str, FieldValue.delete());
//        }
//        this.updateDoc(docName, updates);
//    }

    public void read(String collection, String index, DbCallback dbCallback) {
        db.readDoc(collection, index, dbCallback);
    }

    public void exists(String collection, String index, DbCallback dbCallback) {
        db.docExists(collection, index, dbCallback);
    }
}
package ch.epfl.sdp.musiconnect.database;

import ch.epfl.sdp.musiconnect.Musician;

public class DbAdapter {

    private DataBase db = new DataBase();

    public void add(Musician musician) {
        db.addDoc(musician.getUserName(), new SimplifiedMusician(musician));
    }

    public void delete(Musician musician) {
        db.deleteDoc(musician.getUserName());
    }

//    public void update(Musician musician) {
//        db.updateDoc();
//    }
//
//    public void deleteFieldsInDoc(String docName, List<String> fields) {
//        Map<String, Object> updates = new HashMap<>();
//        for (String str : fields) {
//            updates.put(str, FieldValue.delete());
//        }
//        this.updateDoc(docName, updates);
//    }
//
//    public void readDoc(String docName, DbCallback dbCallback) {
//        db.collection("users").document(docName).get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    SimplifiedMusician data = documentSnapshot.getData();
//                    dbCallback.onCallback(data);
//                })
//                .addOnFailureListener(e -> Log.w(TAG, "Error reading document", e));
//    }
}

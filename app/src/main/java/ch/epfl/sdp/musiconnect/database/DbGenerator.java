package ch.epfl.sdp.musiconnect.database;

import android.util.Log;

import ch.epfl.sdp.musiconnect.AppStatus;

public class DbGenerator {
    // static variable single_instance of type Singleton
    private static DbAdapter single_instance = null;

    // static method to create instance of Singleton class
    public static DbAdapter getDbInstance() {
        if (single_instance == null)
            new DbGenerator();

        return single_instance;
    }

    // private constructor restricted to this class itself
    private DbGenerator() {
        Database db = AppStatus.isInTest() ? new MockDatabase() : new FirebaseDatabase();
        Log.d("AppStatus", db.getClass().getName());
        single_instance = new DbAdapter(db);
    }
}
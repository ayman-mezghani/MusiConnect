package ch.epfl.sdp.musiconnect.database;

import android.util.Log;


public class DbGenerator {
    // static variable single_instance of type Singleton
    private static DbAdapter single_instance = null;
    private static Database database;

    // static method to create instance of Singleton class
    public static DbAdapter getDbInstance() {
        if (single_instance == null)
            new DbGenerator();

        return single_instance;
    }

    public static void setDatabase(Database database) {
        DbGenerator.database = database;
    }

    // private constructor restricted to this class itself
    private DbGenerator() {
        if(database == null) database = new FirebaseDatabase();
        single_instance = new DbAdapter(database);
    }

    static void flush() {
        database = null;
        single_instance = null;
    }
}
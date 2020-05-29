package ch.epfl.sdp.musiconnect.database;


public class DbSingleton {
    // static variable single_instance of type Singleton
    private static DbAdapter single_instance = null;
    private static Database database;

    // static method to create instance of Singleton class
    public static DbAdapter getDbInstance() {
        if (single_instance == null)
            new DbSingleton();

        return single_instance;
    }

    public static void setDatabase(Database database) {
        DbSingleton.database = database;
    }

    // private constructor restricted to this class itself
    private DbSingleton() {
        if(database == null) database = new FirebaseDatabase();
        single_instance = new DbAdapter(database);
    }

    static void flush() {
        database = null;
        single_instance = null;
    }
}
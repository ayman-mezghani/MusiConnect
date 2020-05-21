package ch.epfl.sdp.musiconnect.cloud;

import android.content.Context;


public class CloudStorageSingleton {
    // static variable single_instance of type Singleton
    private static CloudStorage storage;

    // static method to create instance of Singleton class
    public static CloudStorage getCloudInstance(Context context) {
        if (storage == null)
            new CloudStorageSingleton(context);

        return storage;
    }

    public static void setStorage(CloudStorage storage) {
        CloudStorageSingleton.storage = storage;
    }

    // private constructor restricted to this class itself
    private CloudStorageSingleton(Context context) {
        if(storage == null) storage = new FirebaseCloudStorage(context);
    }

    public static void flush() {
        storage = null;
    }
}
package ch.epfl.sdp.musiconnect.cloud;

import android.content.Context;


public class CloudStorageGenerator {
    // static variable single_instance of type Singleton
    private static CloudStorage storage;

    // static method to create instance of Singleton class
    public static CloudStorage getDbInstance(Context context) {
        if (storage == null)
            new CloudStorageGenerator(context);

        return storage;
    }

    public static void setStorage(CloudStorage storage) {
        CloudStorageGenerator.storage = storage;
    }

    // private constructor restricted to this class itself
    private CloudStorageGenerator(Context context) {
        if(storage == null) storage = new FirebaseCloudStorage(context);
    }
}
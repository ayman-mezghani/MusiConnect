package ch.epfl.sdp.musiconnect.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.events.Event;

@Database(entities = {Musician.class, Event.class}, version = 2)
@TypeConverters({InstrumentConverter.class,MyDateConverter.class,MyLocationConverter.class,LocationConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;
    public abstract MusicianDao musicianDao();
    public abstract EventDao eventDao();

    public static AppDatabase getInstance(final Context context){
        if(sInstance == null){
            synchronized (AppDatabase.class){
                if (sInstance == null){
                    if(checkTest()) {
                        sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, "test-database-name")
                                .fallbackToDestructiveMigration()
                                .build();
                    } else {
                        sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, "database-name")
                                .fallbackToDestructiveMigration()
                                .build();
                    }
                }
            }
        }
        return sInstance;
    }

    private static boolean checkTest(){
        boolean isTest;
        try{
            Class.forName("androidx.test.espresso.Espresso");
            isTest =  true;
        } catch(ClassNotFoundException e){
            isTest = false;
        }
        return isTest;
    }
}

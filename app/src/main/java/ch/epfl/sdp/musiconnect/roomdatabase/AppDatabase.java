package ch.epfl.sdp.musiconnect.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ch.epfl.sdp.musiconnect.Musician;

@Database(entities = {Musician.class}, version = 1)
@TypeConverters({InstrumentConverter.class,MyDateConverter.class,MyLocationConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    public abstract MusicianDao musicianDao();

    public static AppDatabase getInstance(final Context context){
        if(sInstance == null){
            synchronized (AppDatabase.class){
                if (sInstance == null){
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database-name").build();
                }
            }
        }
        return sInstance;
    }
}

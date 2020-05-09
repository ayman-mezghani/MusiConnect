package ch.epfl.sdp.musiconnect.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ch.epfl.sdp.musiconnect.events.Event;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Update
    void updateUsers(Event... events);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event")
    void nukeTable();
}

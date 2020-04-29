package ch.epfl.sdp.musiconnect.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ch.epfl.sdp.musiconnect.Musician;

@Dao
public interface MusicianDao {
    @Query("SELECT * FROM musician")
    List<Musician> getAll();

    @Query("SELECT * FROM musician WHERE emailAddress IN (:musicianEmail)")
    List<Musician> loadAllByIds(String[] musicianEmail);

    @Update
    void updateUsers(Musician... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Musician... users);

    @Delete
    void delete(Musician user);

    @Query("DELETE FROM musician")
    void nukeTable();
}

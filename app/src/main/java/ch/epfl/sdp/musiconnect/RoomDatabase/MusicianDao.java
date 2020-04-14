package ch.epfl.sdp.musiconnect.RoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ch.epfl.sdp.musiconnect.Musician;

@Dao
public interface MusicianDao {
    @Query("SELECT * FROM musician")
    List<Musician> getAll();

    @Query("SELECT * FROM musician WHERE emailAddress IN (:musicianEmail)")
    List<Musician> loadAllByIds(int[] musicianEmail);

    @Query("SELECT * FROM musician WHERE firstName LIKE :first AND " +
            "lastName LIKE :last LIMIT 1")
    Musician findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Musician... users);

    @Delete
    void delete(Musician user);

    @Query("DELETE FROM musician")
    void nukeTable();
}

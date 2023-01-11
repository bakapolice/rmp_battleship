package com.rmpcourse.battleship.data.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LeaderboardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Leaderboard leaderboard);

    @Update
    void update(Leaderboard leaderboard);

    @Delete
    void delete(Leaderboard leaderboard);

    @Query("select * from leaderboards where leaderboardId = :id")
    Leaderboard findById(int id);

    @Query("select * from leaderboards")
    LiveData<List<Leaderboard>> getAllLeaderboards();
}

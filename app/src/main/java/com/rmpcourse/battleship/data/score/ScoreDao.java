package com.rmpcourse.battleship.data.score;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Score score);

    @Update
    void update(Score score);

    @Delete
    void delete(Score score);

    @Query("select * from scores where scoreId = :id")
    Score findById(int id);

    @Query("select * from scores")
    LiveData<List<Score>> getAllScores();
}

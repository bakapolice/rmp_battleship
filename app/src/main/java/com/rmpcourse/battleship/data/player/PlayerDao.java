package com.rmpcourse.battleship.data.player;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Player player);

    @Update
    void update(Player player);

    @Delete
    void delete(Player player);

    @Query("select * from players where playerId = :id")
    Player findById(int id);

    /* TODO find by username and password */

    @Query("select * from players")
    LiveData<List<Player>> getAllPlayers();

    @Transaction
    @Query("select * from players")
    LiveData<List<PlayerWithScores>> getPlayersWithScores();

    /* TODO: test and edit if not working */

    @Transaction
    @Query("select * from players where playerId = :id")
    LiveData<List<PlayerWithScores>> getPlayerWithScoresById(int id);
}

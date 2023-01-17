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
    long insert(Player player);

    @Update
    void update(Player player);

    @Delete
    void delete(Player player);

    @Query("select * from players where playerId = :id")
    Player findById(long id);

    @Query("select * from players where email = :email")
    Player findByEmail(String email);

    @Query("select * from players")
    LiveData<List<Player>> getAllPlayers();

    @Query("select * from players where username = :username and password = :password")
    Player findByUsernameAndPassword(String username, String password);

    @Query("select * from players where username = :username")
    Player findByUsername(String username);

    @Transaction
    @Query("select * from players")
    LiveData<List<PlayerWithScores>> getPlayersWithScores();

    /* TODO: test and edit if not working */

    @Transaction
    @Query("select * from players where playerId = :id")
    LiveData<PlayerWithScores> getPlayerWithScoresById(long id);

    @Transaction
    @Query("select * from players")
    public LiveData<List<PlayerAndLeaderboard>> getPlayersAndLeaderboards();

    @Transaction
    @Query("select * from players where playerId = :id")
    public PlayerAndLeaderboard getPlayerAndLeaderboardByPlayerId(long id);
}

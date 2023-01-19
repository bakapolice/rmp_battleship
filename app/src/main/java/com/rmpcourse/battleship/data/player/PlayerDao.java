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

// Абстрактный интерфейс для работы с таблицей пользователей
@Dao
public interface PlayerDao {

    // Вствка в бд
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Player player);

    // Обновление
    @Update
    void update(Player player);

    // Удаление
    @Delete
    void delete(Player player);

    // поиск по параметру
    @Query("select * from players where playerId = :id")
    Player findById(long id);

    // поиск по эл. почте
    @Query("select * from players where email = :email")
    Player findByEmail(String email);

    // Получить всех пользователей
    @Query("select * from players")
    LiveData<List<Player>> getAllPlayers();

    // Поиск по параметрам
    @Query("select * from players where username = :username and password = :password")
    Player findByUsernameAndPassword(String username, String password);

    // Поиск по параметрам
    @Query("select * from players where username = :username")
    Player findByUsername(String username);

    // Транзакция на получения данных на основе отношений один-ко-многим
    @Transaction
    @Query("select * from players")
    LiveData<List<PlayerWithScores>> getPlayersWithScores();

    // Транзакция на получения данных на основе отношений один-ко-многим
    @Transaction
    @Query("select * from players where playerId = :id")
    LiveData<PlayerWithScores> getPlayerWithScoresById(long id);

    // Транзакция на получения данных на основе отношений один-к-одному
    @Transaction
    @Query("select * from players")
    public LiveData<List<PlayerAndLeaderboard>> getPlayersAndLeaderboards();

    // Транзакция на получения данных на основе отношений один-к-одному
    @Transaction
    @Query("select * from players where playerId = :id")
    public PlayerAndLeaderboard getPlayerAndLeaderboardByPlayerId(long id);
}

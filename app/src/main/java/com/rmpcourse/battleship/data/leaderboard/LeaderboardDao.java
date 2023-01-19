package com.rmpcourse.battleship.data.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Абстрактный интерфейс для работы с таблицей лидеров
@Dao
public interface LeaderboardDao {

    // Вставка в БД
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Leaderboard leaderboard);

    // Обвноеление
    @Update
    void update(Leaderboard leaderboard);

    // Удаление
    @Delete
    void delete(Leaderboard leaderboard);

    // Поиск по параметру
    @Query("select * from leaderboards where leaderboardId = :id")
    Leaderboard findById(int id);

    @Query("select * from leaderboards where player_leaderboard_id = :id")
    Leaderboard findByPlayerId(long id);

    // Получение всех записей
    @Query("select * from leaderboards order by total_wins desc")
    LiveData<List<Leaderboard>> getAllLeaderboards();
}

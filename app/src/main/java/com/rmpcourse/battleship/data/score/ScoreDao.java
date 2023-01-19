package com.rmpcourse.battleship.data.score;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Абстрактный интерфейс для работы с таблицей резульаттов
@Dao
public interface ScoreDao {

    //Вставка
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Score score);

    // Обновление
    @Update
    void update(Score score);

    // Удаление
    @Delete
    void delete(Score score);

    // Поиск по параметру
    @Query("select * from scores where scoreId = :id")
    Score findById(int id);

    // получение всех данных
    @Query("select * from scores")
    LiveData<List<Score>> getAllScores();
}

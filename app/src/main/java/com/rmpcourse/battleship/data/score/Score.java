package com.rmpcourse.battleship.data.score;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Таблица "Результаты"
@Entity(tableName = "scores")
public class Score {
    @PrimaryKey(autoGenerate = true)
    public int scoreId;

    @ColumnInfo(name = "player_username")
    public String playerUsername = "";

    @ColumnInfo(name = "target_username")
    public String targetUsername = "";

    @ColumnInfo(name = "player_score_id")
    public long playerScoreId;

    public Long date = 0L;

    @ColumnInfo(name = "match_time")
    public int matchTime = 0;

    @ColumnInfo(name = "match_result")
    public String matchResult = "";
}

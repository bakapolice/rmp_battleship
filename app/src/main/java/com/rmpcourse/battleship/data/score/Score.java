package com.rmpcourse.battleship.data.score;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scores")
public class Score {
    @PrimaryKey(autoGenerate = true)
    public int scoreId;

    @ColumnInfo(name = "player_username")
    public String playerUsername = "";

    @ColumnInfo(name = "target_username")
    public String targetUsername = "";

    @ColumnInfo(name = "player_id")
    public int playerScoreId;

    public Long date = 0L;

    @ColumnInfo(name = "match_time")
    public double matchTime = 0.0;
}

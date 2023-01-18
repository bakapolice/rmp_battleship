package com.rmpcourse.battleship.data.leaderboard;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "leaderboards")
public class Leaderboard {
    @PrimaryKey(autoGenerate = true)
    public int leaderboardId;

    @ColumnInfo(name = "player_username")
    public String playerUsername = "";

    @ColumnInfo(name = "player_leaderboard_id")
    public long playerLeaderboardId;

    @ColumnInfo(name = "total_wins")
    public int totalWins = 0;

    @ColumnInfo(name = "total_losses")
    public int totalLosses = 0;
}

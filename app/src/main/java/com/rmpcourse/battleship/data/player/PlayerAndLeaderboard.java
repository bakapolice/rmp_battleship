package com.rmpcourse.battleship.data.player;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.rmpcourse.battleship.data.leaderboard.Leaderboard;

public class PlayerAndLeaderboard {
    @Embedded public Player player;
    @Relation(
            parentColumn = "playerId",
            entityColumn = "player_leaderboard_id"
    )
    public Leaderboard leaderboard;
}

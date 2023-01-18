package com.rmpcourse.battleship.data.player;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.rmpcourse.battleship.data.score.Score;

import java.util.List;

public class PlayerWithScores {
    @Embedded
    public Player player;
    @Relation(
            parentColumn = "playerId",
            entityColumn = "player_score_id"
    )
    public List<Score> scores;
}

package com.rmpcourse.battleship.data;

import androidx.room.Database;

import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.data.leaderboard.LeaderboardDao;
import com.rmpcourse.battleship.data.player.Player;
import com.rmpcourse.battleship.data.player.PlayerDao;
import com.rmpcourse.battleship.data.score.Score;
import com.rmpcourse.battleship.data.score.ScoreDao;

@Database(entities = {Player.class, Score.class, Leaderboard.class}, version = 1, exportSchema = false)
public abstract class BattleshipDatabase
{

    /* TODO: read about abstract class */
    public abstract PlayerDao playerDao();
    public abstract ScoreDao scoreDao();
    public abstract LeaderboardDao leaderboardDao();
}

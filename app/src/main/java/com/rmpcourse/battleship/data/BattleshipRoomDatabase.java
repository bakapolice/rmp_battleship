package com.rmpcourse.battleship.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.data.leaderboard.LeaderboardDao;
import com.rmpcourse.battleship.data.player.Player;
import com.rmpcourse.battleship.data.player.PlayerDao;
import com.rmpcourse.battleship.data.score.Score;
import com.rmpcourse.battleship.data.score.ScoreDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Player.class, Score.class, Leaderboard.class}, version = 1, exportSchema = false)
public abstract class BattleshipRoomDatabase extends RoomDatabase {

    public abstract PlayerDao playerDao();

    public abstract ScoreDao scoreDao();

    public abstract LeaderboardDao leaderboardDao();


    private static volatile BattleshipRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static BattleshipRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BattleshipRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BattleshipRoomDatabase.class, "battleship_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

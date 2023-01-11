package com.rmpcourse.battleship.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.data.leaderboard.LeaderboardDao;
import com.rmpcourse.battleship.data.player.Player;
import com.rmpcourse.battleship.data.player.PlayerDao;
import com.rmpcourse.battleship.data.score.Score;
import com.rmpcourse.battleship.data.score.ScoreDao;

import java.util.List;

public class DataRepository {

    private PlayerDao mPlayerDao;
    private LiveData<List<Player>> mAllPlayers;

    private ScoreDao mScoreDao;
    /* TODO: get to know how to replace with one-to-many relation action */
    private LiveData<List<Score>> mAllScores;

    private LeaderboardDao mLeaderboardDao;
    private LiveData<List<Leaderboard>> mAllLeaderboards;

    DataRepository(Application application) {
        BattleshipRoomDatabase db = BattleshipRoomDatabase.getDatabase(application);
        mPlayerDao = db.playerDao();
        mAllPlayers = mPlayerDao.getAllPlayers();

        mScoreDao = db.scoreDao();
        mAllScores = mScoreDao.getAllScores();

        mLeaderboardDao = db.leaderboardDao();
        mAllLeaderboards = mLeaderboardDao.getAllLeaderboards();
    }


    /***
     * Player
     */

    void insert(Player player){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mPlayerDao.insert(player));
    }

    void update(Player player){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mPlayerDao.update(player));
    }

    void delete(Player player){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mPlayerDao.delete(player));
    }

    LiveData<List<Player>> getAllPlayers(){
        return mAllPlayers;
    }


    /***
     * Score
     */

    void insert(Score score){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.insert(score));
    }

    void update(Score score){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.update(score));
    }

    void delete(Score score){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.delete(score));
    }

    LiveData<List<Score>> getAllScores(){
        return mAllScores;
    }


    /***
     * Leaderboard
     */

    void insert(Leaderboard leaderboard){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mLeaderboardDao.insert(leaderboard));
    }

    void update(Leaderboard leaderboard){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mLeaderboardDao.update(leaderboard));
    }

    void delete(Leaderboard leaderboard){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mLeaderboardDao.delete(leaderboard));
    }

    LiveData<List<Leaderboard>> getAllLeaderboards(){
        return mAllLeaderboards;
    }


    /* TODO: add async find by id and etc. */
}

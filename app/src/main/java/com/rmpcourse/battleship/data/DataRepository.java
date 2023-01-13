package com.rmpcourse.battleship.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.data.leaderboard.LeaderboardDao;
import com.rmpcourse.battleship.data.player.Player;
import com.rmpcourse.battleship.data.player.PlayerDao;
import com.rmpcourse.battleship.data.player.PlayerWithScores;
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

    public DataRepository(Application application) {
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

    public void insert(Player player){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mPlayerDao.insert(player));
    }

    public void update(Player player){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mPlayerDao.update(player));
    }

    public void delete(Player player){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mPlayerDao.delete(player));
    }

    public LiveData<List<Player>> getAllPlayers(){
        return mAllPlayers;
    }


    /***
     * Score
     */

    public void insert(Score score){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.insert(score));
    }

    public void update(Score score){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.update(score));
    }

    public void delete(Score score){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.delete(score));
    }

    public LiveData<List<Score>> getAllScores(){
        return mAllScores;
    }


    /***
     * Leaderboard
     */

    public void insert(Leaderboard leaderboard){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mLeaderboardDao.insert(leaderboard));
    }

    public void update(Leaderboard leaderboard){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mLeaderboardDao.update(leaderboard));
    }

    public void delete(Leaderboard leaderboard){
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mLeaderboardDao.delete(leaderboard));
    }

    public LiveData<List<Leaderboard>> getAllLeaderboards(){
        return mAllLeaderboards;
    }


    /* TODO: add async find by id and etc. */
}

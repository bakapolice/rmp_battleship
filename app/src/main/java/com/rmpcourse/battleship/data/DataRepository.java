package com.rmpcourse.battleship.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.data.leaderboard.LeaderboardDao;
import com.rmpcourse.battleship.data.player.Player;
import com.rmpcourse.battleship.data.player.PlayerAndLeaderboard;
import com.rmpcourse.battleship.data.player.PlayerDao;
import com.rmpcourse.battleship.data.player.PlayerWithScores;
import com.rmpcourse.battleship.data.score.Score;
import com.rmpcourse.battleship.data.score.ScoreDao;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DataRepository {

    // Поля класса репозитория
    private PlayerDao mPlayerDao;
    private LiveData<List<Player>> mAllPlayers;

    private ScoreDao mScoreDao;

    private LiveData<List<Score>> mAllScores;

    private LeaderboardDao mLeaderboardDao;
    private LiveData<List<Leaderboard>> mAllLeaderboards;


    // Конструктор репозиторя, при создании репозитория получает ссылку на экземпляр БД,
    // ссылки на все DAO и вызывает методы получения списокв данных из БД
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

    // Асинхронная запись в БД
    public long insert(Player player) {
        //BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> id = mPlayerDao.insert(player));
        Callable<Long> insertCallable = () -> mPlayerDao.insert(player);
        long id = -1;
        Future<Long> future = BattleshipRoomDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    // Асинхронное обновление записи в БД
    public void update(Player player) {
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mPlayerDao.update(player));
    }

    // Асинхронное удаление записи из БД
    public void delete(Player player) {
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mPlayerDao.delete(player));
    }

    // Room все запросы на отдельных потоках
    // Прослушиваемая LiveData оповестит подписчиков, когда данные изменятся
    public LiveData<List<Player>> getAllPlayers() {
        return mAllPlayers;
    }

    LiveData<List<PlayerWithScores>> getPlayersWithScores() {
        return mPlayerDao.getPlayersWithScores();
    }
    public LiveData<PlayerWithScores> getPlayerWithScoresById(long id) {
        return mPlayerDao.getPlayerWithScoresById(id);
    }
    public LiveData<List<PlayerAndLeaderboard>> getPlayersAndLeaderboards() {
        return mPlayerDao.getPlayersAndLeaderboards();
    }

    // Поиск записи в БД по параметру
    public Player findPlayerById(long id) {
        return mPlayerDao.findById(id);
    }

    public Player findPlayerByEmail(String email) {
        return mPlayerDao.findByEmail(email);
    }

    public Player findPlayerByUsernameAndPassword(String username, String password) {
        return mPlayerDao.findByUsernameAndPassword(username, password);
    }

    public Player findPlayerByUsername(String username) {
        return mPlayerDao.findByUsername(username);
    }

    public PlayerAndLeaderboard getPlayerAndLeaderboardByPlayerId(long id) {
        return mPlayerDao.getPlayerAndLeaderboardByPlayerId(id);
    }

    /***
     * Score
     */

    // Аналогично методам выше
    public void insert(Score score) {
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.insert(score));
    }

    public void update(Score score) {
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.update(score));
    }

    public void delete(Score score) {
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.delete(score));
    }

    public LiveData<List<Score>> getAllScores() {
        return mAllScores;
    }

    public Score findScoreById(int id) {
        return mScoreDao.findById(id);
    }


    /***
     * Leaderboard
     */

    public void insert(Leaderboard leaderboard) {
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mLeaderboardDao.insert(leaderboard));
    }

    public void update(Leaderboard leaderboard) {
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mLeaderboardDao.update(leaderboard));
    }

    public void delete(Leaderboard leaderboard) {
        BattleshipRoomDatabase.databaseWriteExecutor.execute(() -> mLeaderboardDao.delete(leaderboard));
    }

    public LiveData<List<Leaderboard>> getAllLeaderboards() {
        return mAllLeaderboards;
    }

    public Leaderboard findLeaderboardById(int id) {
        return mLeaderboardDao.findById(id);
    }

    public Leaderboard findLeaderboardByPlayerId(long id) {
        return mLeaderboardDao.findByPlayerId(id);
    }
}

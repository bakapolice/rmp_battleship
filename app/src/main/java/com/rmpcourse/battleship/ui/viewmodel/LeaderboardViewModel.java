package com.rmpcourse.battleship.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.DataRepository;
import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.data.player.PlayerAndLeaderboard;

import java.util.List;

public class LeaderboardViewModel extends AndroidViewModel {

    // Поле для хранения ссылки на репозиторий
    private DataRepository mRepository;

    // Поле для хранения списка всех таблци лидеров из БД
    private final LiveData<List<Leaderboard>> mAllLeaderboards;

    // При создании модели данных получаем ссылку на репозиторий
    // и получаем список всех таблиц лидеров
    public LeaderboardViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllLeaderboards = mRepository.getAllLeaderboards();
    }

    // Метод получения всех таблиц лидеров из модели данных
    public LiveData<List<Leaderboard>> getAllLeaderboards() {
        return mAllLeaderboards;
    }

    // Вставка в БД
    public void insert(Leaderboard leaderboard) {
        mRepository.insert(leaderboard);
    }

    // Обновление записи в БД
    public void update(Leaderboard leaderboard) {
        mRepository.update(leaderboard);
    }

    // Удаление записи в БД
    public void delete(Leaderboard leaderboard) {
        mRepository.delete(leaderboard);
    }

    // Поиск и возвращение всех пользователей и их записей для таблицы лидеров
    public LiveData<List<PlayerAndLeaderboard>> getPlayersAndLeaderboards() {
        return mRepository.getPlayersAndLeaderboards();
    }


    // Найти таблицу лидеров по ID и вернуть
    public Leaderboard findLeaderboardById(int id) {
        return mRepository.findLeaderboardById(id);
    }

    // Поиск таблицы лидеров по id игрока
    public Leaderboard findLeaderboardByPlayerId(long id) {
        return mRepository.findLeaderboardByPlayerId(id);
    }

    // Получить игрока и его запись для таблицы лидерв по id игрока
    public PlayerAndLeaderboard getPlayerAndLeaderboardByPlayerId(long id) {
        return mRepository.getPlayerAndLeaderboardByPlayerId(id);
    }
}

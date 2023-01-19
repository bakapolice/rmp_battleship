package com.rmpcourse.battleship.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.DataRepository;
import com.rmpcourse.battleship.data.player.PlayerWithScores;
import com.rmpcourse.battleship.data.score.Score;

import java.util.List;

public class ScoresViewModel extends AndroidViewModel {

    // Поле для хранения ссылки на репозиторий
    private DataRepository mRepository;

    // Поле для хранения списка всех результатов из БД
    private final LiveData<List<Score>> mAllScores;

    // Поле для хранения списка всех результатов текущего пользователя
    private LiveData<PlayerWithScores> mPlayerScores = new LiveData<PlayerWithScores>() {
    };

    // При создании модели данных получаем ссылку на репозиторий
    // и получаем список всех результатов
    public ScoresViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllScores = mRepository.getAllScores();
    }

    // Метод получения всех результатов из модели данных
    public LiveData<List<Score>> getAllScores() {
        return mAllScores;
    }

    // Вставка в БД
    public void insert(Score score) {
        mRepository.insert(score);
    }

    // Обновление записи в БД
    public void update(Score score) {
        mRepository.update(score);
    }

    // Удаление записи в БД
    public void delete(Score score) {
        mRepository.delete(score);
    }

    // Поиск в БД и сохарение найденного значения в поле класса
    public void findPlayerWithScoresById(long id) {
        if (id != -1) {
            mPlayerScores = mRepository.getPlayerWithScoresById(id);
        } else mPlayerScores = new LiveData<PlayerWithScores>() {
        };
    }

    // Получение списка всех результатов пользователя из модели данных
    public LiveData<PlayerWithScores> getPlayerWithScores() {
        return mPlayerScores;
    }
}

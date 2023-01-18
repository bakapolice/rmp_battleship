package com.rmpcourse.battleship.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.DataRepository;
import com.rmpcourse.battleship.data.player.PlayerWithScores;
import com.rmpcourse.battleship.data.score.Score;

import java.util.List;

public class ScoresViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private final LiveData<List<Score>> mAllScores;

    private LiveData<PlayerWithScores> mPlayerScores = new LiveData<PlayerWithScores>() {
    };

    public ScoresViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllScores = mRepository.getAllScores();
    }

    public LiveData<List<Score>> getAllScores() {
        return mAllScores;
    }

    public void insert(Score score) {
        mRepository.insert(score);
    }

    public void update(Score score) {
        mRepository.update(score);
    }

    public void delete(Score score) {
        mRepository.delete(score);
    }

    public void findPlayerWithScoresById(long id) {
        if (id != -1) {
            mPlayerScores = mRepository.getPlayerWithScoresById(id);
        } else mPlayerScores = new LiveData<PlayerWithScores>() {
        };
    }

    public LiveData<PlayerWithScores> getPlayerWithScores() {
        return mPlayerScores;
    }
}

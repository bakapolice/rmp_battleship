package com.rmpcourse.battleship.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.DataRepository;
import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.data.player.PlayerAndLeaderboard;

import java.util.List;

public class LeaderboardViewModel extends AndroidViewModel {

    private DataRepository mRepository;
    private final LiveData<List<Leaderboard>> mAllLeaderboards;

    public LeaderboardViewModel(Application application){
        super(application);
        mRepository = new DataRepository(application);
        mAllLeaderboards = mRepository.getAllLeaderboards();
    }

    public LiveData<List<Leaderboard>> getAllLeaderboards() { return mAllLeaderboards; }
    public void insert(Leaderboard leaderboard) { mRepository.insert(leaderboard);}
    public void update(Leaderboard leaderboard) { mRepository.update(leaderboard); }
    public void delete(Leaderboard leaderboard) { mRepository.delete(leaderboard); }
    public LiveData<List<PlayerAndLeaderboard>> getPlayersAndLeaderboards() { return mRepository.getPlayersAndLeaderboards(); }
    public Leaderboard findLeaderboardById(int id) { return mRepository.findLeaderboardById(id);}
    public Leaderboard findLeaderboardByPlayerId(long id) { return mRepository.findLeaderboardByPlayerId(id);}

    public PlayerAndLeaderboard getPlayerAndLeaderboardByPlayerId(long id){
        return mRepository.getPlayerAndLeaderboardByPlayerId(id);
    }
}

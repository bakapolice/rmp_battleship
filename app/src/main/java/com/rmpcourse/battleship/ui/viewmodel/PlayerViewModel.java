package com.rmpcourse.battleship.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.DataRepository;
import com.rmpcourse.battleship.data.player.Player;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private final LiveData<List<Player>> mAllPlayers;

    public PlayerViewModel(Application application){
        super(application);
        mRepository = new DataRepository(application);
        mAllPlayers = mRepository.getAllPlayers();
    }

    LiveData<List<Player>> getAllPlayers() { return mAllPlayers; }
    public void insert(Player player) { mRepository.insert(player); }
    public void update(Player player) { mRepository.update(player); }
    public void delete(Player player) { mRepository.delete(player); }

}

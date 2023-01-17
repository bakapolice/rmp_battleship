package com.rmpcourse.battleship.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.DataRepository;
import com.rmpcourse.battleship.data.player.Player;

import java.util.List;
import java.util.Objects;

public class PlayerViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private final LiveData<List<Player>> mAllPlayers;

    // Текущий авторизированный игрок
    private Player _player = null;
    private Player _targetPlayer = null;

    public PlayerViewModel(Application application){
        super(application);
        mRepository = new DataRepository(application);
        mAllPlayers = mRepository.getAllPlayers();
    }

    LiveData<List<Player>> getAllPlayers() { return mAllPlayers; }
    public long insert(Player player) { return mRepository.insert(player); }
    public void update(Player player) { mRepository.update(player); }
    public void delete(Player player) { mRepository.delete(player); }

    public void findPlayerById(long id){
        if(id != -1){
            _player = mRepository.findPlayerById(id);
        }
        else _player = null;
    }

    public boolean findPlayerByUsername(String username){
        _player = mRepository.findPlayerByUsername(username);
        return _player != null;
    }

    public boolean findPlayerByEmail(String email){
        _player = mRepository.findPlayerByEmail(email);
        return _player != null;
    }

    public boolean findTargetPlayerByUsername(String username){
        _targetPlayer = mRepository.findPlayerByUsername(username);
        return _targetPlayer != null;
    }

    public void findTargetPlayerById(long id){
        if(id != -1){
            _targetPlayer = mRepository.findPlayerById(id);
        }
        else _targetPlayer = null;
    }

    public boolean findTargetPlayerByEmail(String email){
        _targetPlayer = mRepository.findPlayerByEmail(email);
        return _targetPlayer != null;
    }

    public boolean findPlayerByUsernameAndPassword(String username, String password){
        _player = mRepository.findPlayerByUsernameAndPassword(username, password);
        return _player != null;
    }

    public Player getPlayer() { return _player; }
    void setPlayer(Player value) { _player = value; }

    public Player getTargetPlayer() { return _targetPlayer; }
    void setTargetPlayer(Player value) { _targetPlayer = value; }

}

package com.rmpcourse.battleship.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rmpcourse.battleship.data.DataRepository;
import com.rmpcourse.battleship.data.player.Player;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {
    // Поле для хранения ссылки на репозиторий
    private DataRepository mRepository;
    // Поле для хранения списка всех пользователей из БД
    private final LiveData<List<Player>> mAllPlayers;

    // Текущий авторизированный игрок
    private Player _player = null;
    // Текущий противник
    private Player _targetPlayer = null;

    // При создании модели данных получаем ссылку на репозиторий
    // и получаем список всех игроков
    public PlayerViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllPlayers = mRepository.getAllPlayers();
    }

    // Получение списка всех игроков из модели данных
    LiveData<List<Player>> getAllPlayers() {
        return mAllPlayers;
    }

    // Вставка в БД
    public long insert(Player player) {
        return mRepository.insert(player);
    }

    // Обновление записи в БД
    public void update(Player player) {
        mRepository.update(player);
    }

    // Удаление записи в БД
    public void delete(Player player) {
        mRepository.delete(player);
    }

    // Поиск пользователя по id и сохранение данных в поле модели данных
    public void findPlayerById(long id) {
        if (id != -1) {
            _player = mRepository.findPlayerById(id);
        } else _player = null;
    }

    // Поиск пользователя по имени и сохранение данных в поле модели данных
    public boolean findPlayerByUsername(String username) {
        _player = mRepository.findPlayerByUsername(username);
        return _player != null;
    }

    // Поиск пользователя по Эл. почте и сохранение данных в поле модели данных
    public boolean findPlayerByEmail(String email) {
        _player = mRepository.findPlayerByEmail(email);
        return _player != null;
    }


    // Поиск противника по имени и сохранение данных в поле модели данных
    public boolean findTargetPlayerByUsername(String username) {
        _targetPlayer = mRepository.findPlayerByUsername(username);
        return _targetPlayer != null;
    }

    // Поиск противника по id и сохранение данных в поле модели данных
    public void findTargetPlayerById(long id) {
        if (id != -1) {
            _targetPlayer = mRepository.findPlayerById(id);
        } else _targetPlayer = null;
    }

    // Поиск противнкиа по email и сохранение данных в поле модели данных
    public boolean findTargetPlayerByEmail(String email) {
        _targetPlayer = mRepository.findPlayerByEmail(email);
        return _targetPlayer != null;
    }

    // Поиск пользователя по имени и паролю и сохранение данных в поле модели данных
    public boolean findPlayerByUsernameAndPassword(String username, String password) {
        _player = mRepository.findPlayerByUsernameAndPassword(username, password);
        return _player != null;
    }

    // Получение пользователя из модели данных
    public Player getPlayer() {
        return _player;
    }

    // Установка пользователя
    void setPlayer(Player value) {
        _player = value;
    }

    // Получение противника из модели данных
    public Player getTargetPlayer() {
        return _targetPlayer;
    }

    // Установка противника
    void setTargetPlayer(Player value) {
        _targetPlayer = value;
    }

}

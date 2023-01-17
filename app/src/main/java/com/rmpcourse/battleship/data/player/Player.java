package com.rmpcourse.battleship.data.player;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "players")
public class Player {

    @PrimaryKey(autoGenerate = true)
    public long playerId;

    public String username = "";
    public String email = "";
    public String password = "";
}

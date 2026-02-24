package com.example.inventory;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    public String login;
    public String password;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
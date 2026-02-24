package com.example.inventory;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE login = :login AND password = :password")
    User getUser(String login, String password);

    @Query("SELECT * FROM users WHERE login = :login")
    User getUserByLogin(String login);
}
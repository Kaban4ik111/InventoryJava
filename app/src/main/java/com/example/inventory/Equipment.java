package com.example.inventory;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "equipment")
public class Equipment {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    public String name;
    public String condition;
    public int cabinet;
    public int userId;

    public Equipment() {}

    public Equipment(String name, String condition, int cabinet, int userId) {
        this.name = name;
        this.condition = condition;
        this.cabinet = cabinet;
        this.userId = userId;
    }
}
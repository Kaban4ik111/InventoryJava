package com.example.inventory;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface EquipmentDao {
    @Insert
    void insert(Equipment equipment);

    @Update
    void update(Equipment equipment);

    @Delete
    void delete(Equipment equipment);

    @Query("SELECT * FROM equipment WHERE userId = :userId AND cabinet = :cabinet")
    List<Equipment> getEquipmentByCabinet(int userId, int cabinet);

    @Query("SELECT * FROM equipment WHERE userId = :userId AND cabinet = :cabinet AND name LIKE '%' || :searchQuery || '%'")
    List<Equipment> searchEquipment(int userId, int cabinet, String searchQuery);

    @Query("SELECT * FROM equipment WHERE id = :id AND userId = :userId")
    Equipment getEquipmentById(int id, int userId);
}
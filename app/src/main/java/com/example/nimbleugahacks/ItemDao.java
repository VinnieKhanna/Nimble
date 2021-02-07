package com.example.nimbleugahacks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item")
    List<Item> getAll();

    @Query("SELECT * FROM item WHERE itemID IN (:itemIds)")
    List<Item> loadAllByIds(int[] itemIds);

    @Query("SELECT * FROM item WHERE item_name LIKE :itemName LIMIT 1")
    Item findByName(String itemName);

    @Insert
    void insertAll(Item... items);

    @Delete
    void delete(Item item);
}

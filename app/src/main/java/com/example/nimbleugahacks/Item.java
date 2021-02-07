package com.example.nimbleugahacks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {

    public Item(int itemID, String storeName, double price) {
        this.itemID = itemID;
        this.storeName = storeName;
        this.price = price;
    }

    @PrimaryKey
    public int itemID;

    @ColumnInfo(name = "item_name")
    public String storeName;

    @ColumnInfo(name = "price")
    public double price;
    }


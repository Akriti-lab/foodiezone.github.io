package com.akriti.foodiezone.util

import androidx.room.Database
import androidx.room.RoomDatabase
import database.RestaurantEntity

@Database(entities=[RestaurantEntity::class], version=1)
abstract class Restaurantdatabase:RoomDatabase() {
    abstract fun restaurantDao():RestaurantDao
}
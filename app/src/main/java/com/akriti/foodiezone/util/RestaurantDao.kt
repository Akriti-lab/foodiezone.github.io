package com.akriti.foodiezone.util

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import database.RestaurantEntity

@Dao
interface RestaurantDao {
    @Insert
    fun insertRestaurant(restaurantEntity: RestaurantEntity)
    @Delete
    fun deleteRestaurant(restaurantEntity: RestaurantEntity)
    @Query ("SELECT * FROM restaurants")
    fun getAllRestaurants():List<RestaurantEntity>
    @Query("SELECT * FROM restaurants WHERE :RestaurantId")
    fun getRestaurantById(RestaurantId:String):RestaurantEntity
}
package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity (
    @PrimaryKey val RestaurantId:String,
    @ColumnInfo(name = "restaurant_name") val RestaurantName:String,
    @ColumnInfo(name = "restaurant_rating") val RestaurantRating:String,
    @ColumnInfo(name = "restaurant_costforone") val RestaurantCostForOne:String,
    @ColumnInfo(name = "restaurant_image")val RestaurantImage:String


)

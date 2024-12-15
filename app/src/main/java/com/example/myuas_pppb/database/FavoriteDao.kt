package com.example.myuas_pppb.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myuas_pppb.model.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @get:Query("SELECT * from favorites ORDER BY id ASC")
    val getAllFavorite: LiveData<List<Favorite>>

    @Query("SELECT COUNT(*) FROM favorites WHERE name = :name")
    fun isFavorite(name: String): Int

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun getFavoriteById(id: Int): Favorite?

    @Query("DELETE FROM favorites WHERE name = :name")
    fun deleteFavorite(name: String)
}
package com.example.myuas_pppb.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myuas_pppb.model.Cart

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItem(cartItem: Cart)

    @Delete
    fun deleteCartItem(cartItem: Cart)

    @Query("SELECT * FROM carts WHERE name = :name LIMIT 1")
    fun getCartItem(name: String): Cart?

    @Query("SELECT * FROM carts")
    fun getAllCartItems(): LiveData<List<Cart>>

    @Query("UPDATE carts SET qty = :newQuantity WHERE id = :id")
    fun updateCartItemQuantity(id: Int, newQuantity: Int)

    @Query("DELETE FROM carts")
    fun clearAllCartItems()
}
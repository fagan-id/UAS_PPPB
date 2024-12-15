package com.example.myuas_pppb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myuas_pppb.model.Cart

@Database(entities = [Cart::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: CartDatabase? = null
        fun getDatabase(context: Context): CartDatabase? {
            if (INSTANCE == null) {
                synchronized(CartDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CartDatabase::class.java,
                        "carts"
                    ).build()
                }
            }
            return INSTANCE
        }
    }


}
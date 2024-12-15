package com.example.myuas_pppb

import android.content.Context

class PrefManager private constructor(context: Context){
    private val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    companion object{
        private const val PREFS_FILENAME = "AuthAppRef"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_EMAIL = "email"


        @Volatile
        private var instance:PrefManager? = null
        fun getInstance(context: Context): PrefManager{
            return instance ?: synchronized(this){
                instance ?: PrefManager(context.applicationContext).also { pref ->
                    instance = pref
                }
            }
        }
    }

    fun saveUser(username: String, password: String, email: String){
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.putString(KEY_EMAIL, email)
        editor.apply()
    }

    fun getUsername(): String?{
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun saveUsername(username: String){
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun getPassword(): String?{
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }

    fun getEmail(): String?{
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    fun clearUser(){
        sharedPreferences.edit().also {
            it.clear()
            it.apply()
        }
    }

}
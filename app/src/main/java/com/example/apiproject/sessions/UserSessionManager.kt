package com.example.apiproject.sessions

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserSessionPrefs", Context.MODE_PRIVATE)

    fun saveUserLogin(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.contains("username")
    }

    fun getLoggedInUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    fun logoutUser() {
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.apply()
    }
}

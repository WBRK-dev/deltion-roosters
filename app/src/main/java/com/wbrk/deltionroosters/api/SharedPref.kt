package com.wbrk.deltionroosters.api

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class SharedPref(activityClass: AppCompatActivity) {
    val activity = activityClass
    private val sharedPref : SharedPreferences = activity.getSharedPreferences("preffile", MODE_PRIVATE)
    fun read(key: String, default: String) : String? {
        return sharedPref.getString(key, default)
    }
    fun write(key: String, value: String) : Boolean {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
        return true
    }
}
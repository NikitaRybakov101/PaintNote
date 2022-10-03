package com.example.foodnote.data.sharedPreference

import android.content.Context
import com.example.foodnote.ui.activity.MainActivity

object SharedPreference {

    fun saveData(stringData: String, key: String, activity: MainActivity) {
        val editor = activity.getPreferences(Context.MODE_PRIVATE).edit()

        editor.putString(key, stringData)
        editor.apply()
    }

    fun saveDataBoolean(boolean: Boolean, key: String, activity: MainActivity) {
        val editor = activity.getPreferences(Context.MODE_PRIVATE).edit()

        editor.putBoolean(key, boolean)
        editor.apply()
    }

    fun loadDataBoolean(key: String, activity: MainActivity) = activity.getPreferences(Context.MODE_PRIVATE).getBoolean(key, true)

    fun loadDataString(key: String, activity: MainActivity) = activity.getPreferences(Context.MODE_PRIVATE).getString(key, "")
}
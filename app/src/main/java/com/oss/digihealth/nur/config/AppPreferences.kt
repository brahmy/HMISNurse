package com.oss.digihealth.nur.config

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class AppPreferences @SuppressLint("CommitPrefEdits") private constructor(
    context: Context,
    Preferncename: String
) {
    private val appSharedPrefs: SharedPreferences
    private val prefsEditor: SharedPreferences.Editor
    fun getString(key: String?): String? {
        return appSharedPrefs.getString(key, "")
    }

    fun getBoolean(key: String?): Boolean {
        return appSharedPrefs.getBoolean(key, false)
    }

    fun getInt(key: String?): Int {
        return appSharedPrefs.getInt(key, 0)
    }

    fun saveString(Tag: String?, text: String?) {
        prefsEditor.putString(Tag, text)
        prefsEditor.apply()
    }

    fun saveBoolean(Tag: String?, value: Boolean) {
        prefsEditor.putBoolean(Tag, value)
        prefsEditor.commit()
    }

    fun saveInt(Tag: String?, value: Int) {
        prefsEditor.putInt(Tag, value)
        prefsEditor.commit()
    }

    fun clearData() {
        prefsEditor.clear()
        prefsEditor.commit()
    }

    companion object {
        private var instance: AppPreferences? = null
        fun getInstance(
            context: Context,
            Preferncename: String
        ): AppPreferences? {
            if (instance == null) instance =
                AppPreferences(context, Preferncename)
            return instance
        }
    }

    init {
        appSharedPrefs = context.getSharedPreferences(
            Preferncename,
            Activity.MODE_PRIVATE
        )
        prefsEditor = appSharedPrefs.edit()
    }
}
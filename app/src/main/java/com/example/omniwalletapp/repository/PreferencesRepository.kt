package com.example.omniwalletapp.repository

import android.content.Context
import com.example.omniwalletapp.util.Constants
import javax.inject.Inject

class PreferencesRepository @Inject constructor(context: Context) {

    private var prefs = context.getSharedPreferences(Constants.LOCAL_SHARED_PREF, Context.MODE_PRIVATE)

    fun isRememberLogin(): Boolean {
        return prefs.getBoolean("is_remember_login", false)
    }

    fun setRememberLogin(remember: Boolean) {
        prefs.edit().putBoolean("is_remember_login", remember).apply()
    }
}
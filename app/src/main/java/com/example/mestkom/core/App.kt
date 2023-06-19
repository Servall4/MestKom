package com.example.mestkom.core

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.example.mestkom.data.PreferencesManager
import com.example.mestkom.data.map.LocationCommunication
import com.example.mestkom.ui.auth.AuthViewModel
import com.example.mestkom.ui.home.HomeViewModel
import com.example.mestkom.ui.repository.UserRepository
import com.yandex.mapkit.MapKitFactory

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("544fcc81-0d9f-43db-8e6c-0966ab04f5fc")
        makeSharedPreference()
    }
    fun makeSharedPreference(): PreferencesManager {
        val sharedPreferences = getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
        return PreferencesManager.Base(sharedPreferences)
    }

    companion object{
        private const val APP_PREF = "APP_PREF"
    }
}
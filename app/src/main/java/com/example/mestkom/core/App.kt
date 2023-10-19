package com.example.mestkom.core

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("544fcc81-0d9f-43db-8e6c-0966ab04f5fc")
    }
}
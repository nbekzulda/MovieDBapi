package com.example.moviedbapi.main

import android.app.Application
import com.example.moviedbapi.main.DI.appModule
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}
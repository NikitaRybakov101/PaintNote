package com.example.foodnote.app

import android.app.Application
import com.example.foodnote.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    applicationModule,
                    noteBookModule,
                )
            )
        }
    }
}
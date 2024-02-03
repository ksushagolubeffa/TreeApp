package com.example.treeapp

import android.app.Application
import com.example.treeapp.di.databaseModule
import com.example.treeapp.di.nodesModule
import com.example.treeapp.presentation.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,
                databaseModule,
                nodesModule,
            )
        }
    }
}
package com.example.task1

import android.app.Application
import com.example.task1.di.editModule
import com.example.task1.di.mainModule
import com.example.task1.di.runModule
import com.example.task1.di.settingsModule
import com.example.task1.di.userModule
import com.example.task1.di.watchModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(watchModule, userModule, settingsModule, runModule, mainModule, editModule)
        }
    }
}
package com.example.yelp

import android.app.Application
import com.example.yelp.presentation.extensions.di.diModule
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.core.context.startKoin

class App: Application() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        setUpKoin()
        //setUpStetho()
        Stetho.initializeWithDefaults(this)
    }

    private fun setUpStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    @ExperimentalCoroutinesApi
    private fun setUpKoin() {
        startKoin {
            androidContext(this@App)
            androidLogger(Level.NONE)
            modules(listOf(diModule))
        }
    }
}
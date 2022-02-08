package com.example.films.presentation

import android.annotation.SuppressLint
import android.app.Application
import com.example.films.common.SharedPref
import dagger.hilt.android.HiltAndroidApp
val prefs: SharedPref by lazy {
    BaseApplication.prefs!!
}

@HiltAndroidApp
class BaseApplication: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var prefs: SharedPref? = null
        lateinit var instance: BaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = SharedPref(applicationContext)
    }
}
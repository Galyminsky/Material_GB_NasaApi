package me.proton.jobforandroid.material_gb_nasaapi.ui

import android.app.Application
import me.proton.jobforandroid.material_gb_nasaapi.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin



class App : Application() {
    companion object {
        lateinit var appInstance: App
        const val APP_PREF_NAME = "AppSettings"
        const val PREF_THEME_KEY = "AppTheme"
        const val DATE_TIME_FORMAT_PATTERN = "dd.MM.yyyy HH:mm"
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}
package me.proton.jobforandroid.material_gb_nasaapi.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.proton.jobforandroid.material_gb_nasaapi.R
import me.proton.jobforandroid.material_gb_nasaapi.ui.main.MainFragment


class MainActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(App.APP_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences?.let {
            val themeId = it.getInt(App.PREF_THEME_KEY, R.style.AppThemeLight)
            setTheme(themeId)
        }
        setContentView(R.layout.main_activity)
        savedInstanceState ?: let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
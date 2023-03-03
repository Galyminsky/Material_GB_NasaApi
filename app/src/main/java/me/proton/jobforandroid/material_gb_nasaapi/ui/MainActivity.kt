package me.proton.jobforandroid.material_gb_nasaapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.proton.jobforandroid.material_gb_nasaapi.R
import me.proton.jobforandroid.material_gb_nasaapi.ui.main.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
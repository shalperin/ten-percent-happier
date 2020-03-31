package com.blauhaus.android.redwood.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        daynightfab.setOnClickListener{
            toggleDayNight()
        }

        meditationfab.setOnClickListener{
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_meditationDemoFragment)
        }

        todofab.setOnClickListener{
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_todoMvvmFragment)
        }
    }


    private fun toggleDayNight() {
        when(AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}
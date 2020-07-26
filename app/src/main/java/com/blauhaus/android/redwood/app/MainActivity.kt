package com.blauhaus.android.redwood.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        daynightfab.setOnClickListener{
//            toggleDayNight()
//        }

        go_home.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_homeFragment)
        }


        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener(destinationChangedListener)
    }


    private fun toggleDayNight() {
        when(AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }


    private val destinationChangedListener = NavController.OnDestinationChangedListener{ controller, destination, arguments ->
        when(destination.id) {
            R.id.homeFragment -> {
                my_toolbar.visibility = View.GONE
            }
            R.id.loginFragment -> {
                my_toolbar.visibility = View.VISIBLE
                my_toolbar.destination_description.setText(R.string.login_button_text)
            }
            R.id.meditationDemoFragment -> {
                my_toolbar.visibility = View.VISIBLE
                my_toolbar.destination_description.setText(R.string.meditation_demo_description)
            }
            R.id.todoMvvmFragment -> {
                my_toolbar.visibility = View.VISIBLE
                my_toolbar.destination_description.setText(R.string.todo_demo_description)
            }
        }
    }
}
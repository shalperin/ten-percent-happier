package com.blauhaus.android.redwood.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.blauhaus.android.redwood.barchart.PROP_BAR_COLOR
import com.blauhaus.android.redwood.barchart.BarChartFragment
import com.blauhaus.android.redwood.barchart.BarChartViewModel
import com.blauhaus.android.redwood.barchart.barChartDemoData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()

        fab.setOnClickListener{
            toggleDayNight()
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

    private fun toggleLanguage():Nothing = TODO()

    private fun setUpNavigation() {
        // Create a listener for doing things like setting the AppBar title text.
        findNavController(R.id.nav_host_fragment)
            .addOnDestinationChangedListener{ controller, destination, arguments ->
                when (destination.id) {
                    R.id.indexFragment -> {
                        title = "Redwood"
                    }
                    R.id.meditationDemoFragment -> {
                        title = "Meditation Challenge"
                    }
                    else -> {
                        title = "Redwood"
                    }
                }
            }
    }

}

//  val lastFourWeeksViewModel by viewModel<LastFourWeeksViewModel>()
//
//

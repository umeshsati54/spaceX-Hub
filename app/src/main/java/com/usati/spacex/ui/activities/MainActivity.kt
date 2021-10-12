package com.usati.spacex.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.usati.spacex.R
import com.usati.spacex.SpacexRepository
import com.usati.spacex.db.SpacexDatabase
import com.usati.spacex.ui.fragments.RocketFragment
import com.usati.spacex.ui.fragments.SearchFragment
import com.usati.spacex.ui.viewmodels.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModelRocket: RocketViewModel
    lateinit var viewModelLaunches: LaunchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.background = null

        val spacexRepository = SpacexRepository(SpacexDatabase(this))
        val rocketViewModelProviderFactory =
            ViewModelProviderFactory.RocketViewModelProviderFactory(application, spacexRepository)
        val launchViewModelProviderFactory =
            ViewModelProviderFactory.LaunchViewModelProviderFactory(application, spacexRepository)
        viewModelRocket =
            ViewModelProvider(this, rocketViewModelProviderFactory).get(RocketViewModel::class.java)
        viewModelLaunches =
            ViewModelProvider(this, launchViewModelProviderFactory).get(LaunchViewModel::class.java)


        bottomNavigationView.setupWithNavController(spacexNavHostFragment.findNavController())

        search_fab.setOnClickListener {
            when {
                spacexNavHostFragment.childFragmentManager.fragments[0] is RocketFragment -> {
                    findNavController(R.id.spacexNavHostFragment)
                        .navigate(R.id.action_rocketFragment_to_searchFragment)
                }
                spacexNavHostFragment.childFragmentManager.fragments[0] is SearchFragment -> {

                }

                else -> {
                    findNavController(R.id.spacexNavHostFragment)
                        .navigate(R.id.action_launchesFragment_to_searchFragment)
                }
            }
        }
    }
}
package com.usati.spacex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.usati.spacex.R
import com.usati.spacex.RocketRepository
import com.usati.spacex.RocketViewModel
import com.usati.spacex.RocketViewModelProviderFactory
import com.usati.spacex.db.SpacexDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: RocketViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rocketRepository = RocketRepository(SpacexDatabase(this))
        val viewModelProviderFactory = RocketViewModelProviderFactory(application, rocketRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(RocketViewModel::class.java)

        bottomNavigationView.setupWithNavController(spacexNavHostFragment.findNavController())
    }
}
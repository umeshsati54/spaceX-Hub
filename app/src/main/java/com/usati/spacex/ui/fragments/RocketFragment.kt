package com.usati.spacex.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.usati.spacex.R
import com.usati.spacex.Resource
import com.usati.spacex.adapters.RocketAdapter
import com.usati.spacex.ui.viewmodels.RocketViewModel
import com.usati.spacex.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_launches.*
import kotlinx.android.synthetic.main.fragment_rocket.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.rocket_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class RocketFragment : Fragment(R.layout.fragment_rocket) {
    lateinit var viewModel: RocketViewModel
    lateinit var rocketAdapter: RocketAdapter
    lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModelRocket
        setupRecyclerView()
        //viewModel.getRocketsAPI()

        bottomSheetBehavior = BottomSheetBehavior.from(rocketDetailsBS)
        bottomSheetBehavior.peekHeight = 0

        rocketAdapter.setOnItemClickListener { rocket ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            Glide.with(this).load(rocket.flickr_images?.get(0)).into(ivRocketImageBS)
            tvRocketTitleBS.text = rocket.name
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("MMM dd, yyyy")
            val date: Date = inputFormat.parse(rocket.first_flight)
            val formattedDate: String = outputFormat.format(date)
            tvSubRocketTitleBS.text = formattedDate
            tvCostPerLaunch.text = "$" + rocket.cost_per_launch.toString()
            tvHeight.text = rocket.height.meters.toString() + " meters"
            tvDiameter.text = rocket.diameter.meters.toString() + " meters"
            tvMass.text = rocket.mass?.kg.toString() + " KG"
            tvRocketDetails.text = rocket.description

            ibWikiRocket.setOnClickListener {
                val wikiLink = rocket.wikipedia
                if (wikiLink != null) {
                    LaunchesFragment.openUrl(it.context, wikiLink)
                } else Toast.makeText(it.context, "No wiki page", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.rockets.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        tvErrorRocket.text = message
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                }
            }
        })

        viewModel.getRocketFromRoom().observe(viewLifecycleOwner, { rockets ->
            rocketAdapter.differ.submitList(rockets)
        })
    }

    private fun setupRecyclerView() {
        rocketAdapter = RocketAdapter()
        rvRockets.apply {
            adapter = rocketAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        rocketProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        rocketProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false

}
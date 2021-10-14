package com.usati.spacex.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.usati.spacex.R
import com.usati.spacex.Resource
import com.usati.spacex.adapters.LaunchAdapter
import com.usati.spacex.adapters.RocketAdapter
import com.usati.spacex.ui.activities.MainActivity
import com.usati.spacex.ui.viewmodels.LaunchViewModel
import kotlinx.android.synthetic.main.fragment_launches.*
import kotlinx.android.synthetic.main.fragment_rocket.*
import kotlinx.android.synthetic.main.rocket_item.*
import kotlinx.android.synthetic.main.rocket_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Rect

import android.view.MotionEvent
import androidx.core.widget.NestedScrollView
import android.content.ActivityNotFoundException
import android.content.Context

import android.net.Uri

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity


class LaunchesFragment : Fragment(R.layout.fragment_launches) {
    lateinit var viewModel: LaunchViewModel
    lateinit var launchAdapter: LaunchAdapter

    lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModelLaunches
        setupRecyclerView()
        //viewModel.getRocketsAPI()

        bottomSheetBehavior = BottomSheetBehavior.from(launchDetailsBS)
        bottomSheetBehavior.peekHeight = 0


        launchAdapter.setOnItemClickListener { launch ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            Glide.with(this).load(launch.links?.patch?.small)
                .placeholder(R.drawable.rocket_launch_outline).into(ivLaunchImageBS)
            tvTitleBS.text = launch.name
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm ")
            val date: Date = inputFormat.parse(launch.date_utc)
            val formattedDate: String = outputFormat.format(date)
            tvSubTitleBS.text = formattedDate + "IST"
            tvLaunchDetails.text = launch.details

            ibYoutube.setOnClickListener {
                val youtubeId = launch.links?.youtube_id
                if (youtubeId != null) {
                    watchYoutubeVideo(it.context, youtubeId)
                } else Toast.makeText(it.context, "No video available", Toast.LENGTH_SHORT).show()
            }

            ibWeb.setOnClickListener {
                val articleLink = launch.links?.article
                if (articleLink != null) {
                    openUrl(it.context, articleLink)
                } else Toast.makeText(it.context, "No article available", Toast.LENGTH_SHORT).show()
            }

            ibWikiLaunch.setOnClickListener {
                val wikiLink = launch.links?.wikipedia
                if (wikiLink != null) {
                    openUrl(it.context, wikiLink)
                } else Toast.makeText(it.context, "No wiki page", Toast.LENGTH_SHORT).show()
            }
        }

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if (i == pastRB.id) {
                viewModel.getPastLaunchesFromRoom()
                    .observe(viewLifecycleOwner, { launches ->
                        launchAdapter.differ.submitList(launches)
                    })
            } else if (i == upcomingRB.id) {
                viewModel.getUpcomingLaunchesFromRoom()
                    .observe(viewLifecycleOwner, { launches ->
                        launchAdapter.differ.submitList(launches)
                    })
            }
        }

        viewModel.launches.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        tvErrorLaunch.text = message
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

        viewModel.getPastLaunchesFromRoom().observe(viewLifecycleOwner, { launches ->
            launchAdapter.differ.submitList(launches)
        })

    }

    private fun setupRecyclerView() {
        launchAdapter = LaunchAdapter()
        rvLaunches.apply {
            adapter = launchAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {
        fun watchYoutubeVideo(context: Context, id: String) {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=$id")
            )
            try {
                context.startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                context.startActivity(webIntent)
            }
        }

        fun openUrl(context: Context, url: String) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
    }

    private fun hideProgressBar() {
        launchProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        launchProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false


}
package com.usati.spacex.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.usati.spacex.R
import com.usati.spacex.Resource
import com.usati.spacex.adapters.LaunchAdapter
import com.usati.spacex.ui.activities.MainActivity
import com.usati.spacex.ui.fragments.LaunchesFragment.Companion.openUrl
import com.usati.spacex.ui.fragments.LaunchesFragment.Companion.watchYoutubeVideo
import com.usati.spacex.ui.viewmodels.LaunchViewModel
import kotlinx.android.synthetic.main.fragment_launches.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.text.SimpleDateFormat
import java.util.*


class SearchFragment : Fragment(R.layout.fragment_search) {
    lateinit var viewModel: LaunchViewModel
    lateinit var launchAdapter: LaunchAdapter
    lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModelLaunches
        setupRecyclerView()

        bottomSheetBehavior = BottomSheetBehavior.from(searchDetailsBS)
        bottomSheetBehavior.peekHeight = 0

        launchAdapter.setOnItemClickListener { launch ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            Glide.with(this).load(launch.links?.patch?.small)
                .placeholder(R.drawable.rocket_launch_outline).into(ivSearchImageBS)
            tvSearchTitleBS.text = launch.name
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm ")
            val date: Date = inputFormat.parse(launch.date_utc)
            val formattedDate: String = outputFormat.format(date)
            tvSubSearchTitleBS.text = formattedDate + "IST"
            tvSearchDetails.text = launch.details

            ivYoutubeSearch.setOnClickListener {
                val youtubeId = launch.links?.youtube_id
                if (youtubeId != null) {
                    watchYoutubeVideo(it.context, youtubeId)
                } else Toast.makeText(it.context, "No video available", Toast.LENGTH_SHORT).show()
            }

            ivWebSearch.setOnClickListener {
                val articleLink = launch.links?.article
                if (articleLink != null) {
                    openUrl(it.context, articleLink)
                } else Toast.makeText(it.context, "No article available", Toast.LENGTH_SHORT).show()
            }

            ivWikiSearch.setOnClickListener {
                val wikiLink = launch.links?.wikipedia
                if (wikiLink != null) {
                    openUrl(it.context, wikiLink)
                } else Toast.makeText(it.context, "No wiki page", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.launches.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        tvErrorSearch.text = message
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

        etSearch.editText?.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty()) {
                searchForLaunches(text.toString())
            } else tvErrorSearch.text
        }
    }

    private fun searchForLaunches(searchText: String) {
        var searchText = searchText
        searchText = "%$searchText%"
        viewModel.searchLaunches(searchText).observe(viewLifecycleOwner, { launches ->
            launchAdapter.differ.submitList(launches)
        })


    }


    private fun setupRecyclerView() {
        launchAdapter = LaunchAdapter()
        rvSearchLaunches.apply {
            adapter = launchAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        searchProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        searchProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false


}
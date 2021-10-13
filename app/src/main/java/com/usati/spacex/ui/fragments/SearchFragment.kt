package com.usati.spacex.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.usati.spacex.R
import com.usati.spacex.adapters.LaunchAdapter
import com.usati.spacex.ui.activities.MainActivity
import com.usati.spacex.ui.viewmodels.LaunchViewModel
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment(R.layout.fragment_search){
    lateinit var viewModel: LaunchViewModel
    lateinit var launchAdapter: LaunchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModelLaunches
        setupRecyclerView()

        launchAdapter.setOnItemClickListener { launch ->

        }

        etSearch.editText?.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty()){
                searchForLaunches(text.toString())
            }
        }


//        viewModel.getUpcomingLaunchesFromRoom().observe(viewLifecycleOwner, Observer { launches ->
//            launchAdapter.differ.submitList(launches)
//        })
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


}
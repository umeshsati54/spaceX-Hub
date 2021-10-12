package com.usati.spacex.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.usati.spacex.R
import com.usati.spacex.Resource
import com.usati.spacex.RocketAdapter
import com.usati.spacex.RocketViewModel
import kotlinx.android.synthetic.main.fragment_rocket.*

class RocketFragment : Fragment(R.layout.fragment_rocket) {
    lateinit var viewModel: RocketViewModel
    lateinit var rocketAdapter: RocketAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        //viewModel.getRocketsAPI()

        viewModel.rockets.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    //Toast.makeText(context, response.message+response.data, Toast.LENGTH_SHORT).show()
                    response.data?.let { rocketResponse ->
                        //rocketAdapter.differ.submitList(rocketResponse)

                    }
                }

                is Resource.Error -> {

                    response.message?.let { message ->
                        Log.e("ROCKET TAG", message)
                        Toast.makeText(context, message+response.data, Toast.LENGTH_SHORT).show()

                    }
                }
                is Resource.Loading -> {

                }

            }
        })

        viewModel.getRocketFromRoom().observe(viewLifecycleOwner, Observer { rockets ->
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

}
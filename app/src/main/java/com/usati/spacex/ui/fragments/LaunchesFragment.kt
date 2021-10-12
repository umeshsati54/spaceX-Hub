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
import com.usati.spacex.R
import com.usati.spacex.Resource
import com.usati.spacex.adapters.LaunchAdapter
import com.usati.spacex.adapters.RocketAdapter
import com.usati.spacex.ui.activities.MainActivity
import com.usati.spacex.ui.viewmodels.LaunchViewModel
import kotlinx.android.synthetic.main.fragment_launches.*
import kotlinx.android.synthetic.main.fragment_rocket.*

class LaunchesFragment : Fragment(R.layout.fragment_launches) {
    lateinit var viewModel: LaunchViewModel
    lateinit var launchAdapter: LaunchAdapter

    lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModelLaunches
        setupRecyclerView()
        //viewModel.getRocketsAPI()

        val options = resources.getStringArray(R.array.Filter)
        arrayAdapter = ArrayAdapter(context?.applicationContext!!, R.layout.launches_dropdown_item, options)
        autoCompleteTextView.setAdapter(arrayAdapter)


        autoCompleteTextView.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //displayStatesForSelectedCountry(s.toString())
                //viewModel.country.value = s.toString()
                if (s.toString() == "Upcoming"){
                    viewModel.getUpcomingLaunchesFromRoom().observe(viewLifecycleOwner, Observer { rockets ->
                        launchAdapter.differ.submitList(rockets)
                    })
                }
                else{
                    viewModel.getPastLaunchesFromRoom().observe(viewLifecycleOwner, Observer { rockets ->
                        launchAdapter.differ.submitList(rockets)
                    })
                }
            }
        })
        viewModel.launches.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    //Toast.makeText(context, response.message+response.data, Toast.LENGTH_SHORT).show()
                    response.data?.let {
                        //rocketAdapter.differ.submitList(rocketResponse)

                    }
                }

                is Resource.Error -> {

                    response.message?.let { message ->
                        Log.e("ROCKET TAG", message)
                        Toast.makeText(context, message + response.data, Toast.LENGTH_SHORT).show()

                    }
                }
                is Resource.Loading -> {

                }

            }
        })

        viewModel.getUpcomingLaunchesFromRoom().observe(viewLifecycleOwner, Observer { launches ->
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

    private fun AutoCompleteTextView.showDropdownMenu(adapter: ArrayAdapter<String>){
        if(!TextUtils.isEmpty(this.text.toString())){
            adapter.filter.filter(null)
        }
    }

    override fun onResume() {
        super.onResume()
        autoCompleteTextView.showDropdownMenu(arrayAdapter)
    }
}
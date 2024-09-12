package com.example.vanalaeropuerto.fragments.empresa

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.adapters.empresa.TripsAdapter
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.empresa.ConfirmedTripsViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.PendingTripsViewModel
import com.google.android.material.snackbar.Snackbar

class ConfirmedTripsFragment : Fragment() {

    private lateinit var v : View

    private lateinit var progressBar : ProgressBar
    private lateinit var recyclerConfirmedTrips : RecyclerView
    private lateinit var tripsAdapter : TripsAdapter

    private lateinit var viewModel: ConfirmedTripsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_confirmed_trips, container, false)

        recyclerConfirmedTrips = v.findViewById(R.id.recycler_confirmed_trips)
        progressBar = v.findViewById(R.id.progressBarLoading)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ConfirmedTripsViewModel::class.java)

        recyclerConfirmedTrips.layoutManager = LinearLayoutManager(context)
        tripsAdapter = TripsAdapter(mutableListOf())
        recyclerConfirmedTrips.adapter = tripsAdapter

        viewModel.getConfirmedTrips()

        viewModel._tripsList.observe(viewLifecycleOwner, Observer { _tripsList ->
            if (_tripsList != null) {
                tripsAdapter.submitList(_tripsList)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    this.showLoading()
                }
                is ViewState.Failure -> {
                    this.showError()
                }
                is ViewState.Idle -> {
                    this.hideLoading()
                }
                is ViewState.Empty ->{
                    this.showEmpty()
                } else ->{
                this.showError()
            }
            }
        })

    }

    private fun showEmpty() {
        progressBar.visibility = View.GONE
        recyclerConfirmedTrips.visibility = View.GONE
    }
    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerConfirmedTrips.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        recyclerConfirmedTrips.visibility = View.VISIBLE
    }

    private fun showError() {
        recyclerConfirmedTrips.visibility = View.GONE
        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.ha_ocurrido_un_error), Snackbar.LENGTH_SHORT).show()
    }


}
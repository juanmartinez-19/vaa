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
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.viewmodels.empresa.PendingTripsViewModel
import com.google.android.material.snackbar.Snackbar

class PendingTripsFragment : Fragment() {

    private lateinit var v : View

    private lateinit var progressBar : ProgressBar
    private lateinit var recyclerPendingTrips : RecyclerView
    private lateinit var tripsAdapter : TripsAdapter

    private var tripRequesterList = mutableListOf<TripRequester>()
    private var requesterMap = mutableMapOf<String?, Requester>()

    private lateinit var viewModel: PendingTripsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pending_trips, container, false)

        recyclerPendingTrips = v.findViewById(R.id.rvTrips)
        progressBar = v.findViewById(R.id.progressBarLoading)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PendingTripsViewModel::class.java)

        recyclerPendingTrips.layoutManager = LinearLayoutManager(context)
        tripsAdapter = TripsAdapter(mutableListOf())
        recyclerPendingTrips.adapter = tripsAdapter

        viewModel.getPendingTrips()

        viewModel._tripsList.observe(viewLifecycleOwner, Observer { _tripsList ->
            if (_tripsList != null) {
                for (trip in _tripsList) {
                    val requesterId = trip.getRequesterId()
                    if (requesterId != null) {
                        viewModel.getRequester(requesterId)
                    }
                }
            }
        })

        viewModel.requestersMap.observe(viewLifecycleOwner, Observer { itrequestersMap ->
            if (itrequestersMap != null) {
                this.requesterMap = itrequestersMap
                updateTripRequester()
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

    private fun updateTripRequester() {
        tripRequesterList.clear()
        for (trip in viewModel._tripsList.value ?: emptyList()) {
            val requesterId = trip.getRequesterId()
            val requester = requesterId?.let { requesterMap[it] }
            if (requester != null) {
                tripRequesterList.add(TripRequester(trip, requester))
            }
        }

        tripsAdapter.submitList(tripRequesterList)
    }


    private fun showEmpty() {
        progressBar.visibility = View.GONE
        recyclerPendingTrips.visibility = View.GONE
        Snackbar.make(v,"La lista está vacía", Snackbar.LENGTH_SHORT).show()
    }
    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerPendingTrips.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        recyclerPendingTrips.visibility = View.VISIBLE
    }

    private fun showError() {
        recyclerPendingTrips.visibility = View.GONE
        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.ha_ocurrido_un_error), Snackbar.LENGTH_SHORT).show()
    }




}
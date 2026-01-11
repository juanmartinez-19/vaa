package com.example.vanalaeropuerto.fragments.empresa

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.adapters.empresa.TripsAdapter
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.fragments.user.VehiculosFragmentDirections
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.PendingTripsViewModel
import com.google.android.material.snackbar.Snackbar

class PendingTripsFragment : Fragment() {

    private lateinit var v: View
    private lateinit var viewModel: PendingTripsViewModel
    private lateinit var adapter: TripsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_pending_trips, container, false)
        recycler = v.findViewById(R.id.rvTrips)
        progressBar = v.findViewById(R.id.progressBarLoading)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PendingTripsViewModel::class.java]

        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = TripsAdapter(mutableListOf()) { position ->
            val item = adapter.getSelectedItem(position)

            findNavController().navigate(
                R.id.pendingTripDetailFragment,
                Bundle().apply {
                    putString("tripId", item.getTrip().getTripId())
                    putString("requesterId", item.getRequester().getRequesterId())
                }
            )
        }

        recycler.adapter = adapter

        viewModel.tripItems.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            when (it) {
                ViewState.Loading -> showLoading()
                ViewState.Idle -> hideLoading()
                ViewState.Empty -> showEmpty()
                else -> showError()
            }
        }

        viewModel.getPendingTrips()
    }

    override fun onStart() {
        super.onStart()
        viewModel.startListening()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopListening()
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recycler.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        recycler.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        hideLoading()
        Snackbar.make(v, "La lista está vacía", Snackbar.LENGTH_SHORT).show()
    }

    private fun showError() {
        hideLoading()
        Snackbar.make(v, getString(R.string.ha_ocurrido_un_error), Snackbar.LENGTH_SHORT).show()
    }
}

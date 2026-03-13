package com.example.vanalaeropuerto.fragments.empresa

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.adapters.empresa.TripsAdapter
import com.example.vanalaeropuerto.data.TripsUiState
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.empresa.ConfirmedTripsViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.PendingTripsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PendingTripsFragment : Fragment() {

    private lateinit var v: View
    private val viewModel: PendingTripsViewModel by viewModels()
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

        viewModel.uiState.observe(viewLifecycleOwner) { state ->

            when(state) {

                is TripsUiState.Loading -> {
                    showLoading()
                }

                is TripsUiState.Success -> {
                    hideLoading()
                    adapter.submitList(state.trips)
                }

                is TripsUiState.Empty -> {
                    showEmpty()
                }

                is TripsUiState.Error -> {
                    showError()
                }
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

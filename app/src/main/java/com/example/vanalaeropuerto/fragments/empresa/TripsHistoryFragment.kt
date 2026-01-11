package com.example.vanalaeropuerto.fragments.empresa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.adapters.empresa.TripsAdapter
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.TripsHistoryViewModel
import com.google.android.material.snackbar.Snackbar

class TripsHistoryFragment : Fragment() {

    private lateinit var v: View
    private lateinit var viewModel: TripsHistoryViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerTripHistory: RecyclerView
    private lateinit var tripsAdapter: TripsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_trips_history, container, false)

        recyclerTripHistory = v.findViewById(R.id.recycler_trip_history)
        progressBar = v.findViewById(R.id.progressBarLoading)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[TripsHistoryViewModel::class.java]

        val sessionViewModel =
            ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        sessionViewModel.currentRequester.observe(viewLifecycleOwner) { session ->
            if (session == null || session.getRequesterRole() != Roles.ADMIN) {
                requireActivity().finish()
            }
        }

        recyclerTripHistory.layoutManager = LinearLayoutManager(requireContext())

        tripsAdapter = TripsAdapter(mutableListOf()) { position ->
            val item = tripsAdapter.getSelectedItem(position)

            findNavController().navigate(
                R.id.pendingTripDetailFragment,
                Bundle().apply {
                    putString("tripId", item.getTrip().getTripId())
                    putString("requesterId", item.getRequester().getRequesterId())
                }
            )
        }

        recyclerTripHistory.adapter = tripsAdapter

        // 📌 Observers LIMPIOS
        viewModel.tripItems.observe(viewLifecycleOwner) {
            tripsAdapter.submitList(it.toMutableList())
        }

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ViewState.Loading -> showLoading()
                ViewState.Idle -> hideLoading()
                ViewState.Empty -> showEmpty()
                else -> showError()
            }
        }

        viewModel.getTripHistory()
    }

    private fun showEmpty() {
        progressBar.visibility = View.GONE
        recyclerTripHistory.visibility = View.GONE
        Snackbar.make(v, "La lista está vacía", Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerTripHistory.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        recyclerTripHistory.visibility = View.VISIBLE
    }

    private fun showError() {
        recyclerTripHistory.visibility = View.GONE
        progressBar.visibility = View.GONE
        Snackbar.make(
            v,
            getString(R.string.ha_ocurrido_un_error),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}

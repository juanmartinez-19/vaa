package com.example.vanalaeropuerto.fragments.empresa

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.adapters.empresa.TripsAdapter
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.ConfirmedTripsViewModel
import com.google.android.material.snackbar.Snackbar

class ConfirmedTripsFragment : Fragment() {

    private lateinit var v: View

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerConfirmedTrips: RecyclerView
    private lateinit var tripsAdapter: TripsAdapter

    private var tripRequesterList = mutableListOf<TripRequester>()
    private var requesterMap = mutableMapOf<String?, Requester>()

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

        val sessionViewModel =
            ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        sessionViewModel.currentRequester.observe(viewLifecycleOwner) { session ->
            if (session == null || session.getRequesterRole() != Roles.ADMIN) {
                requireActivity().finish()
            }
        }

        recyclerConfirmedTrips.layoutManager = LinearLayoutManager(context)
        tripsAdapter = TripsAdapter(mutableListOf()) {
            val tripRequester = tripsAdapter.getSelectedItem(it)
            val navController = findNavController()

            // Crear un Bundle para pasar el tripRequester
            val bundle = Bundle().apply {
                putString("tripId", tripRequester.getTrip()?.getTripId())
                putString("requesterId", tripRequester.getRequester()?.getRequesterId())
            }

            // Navegar al fragmento y pasar el Bundle
            navController.navigate(R.id.pendingTripDetailFragment, bundle)
        }
        recyclerConfirmedTrips.adapter = tripsAdapter

        viewModel.getConfirmedTrips()

        viewModel.tripsFull.observe(viewLifecycleOwner) { trips ->
            tripsAdapter.submitList(trips.toMutableList())
        }


        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                ViewState.Loading -> showLoading()
                ViewState.Idle -> hideLoading()
                ViewState.Empty -> showEmpty()
                ViewState.Failure -> showError()
                else -> {showError()}
            }
        }

    }


    private fun showEmpty() {
        progressBar.visibility = View.GONE
        recyclerConfirmedTrips.visibility = View.GONE
        Snackbar.make(v,"La lista está vacía", Snackbar.LENGTH_SHORT).show()
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
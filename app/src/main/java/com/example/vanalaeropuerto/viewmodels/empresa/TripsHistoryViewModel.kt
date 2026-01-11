package com.example.vanalaeropuerto.viewmodels.empresa

import TripItemUI
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.example.vanalaeropuerto.data.repositories.TripsRepository
import com.example.vanalaeropuerto.data.repositories.empresa.DriversRepository
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.Trip
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class TripsHistoryViewModel : ViewModel() {

    private val tripsRepository = TripsRepository()
    private val requesterRepository = RequesterRepository()

    private val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState: LiveData<ViewState> = _viewState

    private val _tripItems = MutableLiveData<List<TripItemUI>>()
    val tripItems: LiveData<List<TripItemUI>> = _tripItems
    private val driversRepository = DriversRepository()

    private var listener: ListenerRegistration? = null

    // 🟢 Carga inicial
    fun getTripHistory() {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = tripsRepository.getTripHistory()) {
                is MyResult.Success -> {
                    if (result.data.isEmpty()) {
                        _viewState.value = ViewState.Empty
                    } else {
                        buildTripItems(result.data)
                    }
                }
                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                    Log.e("TripsHistoryVM", "Error", result.exception)
                }
            }
        }
    }
    private suspend fun buildTripItems(trips: List<Trip>) {
        val fullList = mutableListOf<TripItemUI>()

        for (trip in trips) {

            val requesterId = trip.getRequesterId()
            val driverId = trip.getDriverId()

            if (requesterId.isNullOrEmpty() || driverId.isNullOrEmpty()) continue

            val requesterResult = requesterRepository.getRequester(requesterId)
            val driverResult = driversRepository.getDriverById(driverId)

            if (
                requesterResult is MyResult.Success &&
                driverResult is MyResult.Success
            ) {
                fullList.add(
                    TripItemUI(
                        trip = trip,
                        requester = requesterResult.data!!,
                        driver = driverResult.data
                    )
                )
            }
        }
        _tripItems.value = fullList
        _viewState.value = ViewState.Idle
    }
    /*
    private suspend fun buildTripItems(trips: List<Trip>) {
        val requesterCache = mutableMapOf<String, Requester>()
        val items = mutableListOf<TripItemUI>()

        for (trip in trips) {
            val requesterId = trip.getRequesterId() ?: continue

            val requester = requesterCache[requesterId]
                ?: when (val result = requesterRepository.getRequester(requesterId)) {
                    is MyResult.Success -> {
                        result.data?.also {
                            requesterCache[requesterId] = it
                        }
                    }
                    else -> null
                }

            if (requester != null) {
                items.add(
                    TripItemUI(
                        trip = trip,
                        requester = requester,
                        driver = driverResult.data
                    )
                )
            }
        }

        _tripItems.value = items
        _viewState.value = ViewState.Idle
    }

     */
}

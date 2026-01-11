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
import com.example.vanalaeropuerto.entities.Trip
import kotlinx.coroutines.launch

class ConfirmedTripsViewModel : ViewModel() {

    private val tripsRepository = TripsRepository()
    private val requesterRepository = RequesterRepository()
    private val driversRepository = DriversRepository()

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val _tripsFull = MutableLiveData<List<TripItemUI>>()
    val tripsFull: LiveData<List<TripItemUI>> = _tripsFull

    init {
        _viewState.value = ViewState.Idle
    }

    fun getConfirmedTrips() {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = tripsRepository.getConfirmedTrips()) {
                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                }
                is MyResult.Success -> {
                    if (result.data.isEmpty()) {
                        _viewState.value = ViewState.Empty
                        return@launch
                    } else {
                        buildTripItems(result.data)
                    }
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
        _tripsFull.value = fullList
        _viewState.value = ViewState.Idle
    }

    /*

    fun getRequester(requesterId: String?) {
        if (requesterId.isNullOrEmpty()) return

        viewModelScope.launch {
            _viewState.value = ViewState.Loading
            when (val result = getRequesterUseCase.getRequester(requesterId)) {
                is MyResult.Success -> {
                    val currentMap = _requestersMap.value ?: mutableMapOf()
                    // Asegúrate de que requesterId no sea null antes de usarlo como clave
                    requesterId.let {
                        currentMap[it] = result.data ?: return@launch
                        _requestersMap.value = currentMap
                    }
                    _viewState.value = ViewState.Idle
                }
                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", "Failure: ${result.exception}")
                }
            }
        }
    }
    */

}
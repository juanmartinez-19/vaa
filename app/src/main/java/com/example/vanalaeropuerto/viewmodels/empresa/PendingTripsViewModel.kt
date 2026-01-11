package com.example.vanalaeropuerto.viewmodels.empresa

import TripItemUI
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.example.vanalaeropuerto.data.repositories.TripsRepository
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.Trip
import kotlinx.coroutines.launch

class PendingTripsViewModel : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState: LiveData<ViewState> = _viewState

    private val _tripItems = MutableLiveData<List<TripItemUI>>()
    val tripItems: LiveData<List<TripItemUI>> = _tripItems

    private val tripsRepo = TripsRepository()
    private val requesterRepo = RequesterRepository()

    private val requesterMap = mutableMapOf<String, Requester>()
    private var rawTrips: List<Trip> = emptyList()

    fun getPendingTrips() {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = tripsRepo.getPendingTrips()) {
                is MyResult.Success -> {
                    rawTrips = result.data
                    loadRequesters()
                }
                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                }
            }
        }
    }

    private fun loadRequesters() {
        viewModelScope.launch {
            rawTrips.forEach { trip ->
                val id = trip.getRequesterId() ?: return@forEach
                if (!requesterMap.containsKey(id)) {
                    when (val res = requesterRepo.getRequester(id)) {
                        is MyResult.Success -> {
                            res.data?.let { requesterMap[id] = it }
                        }
                        else -> {}
                    }
                }
            }
            buildUIItems()
        }
    }

    private fun buildUIItems() {
        _tripItems.value = rawTrips.mapNotNull { trip ->
            val requester = requesterMap[trip.getRequesterId()]
            requester?.let {
                TripItemUI(
                    trip = trip,
                    requester = it,
                    driver = null
                )
            }
        }

        _viewState.value =
            if (_tripItems.value.isNullOrEmpty()) ViewState.Empty
            else ViewState.Idle
    }

    fun startListening() {
        tripsRepo.listenPendingTrips(
            onSuccess = { trips ->
                rawTrips = trips
                loadRequesters()
            },
            onError = {
                _viewState.value = ViewState.Failure
            }
        )
    }

    fun stopListening() {
        tripsRepo.clearListener()
    }

    override fun onCleared() {
        stopListening()
        super.onCleared()
    }
}

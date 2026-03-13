package com.example.vanalaeropuerto.viewmodels.empresa

import TripItemUI
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.TripsUiState
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.example.vanalaeropuerto.data.repositories.TripsRepository
import com.example.vanalaeropuerto.data.repositories.empresa.DriversRepository
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingTripsViewModel @Inject constructor(
    private val tripsRepository: TripsRepository,
    private val requesterRepository: RequesterRepository
)  : ViewModel() {

    private val _uiState = MutableLiveData<TripsUiState>()
    val uiState: LiveData<TripsUiState> = _uiState

    private val requesterMap = mutableMapOf<String, Requester>()
    private var rawTrips: List<Trip> = emptyList()

    fun getPendingTrips() {
        _uiState.value = TripsUiState.Loading

        viewModelScope.launch {
            when (val result = tripsRepository.getPendingTrips()) {
                is MyResult.Success -> {
                    rawTrips = result.data
                    loadRequesters()
                }

                is MyResult.Failure -> {
                    _uiState.value = TripsUiState.Error("No se pudieron cargar los viajes")
                }
            }
        }
    }

    private fun loadRequesters() {
        viewModelScope.launch {
            rawTrips.forEach { trip ->
                val id = trip.getRequesterId() ?: return@forEach
                if (!requesterMap.containsKey(id)) {
                    when (val res = requesterRepository.getRequester(id)) {
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

        val tripItems = rawTrips.mapNotNull { trip ->
            val requester = requesterMap[trip.getRequesterId()]
            requester?.let {
                TripItemUI(
                    trip = trip,
                    requester = it,
                    driver = null
                )
            }
        }

        _uiState.value =
            if (tripItems.isEmpty()) {
                TripsUiState.Empty
            } else {
                TripsUiState.Success(tripItems)
            }
    }

    fun startListening() {
        tripsRepository.listenPendingTrips(
            onSuccess = { trips ->
                rawTrips = trips
                loadRequesters()
            },
            onError = {
                _uiState.value = TripsUiState.Error("No se pudieron cargar los viajes")
            }
        )
    }

    fun stopListening() {
        tripsRepository.clearListener()
    }

    override fun onCleared() {
        stopListening()
        super.onCleared()
    }
}

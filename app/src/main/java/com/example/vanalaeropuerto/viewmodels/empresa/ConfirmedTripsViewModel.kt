package com.example.vanalaeropuerto.viewmodels.empresa

import TripItemUI
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.TripsUiState
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.example.vanalaeropuerto.data.repositories.TripsRepository
import com.example.vanalaeropuerto.data.repositories.empresa.DriversRepository
import com.example.vanalaeropuerto.entities.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmedTripsViewModel @Inject constructor(
    private val tripsRepository: TripsRepository,
    private val requesterRepository: RequesterRepository,
    private val driversRepository: DriversRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<TripsUiState>()
    val uiState: LiveData<TripsUiState> = _uiState

    fun getConfirmedTrips() {
        _uiState.value = TripsUiState.Loading

        viewModelScope.launch {
            when (val result = tripsRepository.getConfirmedTrips()) {
                is MyResult.Failure -> {
                    _uiState.value = TripsUiState.Error("No se pudieron cargar los viajes")
                }

                is MyResult.Success -> {
                    if (result.data.isEmpty()) {
                        _uiState.value = TripsUiState.Empty
                        return@launch
                    } else {
                        buildTripItems(result.data)
                    }
                }
            }
        }
    }
    private suspend fun buildTripItems(trips: List<Trip>) {

        val tripItems = coroutineScope {

            trips.mapNotNull { trip ->

                val requesterId = trip.getRequesterId()
                val driverId = trip.getDriverId()

                if (requesterId.isNullOrEmpty() || driverId.isNullOrEmpty()) {
                    return@mapNotNull null
                }

                async {

                    val requesterDeferred = async {
                        requesterRepository.getRequester(requesterId)
                    }

                    val driverDeferred = async {
                        driversRepository.getDriverById(driverId)
                    }

                    val requesterResult = requesterDeferred.await()
                    val driverResult = driverDeferred.await()

                    if (
                        requesterResult is MyResult.Success &&
                        driverResult is MyResult.Success
                    ) {

                        TripItemUI(
                            trip = trip,
                            requester = requesterResult.data!!,
                            driver = driverResult.data
                        )

                    } else {
                        null
                    }

                }

            }.awaitAll().filterNotNull()

        }

        _uiState.value = TripsUiState.Success(tripItems)
    }

}
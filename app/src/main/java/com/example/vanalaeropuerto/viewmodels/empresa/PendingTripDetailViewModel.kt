package com.example.vanalaeropuerto.viewmodels.empresa

import android.util.Log
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingTripDetailViewModel  @Inject constructor(
    private val tripsRepository: TripsRepository,
    private val requesterRepository: RequesterRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    private val _trip = MutableLiveData<Trip?>()
    val trip: LiveData<Trip?> get() = _trip

    private val _requester = MutableLiveData<Requester?>()
    val requester: LiveData<Requester?> get() = _requester

    fun cancelTrip(pendingTripId: String) {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = tripsRepository.cancelTrip(pendingTripId)) {
                is MyResult.Success -> {
                    _trip.value = result.data
                    _viewState.value = ViewState.Idle
                }

                is MyResult.Failure -> {
                    _trip.value = null
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }
        }
    }

    fun getTrip(tripId: String) {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = tripsRepository.getTrip(tripId)) {
                is MyResult.Success -> {
                    _trip.value = result.data
                    _viewState.value = ViewState.Idle
                }

                is MyResult.Failure -> {
                    _trip.value = null
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }
        }
    }

    fun getRequester(requesterId: String) {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = requesterRepository.getRequester(requesterId)) {
                is MyResult.Success -> {
                    _requester.value = result.data
                    _viewState.value = ViewState.Idle
                }

                is MyResult.Failure -> {
                    _requester.value = null
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }
        }
    }
}
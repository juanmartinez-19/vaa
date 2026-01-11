package com.example.vanalaeropuerto.viewmodels.empresa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.repositories.empresa.DriversRepository
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.example.vanalaeropuerto.data.repositories.TripsRepository
import com.example.vanalaeropuerto.entities.Driver
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.Trip
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TripSummaryViewModel : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    val getTripsUseCase : TripsRepository = TripsRepository()

    private val _trip = MutableLiveData<Trip?>()
    val trip: LiveData<Trip?> get() = _trip

    val getRequesterUseCase : RequesterRepository = RequesterRepository()

    private val _requester = MutableLiveData<Requester?>()
    val requester: LiveData<Requester?> get() = _requester

    val getDriverUseCase : DriversRepository = DriversRepository()

    private val _driver = MutableLiveData<Driver?>()
    val driver: LiveData<Driver?> get() = _driver

    fun confirmTrip(tripId : String) {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (getTripsUseCase.confirmTrip(tripId)) {
                is MyResult.Success -> {
                    _viewState.value = ViewState.Idle
                }

                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }
        }
    }

    fun getDriver (driverId : String) {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = getDriverUseCase.getDriverById(driverId)) {
                is MyResult.Success -> {
                    _driver.value = result.data
                    _viewState.value = ViewState.Idle
                }

                is MyResult.Failure -> {
                    _driver.value = null
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }
        }
    }
    fun getTrip (tripId : String) {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = getTripsUseCase.getTrip(tripId)) {
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

    fun getRequester(requesterId : String) {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = getRequesterUseCase.getRequester(requesterId)) {
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

    fun loadScreen(tripId: String, requesterId: String, driverId: String) {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {

            try {
                val requesterDeferred = async {
                    getRequesterUseCase.getRequester(requesterId)
                }
                val driverDeferred = async {
                    getDriverUseCase.getDriverById(driverId)
                }
                val tripDeferred = async {
                    getTripsUseCase.getTrip(tripId)
                }

                when (val result = requesterDeferred.await()) {
                    is MyResult.Success -> _requester.value = result.data
                    is MyResult.Failure -> throw result.exception
                }

                when (val result = driverDeferred.await()) {
                    is MyResult.Success -> _driver.value = result.data
                    is MyResult.Failure -> throw result.exception
                }

                when (val result = tripDeferred.await()) {
                    is MyResult.Success -> _trip.value = result.data
                    is MyResult.Failure -> throw result.exception
                }

                _viewState.value = ViewState.Idle

            } catch (e: Exception) {
                _requester.value = null
                _driver.value = null
                _trip.value = null
                _viewState.value = ViewState.Failure
            }
        }
    }



}
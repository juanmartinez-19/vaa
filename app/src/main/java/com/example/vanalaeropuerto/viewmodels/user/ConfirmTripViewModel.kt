package com.example.vanalaeropuerto.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.repositories.TripsRepository
import com.example.vanalaeropuerto.entities.Trip
import kotlinx.coroutines.launch

class ConfirmTripViewModel : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    val getTripsUseCase : TripsRepository = TripsRepository()

    init {
        _viewState.value = ViewState.Idle
    }
    fun addTrip(trip: Trip?) {

        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = trip?.let { getTripsUseCase.addTrip(it) }){
                is MyResult.Success -> {
                    _viewState.value = ViewState.Idle
                }
                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                }

                else -> {_viewState.value = ViewState.Failure}
            }
        }
    }

}
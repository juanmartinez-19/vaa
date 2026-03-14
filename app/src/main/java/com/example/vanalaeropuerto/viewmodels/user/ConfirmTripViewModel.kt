package com.example.vanalaeropuerto.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.repositories.TripsRepository
import com.example.vanalaeropuerto.entities.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmTripViewModel @Inject constructor(
    private val tripsRepository: TripsRepository
)  : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    init {
        _viewState.value = ViewState.Idle
    }

    fun addTrip(trip: Trip?) {

        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (trip?.let { tripsRepository.addTrip(it) }) {
                is MyResult.Success -> {
                    _viewState.value = ViewState.Idle
                }

                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                }

                else -> {
                    _viewState.value = ViewState.Failure
                }
            }
        }
    }

}
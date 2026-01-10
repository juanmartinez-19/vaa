package com.example.vanalaeropuerto.viewmodels.empresa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.RequesterRepository
import com.example.vanalaeropuerto.data.TripsRepository
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.Trip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class PendingTripsViewModel : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    private val _tripsList = MutableLiveData<List<Trip>>()
    val tripsList: LiveData<List<Trip>> = _tripsList
    val getTripsUseCase: TripsRepository = TripsRepository()

    val getRequesterUseCase : RequesterRepository = RequesterRepository()
    private val _requestersMap = MutableLiveData<MutableMap<String?, Requester>>()
    val requestersMap: LiveData<MutableMap<String?, Requester>> get() = _requestersMap

    private var listener: ListenerRegistration? = null

    init {
        _requestersMap.value = mutableMapOf()
    }

    init {
        _viewState.value = ViewState.Idle
    }
    fun getPendingTrips() {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = getTripsUseCase.getPendingTrips()){
                is MyResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        _tripsList.value = result.data.map { it }.toMutableList()
                        _viewState.value = ViewState.Idle
                    } else {
                        _viewState.value = ViewState.Empty
                    }
                }
                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                    Log.d("TEST1", _viewState.value.toString())
                }
            }

        }
    }

    fun startListening() {
        _viewState.value = ViewState.Loading

        getTripsUseCase.listenPendingTrips(
            onSuccess = { trips ->
                _tripsList.value = trips
                _viewState.value =
                    if (trips.isEmpty()) ViewState.Empty
                    else ViewState.Idle
            },
            onError = {
                _viewState.value = ViewState.Failure
            }
        )
    }

    fun stopListening() {
        getTripsUseCase.clearListener()
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }


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
                    Log.d("TEST2", "Failure: ${result.exception}")
                }
            }
        }
    }

}
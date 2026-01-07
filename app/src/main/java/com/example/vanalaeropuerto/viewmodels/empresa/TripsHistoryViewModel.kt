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
import kotlinx.coroutines.launch

class TripsHistoryViewModel : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    var _tripsList : MutableLiveData<MutableList<Trip>?> = MutableLiveData()
    val getTripsUseCase : TripsRepository = TripsRepository()

    val getRequesterUseCase : RequesterRepository = RequesterRepository()
    private val _requestersMap = MutableLiveData<MutableMap<String?, Requester>>()
    val requestersMap: LiveData<MutableMap<String?, Requester>> get() = _requestersMap

    init {
        _requestersMap.value = mutableMapOf()
    }

    init {
        _viewState.value = ViewState.Idle
    }
    fun getTripHistory() {
        viewModelScope.launch {
            _viewState.value = ViewState.Loading
            when (val result = getTripsUseCase.getTripHistory()){
                is MyResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        _tripsList.value = result.data
                        _viewState.value = ViewState.Idle

                    } else {
                        _viewState.value = ViewState.Empty
                    }
                }
                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }


        }
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
                    Log.d("TEST", "Failure: ${result.exception}")
                }
            }
        }
    }



}
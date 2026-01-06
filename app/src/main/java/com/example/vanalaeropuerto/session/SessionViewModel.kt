package com.example.vanalaeropuerto.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vanalaeropuerto.entities.Requester

class SessionViewModel : ViewModel() {

    private val _currentRequester = MutableLiveData<Requester?>()
    val currentRequester: LiveData<Requester?> = _currentRequester

    fun setRequester(requester: Requester) {
        _currentRequester.value = requester
    }

    fun clearSession() {
        _currentRequester.value = null
    }
}

package com.example.vanalaeropuerto.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.RequesterRepository
import com.example.vanalaeropuerto.entities.Requester
import kotlinx.coroutines.launch

class UserSharedViewModel : ViewModel() {

    private val repository = RequesterRepository()

    private val _requester = MutableLiveData<Requester?>()
    val requester: LiveData<Requester?> = _requester

    fun loadRequester(uid: String) {
        viewModelScope.launch {
            when (val result = repository.getRequester(uid)) {
                is MyResult.Success -> _requester.value = result.data
                is MyResult.Failure -> _requester.value = null
            }
        }
    }

}
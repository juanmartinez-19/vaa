package com.example.vanalaeropuerto.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.example.vanalaeropuerto.entities.Requester
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSharedViewModel @Inject constructor(
    private val requesterRepository: RequesterRepository
)  : ViewModel() {
    private val _requester = MutableLiveData<Requester?>()
    val requester: LiveData<Requester?> = _requester

    fun loadRequester(uid: String) {
        viewModelScope.launch {
            when (val result = requesterRepository.getRequester(uid)) {
                is MyResult.Success -> _requester.value = result.data
                is MyResult.Failure -> _requester.value = null
            }
        }
    }

}
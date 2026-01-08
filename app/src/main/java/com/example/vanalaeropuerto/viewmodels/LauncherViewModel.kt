package com.example.vanalaeropuerto.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.RequesterRepository
import com.example.vanalaeropuerto.entities.Requester
import kotlinx.coroutines.launch

class LauncherViewModel : ViewModel() {

    private val _launcherState = MutableLiveData<LauncherState>()
    val launcherState: LiveData<LauncherState> = _launcherState
    val getUsersUseCase : RequesterRepository = RequesterRepository()

    fun loadUser(uid: String) {
        viewModelScope.launch {
            when (val result = getUsersUseCase.getRequester(uid)) {
                is MyResult.Success -> {
                    val requester = result.data
                    if (requester != null) {
                        if (requester.getRequesterRole() == Roles.USER) {
                            _launcherState.postValue(LauncherState.User)
                        } else if (requester.getRequesterRole() == Roles.ADMIN) {
                            _launcherState.postValue(LauncherState.Admin)
                        } else {
                            _launcherState.postValue(LauncherState.Error)
                        }
                    } else {
                        _launcherState.postValue(LauncherState.Login)
                    }
                }
                is MyResult.Failure -> {
                    _launcherState.postValue(LauncherState.Error)
                }
            }
        }
    }

    sealed class LauncherState {
        object User : LauncherState()
        object Admin : LauncherState()
        object Login : LauncherState()
        object Error : LauncherState()
    }


}


package com.example.vanalaeropuerto.session

import com.example.vanalaeropuerto.entities.Requester

sealed class SessionState {
    object Loading : SessionState()
    data class LoggedIn(
        val uid: String
    ) : SessionState()
    object LoggedOut : SessionState()
}



package com.example.vanalaeropuerto.session

sealed class SessionState {
    object Loading : SessionState()
    data class LoggedIn(
        val uid: String
    ) : SessionState()
    object LoggedOut : SessionState()
}



package com.example.vanalaeropuerto.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val localSession = LocalSessionStorage(application)

    private val _state = MutableLiveData<SessionState>()
    val state: LiveData<SessionState> = _state

    init {
        _state.value = SessionState.Loading
        restoreSession()
    }

    private fun restoreSession() {
        val local = localSession.get()

        if (local != null && auth.currentUser != null) {
            _state.value = local
        } else {
            clearSession()
        }
    }
    fun onLoginSuccess(uid: String) {
        localSession.save(uid)
        _state.value = SessionState.LoggedIn(uid)
    }
    fun logout() {
        auth.signOut()
        clearSession()
    }
    private fun clearSession() {
        localSession.clear()
        _state.value = SessionState.LoggedOut
    }
}

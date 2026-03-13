package com.example.vanalaeropuerto.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val localSession = LocalSessionStorage(application)

    private val sessionManager = SessionManager(auth, localSession)

    val session = sessionManager.session

    init {
        sessionManager.restoreSession()
    }

    fun startSession(uid: String) {
        sessionManager.startSession(uid, UserRole.USER)
    }

    fun logout() {
        sessionManager.logout()
    }
}
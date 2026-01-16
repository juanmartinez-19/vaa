package com.example.vanalaeropuerto.session

import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.google.firebase.auth.FirebaseAuth

class SessionManager(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val repository: RequesterRepository = RequesterRepository()
) {

    suspend fun resolveSession(): SessionState {
        val firebaseUser = auth.currentUser ?: return SessionState.LoggedOut

        return when (val result = repository.getRequester(firebaseUser.uid)) {
            is MyResult.Success -> {
                SessionState.LoggedIn(firebaseUser.uid)
            }
            is MyResult.Failure -> {
                auth.signOut()
                SessionState.LoggedOut
            }
        }
    }

    fun logout() {
        auth.signOut()
    }
}

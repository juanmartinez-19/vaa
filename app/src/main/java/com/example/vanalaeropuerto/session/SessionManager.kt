package com.example.vanalaeropuerto.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class SessionManager(
    private val auth: FirebaseAuth,
    private val localSession: LocalSessionStorage
) {

    private val _session = MutableLiveData<UserSession?>()
    val session: LiveData<UserSession?> = _session

    fun restoreSession() {

        val localUid = localSession.getUid()
        val firebaseUser = auth.currentUser

        if (localUid != null && firebaseUser != null) {

            _session.value = UserSession(
                uid = localUid,
                role = UserRole.USER
            )

        } else {

            _session.value = null
        }
    }

    fun startSession(uid: String, role: UserRole) {

        localSession.save(uid)

        _session.value = UserSession(
            uid = uid,
            role = role
        )
    }

    fun logout() {
        auth.signOut()
        localSession.clear()
        _session.value = null
    }
}
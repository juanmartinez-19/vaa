package com.example.vanalaeropuerto.viewmodels.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class AuthCodeViewModel : ViewModel() {
/*
    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult> = _authResult

    sealed class AuthResult {
        object Success : AuthResult()
        data class Failure(val message: String) : AuthResult()
    }

    fun verifyCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // La autenticación fue exitosa
                    _authResult.postValue(AuthResult.Success)
                } else {
                    // Hubo un error al validar el código
                    _authResult.postValue(AuthResult.Failure(task.exception?.message ?: "Error desconocido"))
                }
            }
    }*/
}
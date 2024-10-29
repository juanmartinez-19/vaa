package com.example.vanalaeropuerto.viewmodels.login

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val errores = mutableListOf<String>()

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private var lastPhoneNumber: String? = null
    private var lastRequestTime: Long = 0
    private val requestCooldown: Long = 60000 // 1 minuto

    private val verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Verificación completada automáticamente
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Falla en la verificación
            _authState.postValue(AuthState.Failure(e.message ?: "Error de verificación"))
            _viewState.value = ViewState.Idle
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // Código enviado
            storedVerificationId = verificationId
            resendToken = token
            _authState.postValue(AuthState.CodeSent(storedVerificationId,"Código enviado correctamente."))
            _viewState.value = ViewState.Idle
        }
    }

    fun clearData() {
        _viewState.value = ViewState.Idle
        _authState.value = AuthState.Idle
    }

    fun phoneAuth(
        phoneNumber: String?,
        activity: FragmentActivity
    ) {
        _viewState.value = ViewState.Loading

        val currentTime = System.currentTimeMillis()

        this.validateData(phoneNumber, currentTime)

        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters(errores.first())
            Log.e("PhoneAuthError", "Errores: ${errores.joinToString(", ")}")
            return
        }

        if (!phoneNumber.isNullOrBlank()) {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(verificationCallbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

            lastPhoneNumber = phoneNumber
            lastRequestTime = currentTime
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.postValue(AuthState.Success)
            } else {
                _authState.postValue(AuthState.Failure("Error en el inicio de sesión"))
            }
            _viewState.value = ViewState.Idle
        }
    }

    private fun validateData(
        phoneNumber: String?,
        currentTime: Long
    ) {
        errores.clear()

        if (phoneNumber.isNullOrBlank()) {
            errores.add("El número de teléfono no puede estar vacío.")
        } else if (!phoneNumber.matches(Regex("^[+]?[0-9]{10,13}\$"))) {
            errores.add("Teléfono no válido. Debe contener entre 10 y 13 dígitos y puede incluir un '+' al inicio.")
        }

        if (phoneNumber == lastPhoneNumber && currentTime - lastRequestTime < requestCooldown) {
            val message = "Código ya solicitado recientemente. Espera 60 segundos antes de solicitar uno nuevo."
            errores.add(message)
            _authState.postValue(AuthState.CodeSent(storedVerificationId,message))
        }
    }

    fun verifyCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    sealed class AuthState {
        data object Success : AuthState()
        data class CodeSent(val verificationId: String, val message: String) : AuthState()
        data class Failure(val message: String) : AuthState()
        data object Idle : AuthState()
    }
}

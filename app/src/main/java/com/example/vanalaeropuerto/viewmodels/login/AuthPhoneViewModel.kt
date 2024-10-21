package com.example.vanalaeropuerto.viewmodels.login

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.login.UsersRepository
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit

class AuthPhoneViewModel : ViewModel() {
    private val errores = mutableListOf<String>()

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    val getUsersRepository : UsersRepository = UsersRepository()

    private var auth : FirebaseAuth = Firebase.auth

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private val verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Llamado cuando la verificación es completada automáticamente
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Llamado cuando la verificación falla
            _authState.postValue(AuthState.Failure(e.message ?: "Error de verificación"))
            _viewState.value = ViewState.Idle
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // Llamado cuando se envía el código de verificación
            storedVerificationId = verificationId
            resendToken = token
            _authState.postValue(AuthState.CodeSent(storedVerificationId))
            _viewState.value = ViewState.Idle
        }
    }

    fun phoneAuth(
        phoneNumber : String?,
        activity: FragmentActivity
    ) : MyResult<Unit> {

        _viewState.value = ViewState.Loading

        this.validateData(phoneNumber)

        if (errores.isNotEmpty()||phoneNumber.isNullOrBlank()) {
            _viewState.value = ViewState.InvalidParameters
            Log.e("PhoneAuthError", "Errores: ${errores.joinToString(", ")}")
        } else {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(activity) // Activity (for callback binding)
                    .setCallbacks(verificationCallbacks) // OnVerificationStateChangedCallbacks
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)

        }

        return MyResult.Success(Unit)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.postValue(AuthState.Success)
                    _viewState.value = ViewState.Idle
                } else {
                    _authState.postValue(AuthState.Failure("Error en el inicio de sesión"))
                    _viewState.value = ViewState.Idle
                }
            }
    }

    private fun validateData(
        phoneNumber: String?
    ) {
        val errores = mutableListOf<String>()

        // Validación de telefono
        if (phoneNumber.isNullOrBlank()) {
            errores.add("Telefono no puede estar vacía")
        } else if (!phoneNumber.matches(Regex("^[+]?[0-9]{10,13}\$"))) {
            errores.add("Teléfono no es válido. Debe contener entre 10 y 13 dígitos, y puede incluir un '+' al inicio.")
        }

    }

    sealed class AuthState {
        object Success : AuthState()
        data class CodeSent(val verificationId: String) : AuthState()
        data class Failure(val message: String) : AuthState()
    }

}
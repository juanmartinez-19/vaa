package com.example.vanalaeropuerto.viewmodels.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.login.RequesterRepository
import com.example.vanalaeropuerto.data.ViewState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    val getRequestersUseCase : RequesterRepository = RequesterRepository()
    private val errores = mutableListOf<String>()
    private val _viewState = MutableLiveData<ViewState>()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val viewState: LiveData<ViewState> get() = _viewState

    fun signIn (
        userName : String,
        userSurname : String,
        phoneNumber :String,
        userCuil : String
    ) {
        val uid = auth.currentUser?.uid

        viewModelScope.launch {
            when (getRequestersUseCase.signIn(userName,userSurname,phoneNumber,userCuil,uid)) {
                is MyResult.Success -> {
                    _viewState.value = ViewState.Confirmed
                }

                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                }

            }
        }
    }
    fun validateData(
        userName: String,
        userSurname: String,
        userCuil: String
    ) {
        _viewState.value = ViewState.Loading

        // Limpiar la lista de errores antes de validar
        errores.clear()

        // Validar nombre
        if (userName.isBlank()) {
            errores.add("El nombre no puede estar vacío.")
        }

        // Validar apellido
        if (userSurname.isBlank()) {
            errores.add("El apellido no puede estar vacío.")
        }

        // Validar CUIL (debería tener 11 dígitos)
        if (userCuil.isBlank()) {
            errores.add("El CUIL no puede estar vacío.")
        } else if (userCuil.length != 11) {
            errores.add("El CUIL debe tener exactamente 11 dígitos.")
        } else if (!userCuil.all { it.isDigit() }) {
            errores.add("El CUIL debe contener solo números.")
        }

        // Manejo de errores
        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters(errores.first())
            Log.e("ValidarDatos Registro", "Errores: ${errores.joinToString(", ")}")
        } else {
            _viewState.value = ViewState.ValidParameters
        }

    }




}
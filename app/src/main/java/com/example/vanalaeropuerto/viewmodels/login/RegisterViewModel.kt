package com.example.vanalaeropuerto.viewmodels.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vanalaeropuerto.data.ViewState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit

class RegisterViewModel : ViewModel() {

    private val errores = mutableListOf<String>()
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    private var auth : FirebaseAuth = Firebase.auth


    fun register(
        userName : String,
        userSurname : String,
        userPassword : String,
        userRepeatPassword : String,
        userCuil : String,
        phoneNumber : String
    ) {
        _viewState.value = ViewState.Loading

        this.validateData(userName,userSurname,userPassword,userRepeatPassword,userCuil)

        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters(errores.first())
        } else {
           // val userId = FirebaseAuth.getInstance().currentUser?.uid
            _viewState.value = ViewState.Confirmed
        }

    }
    private fun validateData(
        userName: String,
        userSurname: String,
        userPassword: String,
        userRepeatPassword: String,
        userCuil: String
    ) {
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

        // Validar contraseña
        if (userPassword.isBlank()) {
            errores.add("La contraseña no puede estar vacía.")
        } else {
            // Verificar la longitud
            if (userPassword.length < 8) {
                errores.add("La contraseña debe tener al menos 8 caracteres.")
            }
            // Iniciar un mensaje acumulativo para errores de la contraseña
                        var mensajeError = "La contraseña debe tener al menos"

            // Verificar si tiene al menos una mayúscula
                        if (!userPassword.any { it.isUpperCase() }) {
                            mensajeError += " una letra mayúscula,"
                        }

            // Verificar si tiene al menos un número
                        if (!userPassword.any { it.isDigit() }) {
                            mensajeError += " un número,"
                        }

            // Verificar si tiene al menos un carácter especial
                        if (!userPassword.any { it in "!@#$%^&*()-_=+[]{};:'\",.<>?/|\\\\" }) {
                            mensajeError += " un carácter especial."
                        }

            // Eliminar la última coma si se formó un mensaje
                        mensajeError = mensajeError.trimEnd(',')

            // Si no tiene mayúsculas, números o caracteres especiales, agregar error
            if (mensajeError != "La contraseña debe tener al menos") {
                errores.add(mensajeError)
            }

        }

        // Validar repetición de contraseña
        if (userPassword != userRepeatPassword) {
            errores.add("Las contraseñas no coinciden.")
        }
    }



}
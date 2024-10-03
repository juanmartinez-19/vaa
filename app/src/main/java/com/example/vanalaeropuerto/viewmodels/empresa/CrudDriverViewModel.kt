package com.example.vanalaeropuerto.viewmodels.empresa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.empresa.DriversRepository
import com.example.vanalaeropuerto.entities.Driver
import com.example.vanalaeropuerto.entities.Trip
import kotlinx.coroutines.launch

class CrudDriverViewModel : ViewModel() {

    private val errores = mutableListOf<String>()

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    val getDriversRepository : DriversRepository = DriversRepository()

    private val _driver = MutableLiveData<Driver?>()
    val driver : LiveData<Driver?> get() = _driver


    fun getDriverById (
        driverId : String?
    ){
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = getDriversRepository.getDriverById(driverId)) {
                is MyResult.Success -> {
                    _driver.value = result.data
                    _viewState.value = ViewState.Idle
                }

                is MyResult.Failure -> {
                    _driver.value = null
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }
        }
    }

    fun addDriver (
        driverId : String?,
        name: String?,
        surname: String?,
        tieneButaca: Boolean?,
        phoneNumber:String?,
        cuil:String?
    ){
        _viewState.value = ViewState.Loading

        this.errores.clear()

        this.validateDriverData(name, surname,cuil,phoneNumber)

        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters
            Log.e("ValidarDatos", "Errores: ${errores.joinToString(", ")}")
        } else {
            viewModelScope.launch {

                _viewState.value = ViewState.Loading

                when (getDriversRepository.addDriver(driverId,name,surname, tieneButaca,phoneNumber,cuil)) {
                    is MyResult.Success -> {
                        _viewState.value = ViewState.Confirmed
                    }
                    is MyResult.Failure -> {
                        _viewState.value = ViewState.Failure
                        Log.d("AddProductError", _viewState.value.toString())
                    }
                }

            }

        }


    }

    fun validateDriverData(
        name: String?,
        surname: String?,
        cuil:String?,
        phoneNumber:String?
    ) {
        _viewState.value = ViewState.Loading

        val errores = mutableListOf<String>()

        // Validación de nombre
        if (name.isNullOrBlank()) {
            errores.add("Nombre no puede estar vacío")
        } else if (!name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}\$"))) {
            errores.add("Nombre no es válido. Solo se permiten letras y debe tener entre 2 y 50 caracteres.")
        }

        // Validación de apellido
        if (surname.isNullOrBlank()) {
            errores.add("Apellido no puede estar vacío")
        } else if (!surname.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}\$"))) {
            errores.add("Apellido no es válido. Solo se permiten letras y debe tener entre 2 y 50 caracteres.")
        }

        // Validación de telefono
        if (phoneNumber.isNullOrBlank()) {
            errores.add("Telefono no puede estar vacía")
        } else if (!phoneNumber.matches(Regex("^[+]?[0-9]{10,13}\$"))) {
            errores.add("Teléfono no es válido. Debe contener entre 10 y 13 dígitos, y puede incluir un '+' al inicio.")
        }

        // Validación de cuil
        if (cuil.isNullOrBlank()) {
            errores.add("Cuil no puede estar vacío")
        } else if (cuil.length != 11) {
            errores.add("El CUIL debe tener exactamente 11 caracteres")
            Log.e("ValidarDatos", "2caracteres del cuil: ${cuil.length}")
        }

        // Manejo de errores
        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters
            Log.e("ValidarDatos", "Errores: ${errores.joinToString(", ")}")
        } else {
            _viewState.value = ViewState.Confirmed
        }
    }

}
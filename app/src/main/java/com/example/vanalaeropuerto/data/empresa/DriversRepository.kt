package com.example.vanalaeropuerto.data.empresa

import android.util.Log
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.user.VehiclesRepository
import com.example.vanalaeropuerto.entities.Driver
import com.example.vanalaeropuerto.entities.Requester
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.lang.reflect.InvocationTargetException

class DriversRepository {

    val driverList: MutableList<Driver> = mutableListOf()
    private val db = Firebase.firestore

    private val driver1 = Driver(
        driverId = "1",
        driverName = "Rodrigo",
        driverSurname = "Jamón",
        driverPhoneNumber = "1234567890",
        driverCuil = "23123456789",
        tieneButaca = true
    )
    private val driver2 = Driver(
        driverId = "2",
        driverName = "Carlos",
        driverSurname = "Gómez",
        driverPhoneNumber = "2345678901",
        driverCuil = "23234567890",
        tieneButaca = false
    )
    private val driver3 = Driver(
        driverId = "3",
        driverName = "Diego",
        driverSurname = "Fernández",
        driverPhoneNumber = "3456789012",
        driverCuil = "27345678901",
        tieneButaca = true
    )
    private val driver4 = Driver(
        driverId = "4",
        driverName = "Martín",
        driverSurname = "Rodríguez",
        driverPhoneNumber = "4567890123",
        driverCuil = "27456789012",
        tieneButaca = true
    )
    private val driver5 = Driver(
        driverId = "5",
        driverName = "Rodolfo",
        driverSurname = "Ramírez",
        driverPhoneNumber = "5678901234",
        driverCuil = "20567890123",
        tieneButaca = false
    )
    private val driver6 = Driver(
        driverId = "6",
        driverName = "Luis",
        driverSurname = "Pasteta",
        driverPhoneNumber = "6789012345",
        driverCuil = "20678901234",
        tieneButaca = true
    )
    private val driver7 = Driver(
        driverId = "7",
        driverName = "Jorge",
        driverSurname = "Álvarez",
        driverPhoneNumber = "7890123456",
        driverCuil = "20789012345",
        tieneButaca = false
    )
    private val driver8 = Driver(
        driverId = "8",
        driverName = "Tomás",
        driverSurname = "García",
        driverPhoneNumber = "8901234567",
        driverCuil = "20890123456",
        tieneButaca = true
    )
    private val driver9 = Driver(
        driverId = "9",
        driverName = "Lucas",
        driverSurname = "Díaz",
        driverPhoneNumber = "9012345678",
        driverCuil = "20901234567",
        tieneButaca = false
    )
    private val driver10 = Driver(
        driverId = "10",
        driverName = "Ricardo",
        driverSurname = "López",
        driverPhoneNumber = "1234567890",
        driverCuil = "20123456789",
        tieneButaca = true
    )


    init {
        driverList.add(driver1)
        driverList.add(driver2)
        driverList.add(driver3)
        driverList.add(driver4)
        driverList.add(driver5)
        driverList.add(driver6)
        driverList.add(driver7)
        driverList.add(driver8)
        driverList.add(driver9)
        driverList.add(driver10)
    }

    suspend fun addDriver  (
        driverId : String?,
        name: String?,
        surname: String?,
        tieneButaca: Boolean?,
        cuil:String?,
        telefono:String?
    ): MyResult<Unit> {

        return try {
            val driver = Driver (driverId,name,tieneButaca,telefono,cuil,surname)
            db.collection("drivers")
                .add(driver)
                .await()
            return MyResult.Success(Unit)
        } catch (e: InvocationTargetException) {
            Log.e("FirestoreError", "InvocationTargetException: ${e.message}", e)
            MyResult.Failure(e)
        } catch (e: Exception) {
            Log.e("FirestoreError", "Exception thrown: ${e.message}", e)
            MyResult.Failure(e)
        }

    }

    fun getDriverById(driverId: String?): MyResult<Driver?> {
        if (driverId != null) {
            if (driverId.isEmpty()) {
                return MyResult.Failure(IllegalArgumentException("Requester ID cannot be null or empty"))
            }
        }

        val driver = driverList.find { it.getDriverId() == driverId }

        return if (driver != null) {
            MyResult.Success(driver)
        } else {
            MyResult.Failure(NoSuchElementException("Requester not found"))
        }

    }

    fun getDriversByTripId (tripId : String) : MyResult<MutableList<Driver>> {
    /*val trip = TripsRepository.getTripById(tripId)
    if (trip != null) {
        val vehicleId = trip.getVehicleId()
        val drivers = VehiclesRepository.getDrivers(vehicleId)

        // Asigna el conductor al viaje
        trip.assignDriver(selectedDriver)
        println("Conductor ${selectedDriver.nombre} asignado al viaje ${trip.tripId}")
    }*/
    return MyResult.Success(driverList)
}

    fun getDrivers () : MyResult<MutableList<Driver>> {
        /*val trip = TripsRepository.getTripById(tripId)
        if (trip != null) {
            val vehicleId = trip.getVehicleId()
            val drivers = VehiclesRepository.getDrivers(vehicleId)

            // Asigna el conductor al viaje
            trip.assignDriver(selectedDriver)
            println("Conductor ${selectedDriver.nombre} asignado al viaje ${trip.tripId}")
        }*/
        return MyResult.Success(driverList)
    }
}
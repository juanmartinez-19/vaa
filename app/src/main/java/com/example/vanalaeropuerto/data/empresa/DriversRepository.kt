package com.example.vanalaeropuerto.data.empresa

import android.content.ContentValues
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

    suspend fun getDrivers () : MyResult<MutableList<Driver>> {
        val driversList : MutableList<Driver>

        return try {
            val documents =  db.collection("drivers")
                .get()
                .await()
            driversList = documents.toObjects(Driver::class.java)
            return MyResult.Success(driversList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }
}
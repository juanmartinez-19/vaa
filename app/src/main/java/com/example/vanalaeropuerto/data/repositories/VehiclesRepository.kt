package com.example.vanalaeropuerto.data.repositories

import android.content.ContentValues
import android.util.Log
import com.example.vanalaeropuerto.core.FirestoreCollections
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.entities.Vehicle
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VehiclesRepository @Inject constructor() {

    var vehiclesList : MutableList<Vehicle> = mutableListOf()
    private val db = Firebase.firestore

     suspend fun getVehicles () : MyResult<MutableList<Vehicle>> {

        return try {
            val documents =  db.collection(FirestoreCollections.VEHICLES)
                .get()
                .await()
            vehiclesList = documents.toObjects(Vehicle::class.java)
            return MyResult.Success(vehiclesList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }


}
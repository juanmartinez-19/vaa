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

    suspend fun getDriverById(driverId: String?): MyResult<Driver?> {

        if (driverId.isNullOrBlank()) {
            return MyResult.Failure(
                IllegalArgumentException("Driver ID cannot be null or empty")
            )
        }

        return try {
            val snapshot = db.collection("drivers")
                .document(driverId)
                .get()
                .await()

            if (!snapshot.exists()) {
                MyResult.Success(null) // no existe el driver
            } else {
                val driver = snapshot.toObject(Driver::class.java)
                MyResult.Success(driver)
            }

        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}", e)
            MyResult.Failure(e)
        }
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
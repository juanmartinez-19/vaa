package com.example.vanalaeropuerto.data

import android.content.ContentValues
import android.util.Log
import com.example.vanalaeropuerto.entities.Trip
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.lang.reflect.InvocationTargetException

class TripsRepository {

    var tripsList : MutableList<Trip> = mutableListOf()
    private val db = Firebase.firestore

    suspend fun confirmTrip(tripId: String): MyResult<Trip?> {
        return try {
            val docRef = db.collection("trips").document(tripId)

            // 1️⃣ Update del estado
            docRef.update(
                mapOf(
                    "state" to "CONFIRMED"
                )
            ).await()

            // 2️⃣ Volver a leer el trip actualizado
            val snapshot = docRef.get().await()

            if (!snapshot.exists()) {
                MyResult.Success(null)
            } else {
                val trip = snapshot.toObject(Trip::class.java)
                MyResult.Success(trip)
            }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error confirming trip", e)
            MyResult.Failure(e)
        }
    }


    suspend fun addTrip (trip : Trip)  : MyResult<Trip?>  {
        return try {
            db.collection("trips")
                .document(trip.getTripId()!!) // ID del documento = requesterId
                .set(trip) // set en lugar de add
                .await()

            MyResult.Success(trip)
        } catch (e: Exception) {
            MyResult.Failure(Exception("Error adding trip: ${e.message}"))
        }

    }

    suspend fun cancelTrip(tripId: String): MyResult<Trip?> {
        return try {
            val docRef = db.collection("trips").document(tripId)

            // 1️⃣ Update del estado
            docRef.update(
                mapOf(
                    "state" to "CANCELLED"
                )
            ).await()

            // 2️⃣ Volver a leer el trip actualizado
            val snapshot = docRef.get().await()

            if (!snapshot.exists()) {
                MyResult.Success(null)
            } else {
                val trip = snapshot.toObject(Trip::class.java)
                MyResult.Success(trip)
            }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error confirming trip", e)
            MyResult.Failure(e)
        }
    }

    suspend fun getTrip(tripId: String): MyResult<Trip?> {
        return try {
            val snapshot = db.collection("trips")
                .document(tripId)
                .get()
                .await()

            if (!snapshot.exists()) {
                MyResult.Success(null)
            } else {
                val trip = snapshot.toObject(Trip::class.java)
                MyResult.Success(trip)
            }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Exception thrown: ${e.message}", e)
            MyResult.Failure(e)
        }
    }


    fun updateTrip  (
        tripId: String?,
        originAddress: String?,
        destinationAddress: String?,
        selectedDateInMillis: Long?,
        price:Float?
    ) : MyResult<Trip?> {
        return try {

            val tripToUpdate = tripsList.find { it.getTripId() == tripId }

            tripToUpdate?.let { trip ->
                trip.setOriginAddress(originAddress ?: trip.getOriginAddress())
                trip.setDestinationAddress(destinationAddress ?: trip.getDestinationAddress())
                trip.setDate(selectedDateInMillis.toString())
                trip.setPrice(price ?: trip.getPrice())
            }

            return MyResult.Success(tripToUpdate)
        } catch (e: InvocationTargetException) {
            Log.e("FirestoreError", "InvocationTargetException: ${e.message}", e)
            MyResult.Failure(e)
        } catch (e: Exception) {
            Log.e("FirestoreError", "Exception thrown: ${e.message}", e)
            MyResult.Failure(e)
        }
    }

    suspend fun getPendingTrips () : MyResult<MutableList<Trip>> {
        return try {
            val documents =  db.collection("trips")
                .whereEqualTo("state", "PENDING")
                .get()
                .await()
            tripsList = documents.toObjects(Trip::class.java)
            return MyResult.Success(tripsList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

    suspend fun getTripHistory () : MyResult<MutableList<Trip>> {
        return try {
            val documents =  db.collection("trips")
                .whereEqualTo("state", "done")
                .get()
                .await()
            tripsList = documents.toObjects(Trip::class.java)
            return MyResult.Success(tripsList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

    suspend fun getConfirmedTrips () : MyResult<MutableList<Trip>> {
        return try {
            val documents =  db.collection("trips")
                .whereEqualTo("state", "confirmed")
                .get()
                .await()
            tripsList = documents.toObjects(Trip::class.java)
            return MyResult.Success(tripsList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

}
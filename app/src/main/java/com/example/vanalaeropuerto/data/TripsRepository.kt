package com.example.vanalaeropuerto.data

import android.content.ContentValues
import android.util.Log
import com.example.vanalaeropuerto.data.mapper.toDomain
import com.example.vanalaeropuerto.data.remote.dto.TripFirestoreDto
import com.example.vanalaeropuerto.entities.Trip
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.lang.reflect.InvocationTargetException

class TripsRepository {

    var tripsList : MutableList<Trip> = mutableListOf()
    private val db = Firebase.firestore
    private var listener: ListenerRegistration? = null

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
                val tripDto = snapshot.toObject(TripFirestoreDto::class.java)
                val trip = tripDto?.toDomain()
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
                val tripDto = snapshot.toObject(TripFirestoreDto::class.java)
                val trip = tripDto?.toDomain()
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
                val tripDto = snapshot.toObject(TripFirestoreDto::class.java)
                val trip = tripDto?.toDomain()
                MyResult.Success(trip)
            }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Exception thrown: ${e.message}", e)
            MyResult.Failure(e)
        }
    }


    suspend fun updateTrip(
        tripId: String?,
        originAddress: String?,
        destinationAddress: String?,
        selectedDateInMillis: Long?,
        price: Float?
    ): MyResult<Trip?> {

        if (tripId.isNullOrBlank()) {
            return MyResult.Failure(
                IllegalArgumentException("Trip ID cannot be null or empty")
            )
        }

        return try {
            val docRef = db.collection("trips").document(tripId)

            // 1️⃣ Armar solo los campos a actualizar
            val updates = mutableMapOf<String, Any>()

            originAddress?.let { updates["originAddress"] = it }
            destinationAddress?.let { updates["destinationAddress"] = it }
            selectedDateInMillis?.let { updates["date"] = it }
            price?.let { updates["price"] = it }

            if (updates.isEmpty()) {
                // Nada para actualizar → devolver el trip actual
                val snapshot = docRef.get().await()

                val tripDto = snapshot.toObject(TripFirestoreDto::class.java)
                val trip = tripDto?.toDomain()

                return MyResult.Success(trip)
            }


            // 2️⃣ Update en Firestore
            docRef.update(updates).await()

            // 3️⃣ Volver a leer el trip actualizado
            val snapshot = docRef.get().await()

            if (!snapshot.exists()) {
                MyResult.Success(null)
            } else {
                val trip = snapshot.toObject(Trip::class.java)
                MyResult.Success(trip)
            }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error updating trip", e)
            MyResult.Failure(e)
        }
    }

    fun listenPendingTrips(
        onSuccess: (List<Trip>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        listener?.remove()

        listener = db.collection("trips")
            .whereEqualTo("state", "PENDING")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val trips = snapshots
                    ?.toObjects(Trip::class.java)
                    ?: emptyList()

                onSuccess(trips)
            }
    }

    fun clearListener() {
        listener?.remove()
        listener = null
    }

    suspend fun getPendingTrips () : MyResult<MutableList<Trip>> {
        return try {

            val documents =  db.collection("trips")
                .whereEqualTo("state", "PENDING")
                .get()
                .await()

            for (doc in documents.documents) {
                Log.e(
                    "TRIP_DEBUG",
                    "id=${doc.id}, date=${doc.get("date")} (${doc.get("date")?.javaClass})"
                )
            }


            tripsList = documents.toObjects(TripFirestoreDto::class.java)
                .map { it.toDomain() }
                .toMutableList()

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
            tripsList = documents.toObjects(TripFirestoreDto::class.java)
                .map { it.toDomain() }
                .toMutableList()

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
            tripsList = documents.toObjects(TripFirestoreDto::class.java)
                .map { it.toDomain() }
                .toMutableList()

            return MyResult.Success(tripsList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

}
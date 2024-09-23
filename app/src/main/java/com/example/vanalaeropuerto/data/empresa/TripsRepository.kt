package com.example.vanalaeropuerto.data.empresa

import android.util.Log
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.entities.Trip
import java.lang.reflect.InvocationTargetException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TripsRepository {

    val tripsList : MutableList<Trip> = mutableListOf()
    private val trip1 = Trip(
        date = "10-10-2024",
        originAddress = "Pumacahua 50",
        destinationAddress = "Senillosa 484",
        adults = 2,
        children = 1,
        babies = 0,
        luggageKg = 15.0F,
        segmentId = "ABC123",
        tripId = "1",
        requesterId = "1",
        state = "pending"
    )

    private val trip2 = Trip(
        date = "12-10-2024",
        originAddress = "Hortiguera 333",
        destinationAddress = "Aeropuerto de Ezeiza",
        adults = 1,
        children = 0,
        babies = 0,
        luggageKg = 8.0F,
        segmentId = "XYZ789",
        tripId = "2",
        requesterId = "1",
        state = "pending"
    )

    private val trip3 = Trip(
        date = "15-10-2024",
        originAddress = "La Pampa 4921",
        destinationAddress = "Estados Unidos 423",
        adults = 3,
        children = 2,
        babies = 1,
        luggageKg = 25.0F,
        segmentId = "DEF456",
        tripId = "3",
        requesterId = "2",
        state = "pending"
    )

    private val trip4 = Trip(
        date = "20-09-2024",
        originAddress = "Aeroparque",
        destinationAddress = "José León Suárez 4891",
        adults = 4,
        children = 0,
        babies = 2,
        luggageKg = 30.0F,
        segmentId = "GHI123",
        tripId = "4",
        requesterId = "3",
        state = "confirmed"
    )

    private val trip5 = Trip(
        date = "12-11-2024",
        originAddress = "Av. Corrientes 1200",
        destinationAddress = "Sarmiento 3456",
        adults = 1,
        children = 0,
        babies = 0,
        luggageKg = 10.0F,
        segmentId = "5432123",
        tripId = "5",
        requesterId = "3",
        state = "pending"
    )

    private val trip6 = Trip(
        date = "25-09-2024",
        originAddress = "San Martín 123",
        destinationAddress = "Independencia 678",
        adults = 2,
        children = 2,
        babies = 1,
        luggageKg = 25.5F,
        segmentId = "6789345",
        tripId = "6",
        requesterId = "4",
        state = "pending"
    )

    private val trip7 = Trip(
        date = "30-08-2024",
        originAddress = "Belgrano 456",
        destinationAddress = "9 de Julio 789",
        adults = 3,
        children = 0,
        babies = 0,
        luggageKg = 18.0F,
        segmentId = "3456789",
        tripId = "7",
        requesterId = "5",
        state = "finalized"
    )

    private val trip8 = Trip(
        date = "05-12-2024",
        originAddress = "Lavalle 987",
        destinationAddress = "Mitre 123",
        adults = 1,
        children = 1,
        babies = 0,
        luggageKg = 8.0F,
        segmentId = "1234567",
        tripId = "8",
        requesterId = "5",
        state = "finalized"
    )

    private val trip9 = Trip(
        date = "15-07-2024",
        originAddress = "Las Heras 220",
        destinationAddress = "Rivadavia 654",
        adults = 4,
        children = 0,
        babies = 1,
        luggageKg = 30.0F,
        segmentId = "8765432",
        tripId = "9",
        requesterId = "2",
        state = "pending"
    )

    private val trip10 = Trip(
        date = "28-06-2024",
        originAddress = "Alvear 300",
        destinationAddress = "Callao 500",
        adults = 2,
        children = 3,
        babies = 0,
        luggageKg = 22.5F,
        segmentId = "9876543",
        tripId = "10",
        requesterId = "3",
        state = "confirmed"
    )

    private val trip11 = Trip(
        date = "01-11-2024",
        originAddress = "Perón 1020",
        destinationAddress = "Entre Ríos 300",
        adults = 1,
        children = 0,
        babies = 0,
        luggageKg = 12.0F,
        segmentId = "1234568",
        tripId = "11",
        requesterId = "1",
        state = "confirmed"
    )

    private val trip12 = Trip(
        date = "20-05-2024",
        originAddress = "Juncal 450",
        destinationAddress = "Palermo 100",
        adults = 3,
        children = 1,
        babies = 0,
        luggageKg = 20.0F,
        segmentId = "6543210",
        tripId = "12",
        requesterId = "3",
        state = "pending"
    )

    private val trip13 = Trip(
        date = "10-09-2024",
        originAddress = "Santa Fe 234",
        destinationAddress = "Uruguay 111",
        adults = 2,
        children = 2,
        babies = 1,
        luggageKg = 16.0F,
        segmentId = "5674321",
        tripId = "13",
        requesterId = "5",
        state = "confirmed"
    )


    init {
        tripsList.add(trip1)
        tripsList.add(trip2)
        tripsList.add(trip3)
        tripsList.add(trip4)
        tripsList.add(trip5)
        tripsList.add(trip6)
        tripsList.add(trip7)
        tripsList.add(trip8)
        tripsList.add(trip9)
        tripsList.add(trip10)
        tripsList.add(trip11)
        tripsList.add(trip12)
        tripsList.add(trip13)
    }


    fun getTrip(tripId: String): MyResult<Trip?> {
        return try {
            // Buscar el viaje en la lista usando el tripId
            val trip = tripsList.find { it.getTripId() == tripId }

            // Si el viaje es encontrado, retornar éxito con el viaje
            if (trip != null) {
                MyResult.Success(trip)
            } else {
                // Si no se encuentra el viaje, retornar éxito con valor null
                MyResult.Success(null)
            }
        } catch (e: Exception) {
            // Capturar cualquier excepción y retornar failure con la excepción
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

    fun getPendingTrips () : MyResult<MutableList<Trip>> {
        val pendingTrips = tripsList.filter { trip ->
            trip.getState() == "pending"
        }

        val sortedPendingTrips = pendingTrips.sortedBy { trip ->
            trip.getDate()?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
            }
        }


        return MyResult.Success(sortedPendingTrips.toMutableList())

        /*
        return try {
            val documents =  db.collection("trips")
                .get()
                .await()
            tripsList = documents.toObjects(Trip::class.java)
            return MyResult.Success(tripsList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
        */
    }

    fun getTripHistory () : MyResult<MutableList<Trip>> {
        val tripHistory = tripsList.filter { trip ->
            trip.getState() == "finalized"
        }

        val sortedTripHistory = tripHistory.sortedBy { trip ->
            trip.getDate()?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
            }
        }

        return MyResult.Success(sortedTripHistory.toMutableList())

        /*
        return try {
            val documents =  db.collection("trips")
                .get()
                .await()
            tripsList = documents.toObjects(Trip::class.java)
            return MyResult.Success(tripsList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
        */
    }

    fun getConfirmedTrips () : MyResult<MutableList<Trip>> {
        val confirmedTrips = tripsList.filter { trip ->
            trip.getState() == "confirmed"
        }

        val sortedConfirmedTrips = confirmedTrips.sortedBy { trip ->
            trip.getDate()?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
            }
        }

        return MyResult.Success(sortedConfirmedTrips.toMutableList())

        /*
        return try {
            val documents =  db.collection("trips")
                .get()
                .await()
            tripsList = documents.toObjects(Trip::class.java)
            return MyResult.Success(tripsList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
        */
    }

}
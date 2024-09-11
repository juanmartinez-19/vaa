package com.example.vanalaeropuerto.data.empresa

import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.entities.Trip
import java.text.SimpleDateFormat
import java.util.Locale

class TripsRepository {
    fun getTrips () : MyResult<MutableList<Trip>> {
        val tripsList : MutableList<Trip> = mutableListOf()

        tripsList.add(
            Trip(
                date = "2024-10-10",
                originAddress = "Pumacahua 50",
                destinationAddress = "Senillosa 484",
                adults = 2,
                children = 1,
                babies = 0,
                luggageKg = 15.0F,
                vehicleId = "ABC123"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-10-12",
                originAddress = "Hortiguera 333",
                destinationAddress = "Aeropuerto de Ezeiza",
                adults = 1,
                children = 0,
                babies = 0,
                luggageKg = 8.0F,
                vehicleId = "XYZ789"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-10-15",
                originAddress = "La Pampa 4921",
                destinationAddress = "Estados Unidos 423",
                adults = 3,
                children = 2,
                babies = 1,
                luggageKg = 25.0F,
                vehicleId = "DEF456"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-09-20",
                originAddress = "Aeroparque",
                destinationAddress = "José León Suárez 4891",
                adults = 4,
                children = 0,
                babies = 2,
                luggageKg = 30.0F,
                vehicleId = "GHI123"
            )
        )

        val sortedTrips = tripsList.sortedBy { trip ->
            trip.getDate()?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
            }
        }

        return MyResult.Success(sortedTrips.toMutableList())

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
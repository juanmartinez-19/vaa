package com.example.vanalaeropuerto.data.empresa

import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.entities.Trip
import java.text.SimpleDateFormat
import java.util.Locale

class TripsRepository {
    fun getPendingTrips () : MyResult<MutableList<Trip>> {
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
                segmentoId = "ABC123",
                tripId="1",
                solicitanteId="1"
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
                segmentoId = "XYZ789",
                tripId="1",
                solicitanteId="1"
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
                segmentoId = "DEF456",
                tripId="1",
                solicitanteId="1"
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
                segmentoId = "GHI123",
                tripId="1",
                solicitanteId="1"
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

    fun getTripHistory () : MyResult<MutableList<Trip>> {
        val tripsList : MutableList<Trip> = mutableListOf()

        tripsList.add(
            Trip(
                date = "2024-11-12",
                originAddress = "Av. Corrientes 1200",
                destinationAddress = "Sarmiento 3456",
                adults = 1,
                children = 0,
                babies = 0,
                luggageKg = 10.0F,
                segmentoId = "5432123",
                tripId = "9876543",
                solicitanteId = "1234567"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-09-25",
                originAddress = "San Martín 123",
                destinationAddress = "Independencia 678",
                adults = 2,
                children = 2,
                babies = 1,
                luggageKg = 25.5F,
                segmentoId = "6789345",
                tripId = "5432123",
                solicitanteId = "9876543"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-08-30",
                originAddress = "Belgrano 456",
                destinationAddress = "9 de Julio 789",
                adults = 3,
                children = 0,
                babies = 0,
                luggageKg = 18.0F,
                segmentoId = "3456789",
                tripId = "3456789",
                solicitanteId = "2345678"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-12-05",
                originAddress = "Lavalle 987",
                destinationAddress = "Mitre 123",
                adults = 1,
                children = 1,
                babies = 0,
                luggageKg = 8.0F,
                segmentoId = "1234567",
                tripId = "2345678",
                solicitanteId = "3456789"
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

    fun getConfirmedTrips () : MyResult<MutableList<Trip>> {
        val tripsList : MutableList<Trip> = mutableListOf()

        tripsList.add(
            Trip(
                date = "2024-07-15",
                originAddress = "Las Heras 220",
                destinationAddress = "Rivadavia 654",
                adults = 4,
                children = 0,
                babies = 1,
                luggageKg = 30.0F,
                segmentoId = "8765432",
                tripId = "7654321",
                solicitanteId = "1239876"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-06-28",
                originAddress = "Alvear 300",
                destinationAddress = "Callao 500",
                adults = 2,
                children = 3,
                babies = 0,
                luggageKg = 22.5F,
                segmentoId = "9876543",
                tripId = "8765432",
                solicitanteId = "2348765"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-11-01",
                originAddress = "Perón 1020",
                destinationAddress = "Entre Ríos 300",
                adults = 1,
                children = 0,
                babies = 0,
                luggageKg = 12.0F,
                segmentoId = "1234568",
                tripId = "3456780",
                solicitanteId = "5678901"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-05-20",
                originAddress = "Juncal 450",
                destinationAddress = "Palermo 100",
                adults = 3,
                children = 1,
                babies = 0,
                luggageKg = 20.0F,
                segmentoId = "6543210",
                tripId = "5432109",
                solicitanteId = "4321098"
            )
        )

        tripsList.add(
            Trip(
                date = "2024-09-10",
                originAddress = "Santa Fe 234",
                destinationAddress = "Uruguay 111",
                adults = 2,
                children = 2,
                babies = 1,
                luggageKg = 16.0F,
                segmentoId = "5674321",
                tripId = "6789432",
                solicitanteId = "0987654"
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
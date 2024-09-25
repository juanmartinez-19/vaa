package com.example.vanalaeropuerto.data.user

import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.entities.Vehicle

class VehiclesRepository {

    val vehiclesList : MutableList<Vehicle> = mutableListOf()

   val vehicle = Vehicle(
    vehicleId = "1",
    vehiclePrice = 25000.0,
    vehicleDriver = "John Doe",
    vehicleName = "Toyota Corolla",
    vehicleUrlImage = "https://tcl-s3-bucket.s3.amazonaws.com/web/media/images/Corolla_destacado.max-730x330_2.max-730x330.png",
    vehicleLuggageCapacity = 100F,
    vehiclePassangerCapacity = 3
    )

    val vehicle1 = Vehicle(
        vehicleId = "1",
        vehiclePrice = 25000.0,
        vehicleDriver = "John Doe",
        vehicleName = "Toyota Corolla",
        vehicleUrlImage = "https://tcl-s3-bucket.s3.amazonaws.com/web/media/images/Corolla_destacado.max-730x330_2.max-730x330.png",
        vehicleLuggageCapacity = 100F,
        vehiclePassangerCapacity = 3
    )

    val vehicle2 = Vehicle(
        vehicleId = "4",
        vehiclePrice = 45000.0,
        vehicleDriver = "Emily Davis",
        vehicleName = "BMW 3 Series",
        vehicleUrlImage = "https://www.bmw.cl/content/dam/bmw/common/all-models/3-series/sedan/2022/navigation/bmw-3-series-sedan-lci-modelfinder.png/jcr:content/renditions/cq5dam.resized.img.585.low.time1652686057507.png",
        vehicleLuggageCapacity = 100F,
        vehiclePassangerCapacity = 4
    )

    val vehicle3 = Vehicle(
        vehicleId = "3",
        vehiclePrice = 20000.0,
        vehicleDriver = "Mike Johnson",
        vehicleName = "Ford Focus",
        vehicleUrlImage = "https://www.carone.com.ar/wp-content/uploads/2024/05/fc78c9f4-6351-430f-8fe1-d5f8209bb9f5-1.jpg",
        vehicleLuggageCapacity = 100F,
        vehiclePassangerCapacity = 3
    )

    val vehicle4 = Vehicle(
        vehicleId = "2",
        vehiclePrice = 30000.0,
        vehicleDriver = "Jane Smith",
        vehicleName = "Honda Civic",
        vehicleUrlImage = "https://cdn.motor1.com/images/mgl/WV6rr/s1/lanzamiento-honda-civic-2017.webp",
        vehicleLuggageCapacity = 100F,
        vehiclePassangerCapacity = 3
    )

    init {
        vehiclesList.add(vehicle)
        vehiclesList.add(vehicle1)
        vehiclesList.add(vehicle2)
        vehiclesList.add(vehicle3)
        vehiclesList.add(vehicle4)
    }

     fun getVehicles (passangers : Int, luggage : Float) : MyResult<MutableList<Vehicle>> {
        val vehiclesReturn : MutableList<Vehicle> = mutableListOf()

        vehiclesReturn.addAll(
            vehiclesList.filter {
                it.getVehiclePassangerCapacity()!! >= passangers && it.getVehicleLuggageCapacity()!! >= luggage
            }
        )

        return MyResult.Success(vehiclesReturn)

        /*
        return try {
            val documents =  db.collection("products")
                .get()
                .await()
            productsList = documents.toObjects(Vehicle::class.java)
            return MyResult.Success(productsList)
        } catch (e : Exception) {
            Log.e(ContentValues.TAG, "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
        */
    }


}
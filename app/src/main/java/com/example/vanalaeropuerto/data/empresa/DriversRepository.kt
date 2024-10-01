package com.example.vanalaeropuerto.data.empresa

import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.user.VehiclesRepository
import com.example.vanalaeropuerto.entities.Driver
import com.example.vanalaeropuerto.entities.Requester

class DriversRepository {

    val driverList: MutableList<Driver> = mutableListOf()

    private val driver1 = Driver(driverId = "1", driverName = "Rodrigo Jamón", tieneButaca = true)
    private val driver2 = Driver(driverId = "2", driverName = "Carlos Gómez", tieneButaca = false)
    private val driver3 = Driver(driverId = "3", driverName = "Diego Fernández", tieneButaca = true)
    private val driver4 = Driver(driverId = "4", driverName = "Martín Rodríguez", tieneButaca = true)
    private val driver5 = Driver(driverId = "5", driverName = "Rodolfo Ramírez", tieneButaca = false)
    private val driver6 = Driver(driverId = "6", driverName = "Luis Pasteta", tieneButaca = true)
    private val driver7 = Driver(driverId = "7", driverName = "Jorge Álvarez", tieneButaca = false)
    private val driver8 = Driver(driverId = "8", driverName = "Tomás García", tieneButaca = true)
    private val driver9 = Driver(driverId = "9", driverName = "Lucas Díaz", tieneButaca = false)
    private val driver10 =Driver(driverId = "10", driverName = "Ricardo López", tieneButaca = true)

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

    fun addDriver  (
        driverId : String?,
        name: String?,
        surname: String?,
        tieneButaca: Boolean?,
        cuil:String?,
        telefono:String?
    ): MyResult<Unit> {

        return MyResult.Success(Unit)
    }

    fun getDriverById(driverId: String): MyResult<Driver?> {
        if (driverId.isEmpty()) {
            return MyResult.Failure(IllegalArgumentException("Requester ID cannot be null or empty"))
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
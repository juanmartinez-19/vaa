package com.example.vanalaeropuerto.entities

class Driver(
    private var driverId : String?="",
    private var driverName : String?="",
    private var tieneButaca : Boolean
) {

    fun tieneButaca() : Boolean {
        return this.tieneButaca
    }
    fun getDriverId() : String? {
        return this.driverId
    }
    fun getDriverName() : String? {
        return this.driverName
    }

}
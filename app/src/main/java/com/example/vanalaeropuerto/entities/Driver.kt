package com.example.vanalaeropuerto.entities

class Driver(
    private var driverId : String?="",
    private var driverName : String?="",
    private var tieneButaca : Boolean,
    private var driverPhoneNumber : String?="",
    private var driverCuil : String?="",
    private var driverSurname : String?=""
) {

    fun getTieneButaca() : Boolean {
        return this.tieneButaca
    }
    fun getDriverId() : String? {
        return this.driverId
    }
    fun getDriverName() : String? {
        return this.driverName
    }
    fun getDriverSurname() : String? {
        return this.driverSurname
    }
    fun getDriverCuil() : String? {
        return this.driverCuil
    }
    fun getDriverPhoneNumber() : String? {
        return this.driverPhoneNumber
    }


}
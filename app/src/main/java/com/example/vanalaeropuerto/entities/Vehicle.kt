package com.example.vanalaeropuerto.entities

class Vehicle (
    private var vehicleId : String?="",
    private var vehiclePrice : Double?=0.0,
    private var vehicleDriver : String?="",
    private var vehicleName : String?="",
    private var vehicleUrlImage : String?,
    private var vehiclePassangerCapacity : Int?=0,
    private var vehicleLuggageCapacity : Float?=0F
) {

    fun getVehiclePrice() : Double? {
        return this.vehiclePrice
    }

    fun getVehicleDriver() : String? {
        return this.vehicleDriver
    }

    fun getVehicleId() : String? {
        return this.vehicleId
    }

    fun getVehicleName() : String? {
        return this.vehicleName
    }

    fun getVehicleUrlImage() : String? {
        return this.vehicleUrlImage
    }

    fun getVehiclePassangerCapacity() : Int? {
        return this.vehiclePassangerCapacity
    }

    fun getVehicleLuggageCapacity() : Float? {
        return this.vehicleLuggageCapacity
    }

}
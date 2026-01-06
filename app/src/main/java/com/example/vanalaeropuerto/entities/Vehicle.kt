package com.example.vanalaeropuerto.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Vehicle (
    private var vehicleId : String?="",
    private var vehiclePrice : Float?=0F,
    private var vehicleDriver : String?="",
    private var vehicleName : String?="",
    private var vehicleUrlImage : String?,
    private var vehiclePassangerCapacity : Int?=0,
    private var vehicleLuggageCapacity : Float?=0F
) : Parcelable {

    constructor() : this(null, null,null,null,null,null,null)

    fun getVehiclePrice() : Float? {
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
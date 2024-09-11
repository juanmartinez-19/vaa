package com.example.vanalaeropuerto.entities

class Trip (
    private var date : String?="",
    private var originAddress : String?="",
    private var destinationAddress : String?="",
    private var adults : Int?=0,
    private var children : Int?=0,
    private var babies : Int?=0,
    private var luggageKg : Float?=0F,
    private var vehicleId : String?=""
) {

    fun getDate() : String? {
        return this.date
    }
    fun getOriginAddress() : String? {
        return this.originAddress
    }
    fun getDestinationAddress() : String? {
        return this.destinationAddress
    }
    fun getAdults() : Int? {
        return this.adults
    }
    fun getChildren() : Int? {
        return this.children
    }
    fun getBabies() : Int? {
        return this.babies
    }
    fun getLuggageKg() : Float? {
        return this.luggageKg
    }
    fun getVehicleId() : String? {
        return this.vehicleId
    }

}
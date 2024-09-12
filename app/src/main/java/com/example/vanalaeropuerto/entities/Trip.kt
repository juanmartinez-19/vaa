package com.example.vanalaeropuerto.entities

class Trip (
    private var date : String?="",
    private var originAddress : String?="",
    private var destinationAddress : String?="",
    private var adults : Int?=0,
    private var children : Int?=0,
    private var babies : Int?=0,
    private var luggageKg : Float?=0F,
    private var price : Float?=0F,
    private var tripId : String?="",
    private var segmentoId : String?="",
    private var solicitanteId : String?=""
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
    fun getPrice() : Float? {
        return this.price
    }

    fun getSegmentoId() : String? {
        return this.segmentoId
    }
    fun getTripId() : String? {
        return this.tripId
    }
    fun getSolicitanteId() : String? {
        return this.solicitanteId
    }


}
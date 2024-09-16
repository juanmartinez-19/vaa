package com.example.vanalaeropuerto.entities

class TripRequester (
    private var trip : Trip,
    private var requester : Requester
){

    fun getTrip () : Trip {
        return this.trip
    }

    fun getRequester() : Requester {
        return this.requester
    }

}
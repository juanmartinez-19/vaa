package com.example.vanalaeropuerto.data.remote.dto

data class TripFirestoreDto(
    var date: Long? = null,
    var originAddress: String? = null,
    var destinationAddress: String? = null,
    var adults: Long? = null,
    var children: Long? = null,
    var babies: Long? = null,
    var luggageKg: Double? = null,
    var price: Double? = null,
    var state: String? = null,
    var tripId: String? = null,
    var requesterId: String? = null,
    var driverId: String? = null
)

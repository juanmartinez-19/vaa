package com.example.vanalaeropuerto.data.mapper

import com.example.vanalaeropuerto.data.remote.dto.TripFirestoreDto
import com.example.vanalaeropuerto.entities.Trip

fun TripFirestoreDto.toDomain(): Trip {
    return Trip(
        date = date,
        originAddress = originAddress,
        destinationAddress = destinationAddress,
        adults = adults?.toInt(),
        children = children?.toInt(),
        babies = babies?.toInt(),
        luggageKg = luggageKg?.toFloat(),
        price = price?.toFloat(),
        state = state,
        tripId = tripId,
        requesterId = requesterId
    )
}

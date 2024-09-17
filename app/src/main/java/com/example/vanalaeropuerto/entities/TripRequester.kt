package com.example.vanalaeropuerto.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class TripRequester(
    private var trip: @RawValue Trip?,
    private var requester: @RawValue Requester?
) : Parcelable {

    constructor() : this(null, null)

    fun getTrip(): Trip? {
        return this.trip
    }

    fun getRequester(): Requester? {
        return this.requester
    }
}
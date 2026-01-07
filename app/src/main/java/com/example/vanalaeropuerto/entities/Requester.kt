package com.example.vanalaeropuerto.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Requester(
    private var requesterId : String?="",
    private var requesterName : String?="",
    private var requesterSurname : String?="",
    private var requesterPhoneNumber : String?="",
    private var requesterCuil : String?="",
    private var requesterRole : String?=""
) : Parcelable {

    constructor() : this(null, null,null,null,null,null)

    fun getRequesterId () : String? {
        return this.requesterId
    }

    fun getRequesterRole () : String? {
        return this.requesterRole
    }

    fun getRequesterName () : String? {
        return this.requesterName
    }

    fun getRequesterSurname () : String? {
        return this.requesterSurname
    }

    fun getRequesterPhoneNumber () : String? {
        return this.requesterPhoneNumber
    }

    fun getRequesterCuil () : String? {
        return this.requesterCuil
    }
}
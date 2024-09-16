package com.example.vanalaeropuerto.entities

class Requester(
    private var requesterId : String?="",
    private var requesterName : String?="",
    private var requesterSurname : String?="",
    private var requesterPhoneNumber : String?="",
    private var requesterCuil : String?=""
) {

    fun getRequesterId () : String? {
        return this.requesterId
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
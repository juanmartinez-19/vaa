package com.example.vanalaeropuerto.data.repositories

import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.entities.Requester
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class RequesterRepository {

    private val db = Firebase.firestore

    suspend fun signIn(
        userName : String,
        userSurname : String,
        phoneNumber :String,
        userCuil : String,
        uid : String?
    ) : MyResult<Requester> {
        return try {

            val requester = Requester(
                requesterName = userName,
                requesterSurname = userSurname,
                requesterPhoneNumber = phoneNumber,
                requesterCuil = userCuil,
                requesterId = uid,
                requesterRole = "USER"
            )

            db.collection("requesters")
                .document(uid!!) // ID del documento = requesterId
                .set(requester) // set en lugar de add
                .await()

            MyResult.Success(requester)
        } catch (e: Exception) {
            MyResult.Failure(Exception("Error signing in: ${e.message}"))
        }

    }

    suspend fun getRequester(requesterId: String?): MyResult<Requester?> {
        if (requesterId.isNullOrEmpty()) {
            return MyResult.Failure(IllegalArgumentException("Requester ID cannot be null or empty"))
        }

        return try {
            // Se busca el documento en Firestore con el ID dado
            val documentSnapshot = db.collection("requesters")
                .document(requesterId)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                 // Convertimos el documento a objeto Requester
                val requester = documentSnapshot.toObject(Requester::class.java)
                MyResult.Success(requester)
            } else {
                MyResult.Success(null) // No existe el requester con ese ID
            }
        } catch (e: Exception) {
            MyResult.Failure(Exception("Error fetching requester: ${e.message}"))
        }
    }

}
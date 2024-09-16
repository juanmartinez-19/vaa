package com.example.vanalaeropuerto.data.empresa

import android.util.Log
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.entities.Requester

class RequesterRepository {

    val requestersList: MutableList<Requester> = mutableListOf()

    val requester1 = Requester(
        requesterId = "1",
        requesterName = "Juan",
        requesterSurname = "Pérez",
        requesterPhoneNumber = "123456789",
        requesterCuil = "20123456789"
    )

    val requester2 = Requester(
        requesterId = "2",
        requesterName = "Ana",
        requesterSurname = "González",
        requesterPhoneNumber = "987654321",
        requesterCuil = "27987654321"
    )

    val requester3 = Requester(
        requesterId = "3",
        requesterName = "Carlos",
        requesterSurname = "Lopez",
        requesterPhoneNumber = "1122334455",
        requesterCuil = "23112233445"
    )

    val requester4 = Requester(
        requesterId = "4",
        requesterName = "María",
        requesterSurname = "Fernández",
        requesterPhoneNumber = "6677889900",
        requesterCuil = "27223344556"
    )

    val requester5 = Requester(
        requesterId = "5",
        requesterName = "Pedro",
        requesterSurname = "Martínez",
        requesterPhoneNumber = "4455667788",
        requesterCuil = "20224455667"
    )

    init {
        requestersList.add(requester1)
        requestersList.add(requester2)
        requestersList.add(requester3)
        requestersList.add(requester4)
        requestersList.add(requester5)
    }

    fun getRequester(requesterId: String?): MyResult<Requester?> {

        if (requesterId.isNullOrEmpty()) {
            return MyResult.Failure(IllegalArgumentException("Requester ID cannot be null or empty"))
        }

        val requester = requestersList.find { it.getRequesterId() == requesterId }

        return if (requester != null) {
            MyResult.Success(requester)
        } else {
            MyResult.Failure(NoSuchElementException("Requester not found"))
        }

        /*
        return try {
            val querySnapshot = db.collection("products")
                .whereEqualTo("productId", productId ?: "")
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val documentSnapshot = querySnapshot.documents.first()
                val product = documentSnapshot.toObject(Product::class.java)

                if (product != null) {
                    MyResult.Success(product)
                } else {
                    MyResult.Failure(Exception("Document could not be converted to Product."))
                }
            } else {
                MyResult.Failure(Exception("No document found with productId $productId."))
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Exception thrown: ${e.message}", e)
            MyResult.Failure(e)
        }
        */
    }


}
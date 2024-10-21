package com.example.vanalaeropuerto.data.login

import android.content.ContentValues.TAG
import android.util.Log
import com.example.vanalaeropuerto.data.MyResult
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit

class UsersRepository {




    fun signIn(
        name: String?,
        surname: String?,
        cuil:String?,
        phoneNumber:String?,
        email:String?,
        gender:String?
    ) : MyResult<Unit> {

        return MyResult.Success(Unit)
    }



}
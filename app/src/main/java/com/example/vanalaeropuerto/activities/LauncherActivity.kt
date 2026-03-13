package com.example.vanalaeropuerto.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vanalaeropuerto.session.SessionState
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.session.UserRole
import com.example.vanalaeropuerto.viewmodels.LauncherViewModel
import com.google.firebase.auth.FirebaseAuth

class LauncherActivity : AppCompatActivity() {

    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeSession()
    }

    private fun observeSession() {

        sessionViewModel.session.observe(this) {

            goToLogin()

        }

    }

    private fun goToLogin() {

        startActivity(
            Intent(this, LoginActivity::class.java)
        )

        finish()

    }

}
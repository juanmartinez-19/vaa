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

    private lateinit var sessionViewModel: SessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionViewModel = ViewModelProvider(this)[SessionViewModel::class.java]

        observeSession()
    }

    private fun observeSession() {
        sessionViewModel.state.observe(this) { state ->
            when (state) {
                SessionState.Loading -> {
                    // splash opcional
                }

                SessionState.LoggedOut -> {
                    goToLogin()
                }

                is SessionState.LoggedIn -> {
                    // ⚠️ NO navegamos a Home / Empresa acá
                    goToLogin()
                }
            }
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

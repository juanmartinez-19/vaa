package com.example.vanalaeropuerto.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.session.UserRole
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint

class LoginActivity : AppCompatActivity() {

    private val sessionViewModel: SessionViewModel by viewModels()

    private val requesterRepository = RequesterRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity)

        observeSession()

        FirebaseApp.initializeApp(this)
    }

    private fun observeSession() {

        sessionViewModel.session.observe(this) { session ->

            when {

                session == null -> {
                    // usuario no logueado
                }

                else -> {
                    loadRoleAndNavigate(session.uid)
                }

            }

        }

    }

    private fun loadRoleAndNavigate(uid: String) {

        lifecycleScope.launch {

            when (val result = requesterRepository.getRequester(uid)) {

                is MyResult.Success -> {

                    navigateByRole(result.data?.getRequesterRole())

                    finish()

                }

                is MyResult.Failure -> {

                    sessionViewModel.logout()

                }

            }

        }

    }

    private fun navigateByRole(role: String?) {

        val intent = when (role) {

            UserRole.USER.toString() ->
                Intent(this, HomeActivity::class.java)

            UserRole.ADMIN.toString() ->
                Intent(this, EmpresaActivity::class.java)

            else ->
                Intent(this, HomeActivity::class.java)

        }

        startActivity(intent)

    }

}
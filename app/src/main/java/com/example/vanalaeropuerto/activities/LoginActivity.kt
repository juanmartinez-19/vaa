package com.example.vanalaeropuerto.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.activities.ui.theme.VanAlAeropuertoTheme
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.repositories.RequesterRepository
import com.example.vanalaeropuerto.session.LocalSessionStorage
import com.example.vanalaeropuerto.session.SessionManager
import com.example.vanalaeropuerto.session.SessionState
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.session.UserRole
import com.example.vanalaeropuerto.viewmodels.empresa.ConfirmedTripsViewModel
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionViewModel: SessionViewModel
    val getRequestersUseCase : RequesterRepository = RequesterRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        sessionViewModel = ViewModelProvider(this)[SessionViewModel::class.java]

        observeSession()


        FirebaseApp.initializeApp(this)
    }

    private fun observeSession() {
        sessionViewModel.state.observe(this) { state ->
            when (state) {
                is SessionState.LoggedIn -> {
                    loadRoleAndNavigate(state.uid)
                }

                SessionState.LoggedOut -> {
                    // Se queda en login
                }
                SessionState.Loading -> {
                    // opcional: progress
                }
            }
        }
    }
    private fun loadRoleAndNavigate(uid: String) {
        lifecycleScope.launch {
            when (val result = getRequestersUseCase.getRequester(uid)) {
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
            UserRole.USER.toString() -> Intent(this, HomeActivity::class.java)
            UserRole.ADMIN.toString() -> Intent(this, EmpresaActivity::class.java)
            else -> Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
    }




}
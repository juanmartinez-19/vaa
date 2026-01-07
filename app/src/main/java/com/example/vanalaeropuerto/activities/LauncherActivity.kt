package com.example.vanalaeropuerto.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.vanalaeropuerto.viewmodels.LauncherViewModel
import com.google.firebase.auth.FirebaseAuth

class LauncherActivity : AppCompatActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            goToLogin()
            return
        }

        observeState()
        viewModel.loadUser(currentUser.uid)
    }

    private fun observeState() {
        viewModel.launcherState.observe(this) { state ->
            when (state) {
                is LauncherViewModel.LauncherState.User -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                is LauncherViewModel.LauncherState.Admin -> {
                    startActivity(Intent(this, EmpresaActivity::class.java))
                    finish()
                }
                LauncherViewModel.LauncherState.Login -> {
                    goToLogin()
                }
                LauncherViewModel.LauncherState.Error -> {
                    FirebaseAuth.getInstance().signOut()
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


package com.example.vanalaeropuerto.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.user.UserSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    lateinit var requester: Requester

    private val userViewModel: UserSharedViewModel by viewModels()

    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home_activity)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment

        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        observeSession()
    }

    private fun observeSession() {

        sessionViewModel.session.observe(this) { session ->

            if (session == null) {

                startActivity(Intent(this, LoginActivity::class.java))
                finish()

            } else {

                // usar uid desde la sesión
                userViewModel.loadRequester(session.uid)

            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.nav_host_fragment_home)

        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
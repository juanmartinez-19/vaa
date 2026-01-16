package com.example.vanalaeropuerto.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.session.SessionManager
import com.example.vanalaeropuerto.session.SessionState
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.session.UserRole
import com.example.vanalaeropuerto.viewmodels.empresa.EditTripViewModel
import kotlinx.coroutines.launch

class EmpresaActivity : AppCompatActivity() {

    private lateinit var sessionViewModel: SessionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empresa_activity)
        sessionViewModel = ViewModelProvider(this)[SessionViewModel::class.java]

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home_empresa) as NavHostFragment
        val navController = navHostFragment.navController



        setupActionBarWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        sessionViewModel.state.observe(this) { state ->
            if (state !is SessionState.LoggedIn) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }





    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_home_empresa)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
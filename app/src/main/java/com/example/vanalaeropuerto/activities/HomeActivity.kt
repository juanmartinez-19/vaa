package com.example.vanalaeropuerto.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.login.RequesterRepository
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.viewmodels.login.AuthViewModel
import com.example.vanalaeropuerto.viewmodels.user.UserSharedViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    lateinit var requester: Requester
    private val userViewModel: UserSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        val navController = navHostFragment.navController

        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: run {
                finish()
                return
            }

        userViewModel.loadRequester(uid)

        setupActionBarWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_home)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}

package com.example.vanalaeropuerto.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            // Usuario ya logueado
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            // Usuario NO logueado
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish()
    }
}

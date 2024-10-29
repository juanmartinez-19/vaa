package com.example.vanalaeropuerto.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.fragments.empresa.AsignDriverFragmentDirections
import com.example.vanalaeropuerto.viewmodels.login.LoginViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginFragment : Fragment() {

    private lateinit var v : View


    private lateinit var viewModel: LoginViewModel

    private lateinit var btnRegister : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_login, container, false)

        btnRegister = v.findViewById(R.id.btnRegister)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        btnRegister.setOnClickListener {
            try {
                if (findNavController().currentDestination?.id == R.id.loginFragment) {
                    val action = LoginFragmentDirections.actionLoginFragmentToAuthPhoneFragment()
                    findNavController().navigate(action)
                } else {
                    Log.e("LoginFragment", "Navigation action failed")
                }
            } catch (e: IllegalArgumentException) {
                Log.e("LoginFragment", "Navigation action failed: ${e.message}")
            }
        }
    }

}
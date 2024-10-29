package com.example.vanalaeropuerto.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.login.AuthViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AuthPhoneFragment : Fragment() {

    private lateinit var v: View

    private lateinit var viewModel: AuthViewModel

    private lateinit var progressBar : ProgressBar

    private lateinit var etPhoneNumber : EditText

    private lateinit var btnContinuar : FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_auth_phone, container, false)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        // Levantar las variables de la pantalla
        btnContinuar = v.findViewById(R.id.btnContinuar)
        progressBar = v.findViewById(R.id.progressBarLoading)
        etPhoneNumber = v.findViewById(R.id.etPhoneNumber)

        return v
    }

    override fun onPause() {
        super.onPause()
        // Limpiar el ViewModel cuando el fragmento se detiene
        viewModel.clearData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    this.showLoading()
                }
                is ViewState.Failure -> {
                    this.showError()
                }
                is ViewState.Idle -> {
                    this.hideLoading()
                }
                is ViewState.Confirmed -> {
                    this.hideLoading()
                }
                is ViewState.InvalidParameters -> {
                    this.showInvalidParameters(viewState.message)
                }
                else -> {
                    this.showError()
                }
            }
        })

        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Success -> {
                    Snackbar.make(v, "Autenticación exitosa", Snackbar.LENGTH_SHORT).show()
                    try {
                        if (findNavController().currentDestination?.id == R.id.authCodeFragment) {
                            val action = AuthPhoneFragmentDirections.actionAuthPhoneFragmentToRegisterFragment(etPhoneNumber.text.toString())
                            findNavController().navigate(action)
                        } else {
                            Log.e("AuthCodeFragment", "Navigation action failed")
                        }
                    } catch (e: IllegalArgumentException) {
                        Log.e("AuthCodeFragment", "Navigation action failed: ${e.message}")
                    }
                }
                is AuthViewModel.AuthState.CodeSent -> {
                    // Código enviado al número de teléfono
                    try {
                        if (findNavController().currentDestination?.id == R.id.authPhoneFragment) {
                            val action = AuthPhoneFragmentDirections.actionAuthPhoneFragmentToAuthCodeFragment(state.verificationId,etPhoneNumber.text.toString(),state.message)
                            findNavController().navigate(action)
                        } else {
                            Log.e("AuthPhoneFragment", "Navigation action failed")
                        }
                    } catch (e: IllegalArgumentException) {
                        Log.e("AuthPhoneFragment", "Navigation action failed: ${e.message}")
                    }

                }
                is AuthViewModel.AuthState.Failure -> {
                    // Error en la verificación o autenticación
                    Snackbar.make(v, "Error: ${state.message}", Snackbar.LENGTH_SHORT).show()
                }
                is AuthViewModel.AuthState.Idle -> {
                    this.hideLoading()
                }
                else -> {
                    Snackbar.make(v, "error", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        btnContinuar.setOnClickListener {
            val phoneNumber = "+16505553434"

            viewModel.phoneAuth(phoneNumber,requireActivity())
        }

    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.ha_ocurrido_un_error), Snackbar.LENGTH_SHORT).show()
    }
    private fun showInvalidParameters(message : String) {
        progressBar.visibility = View.GONE
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show()
    }

}
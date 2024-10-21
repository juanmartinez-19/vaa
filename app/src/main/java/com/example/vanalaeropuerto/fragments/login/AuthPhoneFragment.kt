package com.example.vanalaeropuerto.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.login.AuthPhoneViewModel
import com.google.android.material.snackbar.Snackbar

class AuthPhoneFragment : Fragment() {

    private lateinit var v: View

    private lateinit var viewModel: AuthPhoneViewModel

    private lateinit var progressBar : ProgressBar

    private lateinit var etPhoneNumber : EditText
    private lateinit var btnContinuar : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_auth_phone, container, false)

        // Levantar las variables de la pantalla
        btnContinuar = v.findViewById(R.id.btnContinuar)
        progressBar = v.findViewById(R.id.progressBarLoading)
        etPhoneNumber = v.findViewById(R.id.etPhoneNumber)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthPhoneViewModel::class.java)

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
                    this.showInvalidParameters()
                }
                else -> {
                    this.showError()
                }
            }
        })

        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthPhoneViewModel.AuthState.Success -> {
                    // El usuario fue autenticado con éxito
                    Snackbar.make(v, "Autenticación exitosa", Snackbar.LENGTH_SHORT).show()
                    // Redirige a otra pantalla o acción
                }
                is AuthPhoneViewModel.AuthState.CodeSent -> {
                    // Código enviado al número de teléfono
                    Snackbar.make(v, "Código enviado", Snackbar.LENGTH_SHORT).show()

                    try {
                        if (findNavController().currentDestination?.id == R.id.authPhoneFragment) {
                            val action = AuthPhoneFragmentDirections.actionAuthPhoneFragmentToAuthCodeFragment(state.verificationId)
                            findNavController().navigate(action)
                        } else {
                            Log.e("AuthPhoneFragment", "Navigation action failed")
                        }
                    } catch (e: IllegalArgumentException) {
                        Log.e("AuthPhoneFragment", "Navigation action failed: ${e.message}")
                    }
                }
                is AuthPhoneViewModel.AuthState.Failure -> {
                    // Error en la verificación o autenticación
                    Snackbar.make(v, "Error: ${state.message}", Snackbar.LENGTH_SHORT).show()
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
    private fun showInvalidParameters() {
        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.invalid_parameters), Snackbar.LENGTH_SHORT).show()
    }

}
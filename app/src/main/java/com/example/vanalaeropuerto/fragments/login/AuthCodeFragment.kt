package com.example.vanalaeropuerto.fragments.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.activities.HomeActivity
import com.example.vanalaeropuerto.viewmodels.login.AuthViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AuthCodeFragment : Fragment() {

    private lateinit var v: View

    private lateinit var btnVerifyCode: FloatingActionButton
    private lateinit var etCode: EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var verificationId: String

    private lateinit var viewModel: AuthViewModel

    private lateinit var phoneNumber: String
    private lateinit var initMessage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_auth_code, container, false)

        btnVerifyCode = v.findViewById(R.id.btnVerifyCode)
        etCode = v.findViewById(R.id.etCode)
        progressBar = v.findViewById(R.id.progressBarVerificacion)

        phoneNumber = AuthCodeFragmentArgs.fromBundle(requireArguments()).phoneNumber
        verificationId = AuthCodeFragmentArgs.fromBundle(requireArguments()).verificationId
        initMessage = AuthCodeFragmentArgs.fromBundle(requireArguments()).message

        Snackbar.make(v, initMessage, Snackbar.LENGTH_SHORT).show()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        btnVerifyCode.setOnClickListener {
            val code = etCode.text.toString().trim()

            if (code.isNotEmpty() && code.length == 6) {
                // Mostrar la barra de progreso
                progressBar.visibility = View.VISIBLE

                // Llamar a la función para verificar el código
                viewModel.verifyCode(verificationId, code)
            } else {
                Snackbar.make(v, "Por favor ingrese el código de 6 dígitos", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.authState.observe(viewLifecycleOwner) { result ->
            progressBar.visibility = View.GONE
            when (result) {
                is AuthViewModel.AuthState.UserNeedsRegister -> {
                    try {
                        if (findNavController().currentDestination?.id == R.id.authCodeFragment) {

                            val action =
                                      AuthCodeFragmentDirections.actionAuthCodeFragmentToRegisterFragment(phoneNumber)
                            findNavController().navigate(action)

                        } else {
                            Log.e("AuthCodeFragment", "Navigation action failed")
                        }
                    } catch (e: IllegalArgumentException) {
                        Log.e("AuthCodeFragment", "Navigation action failed: ${e.message}")
                    }
                }

                is AuthViewModel.AuthState.UserExists -> {
                    this.goToUserHome()
                    /*
                    try {
                        if (findNavController().currentDestination?.id == R.id.authCodeFragment) {

                            val action =
                                AuthCodeFragmentDirections.actionAuthCodeFragmentToHomeActivity()
                            findNavController().navigate(action)

                        } else {
                            Log.e("AuthCodeFragment", "Navigation action failed")
                        }
                    } catch (e: IllegalArgumentException) {
                        Log.e("AuthCodeFragment", "Navigation action failed: ${e.message}")
                    }
                    */
                }

                is AuthViewModel.AuthState.Failure -> {
                    Snackbar.make(
                        v,
                        "El código ingresado no es válido. Por favor, verifica e intenta nuevamente.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                is AuthViewModel.AuthState.CodeSent -> {
                    // Código enviado al número de teléfono
                    Snackbar.make(v, "Código enviado", Snackbar.LENGTH_SHORT).show()
                }

                else -> {
                    Snackbar.make(
                        v,
                        "Ha ocurrido un error inesperado. Por favor, reinicie la aplicación y vuélvalo a intentar.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun goToUserHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // 🔴 mata LoginActivity
    }



}
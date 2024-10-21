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
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.fragments.empresa.AsignDriverFragmentArgs
import com.example.vanalaeropuerto.viewmodels.login.AuthCodeViewModel
import com.example.vanalaeropuerto.viewmodels.login.AuthPhoneViewModel
import com.google.android.material.snackbar.Snackbar

class AuthCodeFragment : Fragment() {

    private lateinit var v : View

    private lateinit var btnVerifyCode : Button
    private lateinit var etCode : EditText
    private lateinit var progressBar : ProgressBar

    private lateinit var verificationId : String

    private lateinit var viewModel: AuthCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_auth_code, container, false)

        btnVerifyCode = v.findViewById(R.id.btnVerifyCode)
        etCode = v.findViewById(R.id.etCode)
        progressBar = v.findViewById(R.id.progressBarVerificacion)

        verificationId = AuthCodeFragmentArgs.fromBundle(requireArguments()).verificationId


        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthCodeViewModel::class.java)

        btnVerifyCode.setOnClickListener {
            val code = etCode.text.toString().trim()

            if (code.isNotEmpty() && code.length == 6) {
                // Mostrar la barra de progreso
                progressBar.visibility = View.VISIBLE

                // Llamar a la función para verificar el código
                viewModel.verifyCode(verificationId, code)
            } else {
                Snackbar.make(v, "Por favor ingrese el código de 6 dígitos", Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            progressBar.visibility = View.GONE
            when (result) {
                is AuthCodeViewModel.AuthResult.Success -> {
                    // El usuario fue autenticado con éxito
                    Snackbar.make(v, "Autenticación exitosa", Snackbar.LENGTH_SHORT).show()
                }
                is AuthCodeViewModel.AuthResult.Failure -> {
                    // Código enviado al número de teléfono
                    Snackbar.make(v, "error", Snackbar.LENGTH_SHORT).show()

                }
            }
        }

    }

}
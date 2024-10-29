package com.example.vanalaeropuerto.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Message
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.login.RegisterViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

   private lateinit var v : View

   private lateinit var btnContinue : FloatingActionButton
    private lateinit var progressBar : ProgressBar

    private lateinit var etUserName : EditText
    private lateinit var etUserSurname : EditText
    private lateinit var etUserCuil : EditText
    private lateinit var etUserPassword : EditText
    private lateinit var etRepeatPassword : EditText
    private lateinit var btnTogglePasswordVisibility : ImageButton
    private lateinit var btnToggleRepeatPasswordVisibility : ImageButton

    private lateinit var phoneNumber : String

    var isPasswordVisible = false
    var isRepeatPasswordVisible = false

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_register, container, false)

        etUserName = v.findViewById(R.id.etUserName)
        etUserSurname = v.findViewById(R.id.etUserSurname)
        etUserPassword = v.findViewById(R.id.etUserPassword)
        etRepeatPassword = v.findViewById(R.id.etRepeatPassword)
        etUserCuil = v.findViewById(R.id.etUserCuil)
        btnContinue = v.findViewById(R.id.btnContinue)
        progressBar = v.findViewById(R.id.progressBarLoading)
        btnTogglePasswordVisibility = v.findViewById(R.id.btnTogglePasswordVisibility)
        btnToggleRepeatPasswordVisibility  = v.findViewById(R.id.btnToggleRepeatPasswordVisibility )

        phoneNumber = RegisterFragmentArgs.fromBundle(requireArguments()).phoneNumber

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    this.showLoading()
                }
                is ViewState.Confirmed -> {
                    this.showConfirmed()
                }
                is ViewState.InvalidParameters -> {
                    this.showInvalidParameters(viewState.message)
                }

                else -> {
                    this.showError()
                }
            }
        })

        btnContinue.setOnClickListener {
            viewModel.register(etUserName.text.toString(),etUserSurname.text.toString(),etUserPassword.text.toString(),etRepeatPassword.text.toString(),etUserCuil.text.toString(),phoneNumber)

        }

        btnTogglePasswordVisibility.setOnClickListener {
            if (isPasswordVisible) {
                etUserPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnTogglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off) // Icono de ocultar
            } else {
                etUserPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnTogglePasswordVisibility.setImageResource(R.drawable.ic_visibility_24) // Icono de mostrar
            }
            isPasswordVisible = !isPasswordVisible
            etUserPassword.setSelection(etUserPassword.text.length) // Mantener el cursor al final del texto
        }

        btnToggleRepeatPasswordVisibility.setOnClickListener {
            if (isRepeatPasswordVisible) {
                etRepeatPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnToggleRepeatPasswordVisibility.setImageResource(R.drawable.ic_visibility_off) // Icono de ocultar
            } else {
                etRepeatPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnToggleRepeatPasswordVisibility.setImageResource(R.drawable.ic_visibility_24) // Icono de mostrar
            }
            isRepeatPasswordVisible = !isRepeatPasswordVisible
            etRepeatPassword.setSelection(etRepeatPassword.text.length) // Mantener el cursor al final del texto
        }

    }

    private fun showError() {
        progressBar.visibility = View.GONE
        Snackbar.make(v,"Unexpected error", Snackbar.LENGTH_SHORT).show()
    }
    private fun showConfirmed() {
        progressBar.visibility = View.GONE
        try {
            if (findNavController().currentDestination?.id == R.id.registerFragment) {
                val action = RegisterFragmentDirections.actionRegisterFragmentToHomeActivity()
                findNavController().navigate(action)
            } else {
                Log.e("RegisterFragment", "Navigation action failed")
            }
        } catch (e: IllegalArgumentException) {
            Log.e("RegisterFragment", "Navigation action failed: ${e.message}")
        }

    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }
    private fun showInvalidParameters(message: String) {
        progressBar.visibility = View.GONE
        Snackbar.make(v,message , Snackbar.LENGTH_SHORT).show()
    }

}
package com.example.vanalaeropuerto.fragments.login

import android.content.Intent
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
import com.example.vanalaeropuerto.activities.HomeActivity
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.viewmodels.login.RegisterViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.UUID

class RegisterFragment : Fragment() {

   private lateinit var v : View

   private lateinit var btnContinue : FloatingActionButton
    private lateinit var progressBar : ProgressBar

    //Pantalla
    private lateinit var etUserName : EditText
    private lateinit var etUserSurname : EditText
    private lateinit var etUserCuil : EditText

    private lateinit var userName : String
    private lateinit var userSurname : String
    private lateinit var userCuil : String

    private lateinit var phoneNumber : String

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_register, container, false)

        etUserName = v.findViewById(R.id.etUserName)
        etUserSurname = v.findViewById(R.id.etUserSurname)
        etUserCuil = v.findViewById(R.id.etUserCuil)
        btnContinue = v.findViewById(R.id.btnContinue)
        progressBar = v.findViewById(R.id.progressBarLoading)

        phoneNumber = RegisterFragmentArgs.fromBundle(requireArguments()).phoneNumber

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        btnContinue.setOnClickListener {

            userName = etUserName.text.toString()
            userSurname = etUserSurname.text.toString()
            userCuil = etUserCuil.text.toString()

            viewModel.validateData(userName,userSurname,userCuil)

            viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
                when (viewState) {
                    is ViewState.Loading -> {
                        this.showLoading()
                    }
                    is ViewState.ValidParameters -> {
                        viewModel.signIn(userName,userSurname,phoneNumber,userCuil)
                    }
                    is ViewState.InvalidParameters -> {
                        this.showInvalidParameters(viewState.message)
                    }
                    is ViewState.Confirmed -> {
                        this.onRegisterSuccess()
                    }

                    else -> {
                        this.showError()
                    }
                }
            })

        }

    }
/*
    private fun navigate(){
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
*/

    private fun onRegisterSuccess() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


    private fun showError() {
        progressBar.visibility = View.GONE
        Snackbar.make(v,"Unexpected error", Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }
    private fun showInvalidParameters(message: String) {
        progressBar.visibility = View.GONE
        Snackbar.make(v,message , Snackbar.LENGTH_SHORT).show()
    }

}
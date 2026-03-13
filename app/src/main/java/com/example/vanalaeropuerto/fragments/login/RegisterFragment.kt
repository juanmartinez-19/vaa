package com.example.vanalaeropuerto.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.login.RegisterViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

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
    private lateinit var sessionViewModel: SessionViewModel

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
        sessionViewModel = ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        observeViewModel()

        btnContinue.setOnClickListener {
            val userName = etUserName.text.toString()
            val userSurname = etUserSurname.text.toString()
            val userCuil = etUserCuil.text.toString()

            viewModel.validateData(userName, userSurname, userCuil)
        }
    }

    private fun observeViewModel() {

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->

            when (viewState) {

                is ViewState.Loading -> showLoading()

                is ViewState.ValidParameters -> {
                    viewModel.signIn(
                        etUserName.text.toString(),
                        etUserSurname.text.toString(),
                        phoneNumber,
                        etUserCuil.text.toString()
                    )
                }

                is ViewState.InvalidParameters -> {
                    showInvalidParameters(viewState.message)
                }

                is ViewState.Confirmed -> {
                    onRegisterSuccess()
                }

                else -> showError()
            }

        }
    }

    private fun onRegisterSuccess() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        sessionViewModel.startSession(uid)
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
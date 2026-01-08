package com.example.vanalaeropuerto.fragments.user

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.user.IngresoDatosViewModel
import com.example.vanalaeropuerto.viewmodels.user.UserSharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class IngresoDatosFragment : Fragment() {

    private lateinit var v : View
    private lateinit var viewModel: IngresoDatosViewModel

    private lateinit var progressBar : ProgressBar
    private val userViewModel: UserSharedViewModel by activityViewModels()

    private lateinit var rbThirdParty : RadioButton
    private lateinit var rbMyself : RadioButton
    private lateinit var thirdPartyFields : LinearLayout
    private lateinit var fabConfirmForm : FloatingActionButton
    private lateinit var tvUserName : TextView
    private lateinit var tvUserSurname : TextView
    private lateinit var tvUserPhoneNumber : TextView
    private lateinit var tvUserCuil : TextView
    private lateinit var etThirdPartyName : EditText
    private lateinit var etThirdPartySurname : EditText
    private lateinit var etThirdPartyPhoneNumber : EditText
    private lateinit var etThirdPartyCuil : EditText

    private lateinit var userName : String
    private lateinit var userSurname : String
    private lateinit var userPhoneNumber : String
    private lateinit var userCuil : String

    private lateinit var thirdPartyName : String
    private lateinit var thirdPartySurname : String
    private lateinit var thirdPartyPhoneNumber : String
    private lateinit var thirdPartyCuil : String

    private var radiobuttonChecked : Boolean = false
    private var isForUser : Boolean = false
    private var validateCounter : Int = 0

    private lateinit var tripRequester : TripRequester

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_ingreso_datos, container, false)
        viewModel = ViewModelProvider(this).get(IngresoDatosViewModel::class.java)

        progressBar = v.findViewById(R.id.progressBarLoading)
        rbThirdParty = v.findViewById(R.id.rb_third_party)
        rbMyself = v.findViewById(R.id.rb_myself)
        thirdPartyFields = v.findViewById(R.id.third_party_fields)
        fabConfirmForm = v.findViewById(R.id.fab_confirm_form)

        tvUserName = v.findViewById(R.id.tv_user_name)
        tvUserSurname = v.findViewById(R.id.tv_user_surname)
        tvUserPhoneNumber = v.findViewById(R.id.tv_user_phone)
        tvUserCuil = v.findViewById(R.id.tv_user_cuil)

        etThirdPartyName = v.findViewById(R.id.et_third_party_name)
        etThirdPartySurname = v.findViewById(R.id.et_third_party_surname)
        etThirdPartyPhoneNumber = v.findViewById(R.id.et_third_party_phone_number)
        etThirdPartyCuil = v.findViewById(R.id.et_third_party_cuil)

        tripRequester = IngresoDatosFragmentArgs.fromBundle(requireArguments()).tripRequester

        userViewModel.requester.observe(viewLifecycleOwner) { requester ->
            requester ?: return@observe

            tvUserName.text = requester.getRequesterName().toString()
            tvUserSurname.text = requester.getRequesterSurname().toString()
            tvUserPhoneNumber.text = requester.getRequesterPhoneNumber().toString()
            tvUserCuil.text = requester.getRequesterCuil().toString()
        }

        return v
    }

    override fun onPause() {
        super.onPause()
        // Limpiar el ViewModel cuando el fragmento se detiene
        viewModel.clearData()
        this.validateCounter = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionViewModel =
            ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        sessionViewModel.currentRequester.observe(viewLifecycleOwner) { session ->
            if (session == null || session.getRequesterRole() != Roles.USER) {
                requireActivity().finish()
            }
        }

        rbThirdParty.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                thirdPartyFields.visibility = View.VISIBLE
                radiobuttonChecked = true
                isForUser = false
            }
        }

        rbMyself.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                thirdPartyFields.visibility = View.GONE
                radiobuttonChecked = true
                isForUser = true
            }
        }

        fabConfirmForm.setOnClickListener {
            userName = tvUserName.text.toString()
            userSurname = tvUserSurname.text.toString()
            userPhoneNumber = tvUserPhoneNumber.text.toString()
            userCuil = tvUserCuil.text.toString()

            thirdPartyName = etThirdPartyName.text.toString()
            thirdPartySurname = etThirdPartySurname.text.toString()
            thirdPartyPhoneNumber = etThirdPartyPhoneNumber.text.toString()
            thirdPartyCuil = etThirdPartyCuil.text.toString()

            viewModel.validateUserData(radiobuttonChecked,userName, userSurname, userPhoneNumber, userCuil)

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
                        validateCounter += 1

                        if ((isForUser && validateCounter==1)||(!isForUser && validateCounter==2)) {
                            this.showConfirmed()
                        } else if (!isForUser && validateCounter==1) {
                            viewModel.validateUserData(
                                radiobuttonChecked,
                                thirdPartyName,
                                thirdPartySurname,
                                thirdPartyPhoneNumber,
                                thirdPartyCuil
                            )
                        }
                    }
                    is ViewState.InvalidParameters -> {
                        this.showInvalidParameters(viewState.message)
                    }
                    else -> {
                        this.showError()
                    }
                }
            })

        }

    }

    private fun navigate() {
        try {
            if (findNavController().currentDestination?.id == R.id.ingresoDatosFragment) {
                val action = IngresoDatosFragmentDirections.actionIngresoDatosFragmentToConfirmTripFragment(tripRequester)
                findNavController().navigate(action)
            }
        } catch (e: IllegalArgumentException) {
            Log.e("IngresoDatosFragment", "Navigation action failed: ${e.message}")
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

    private fun showConfirmed() {
        this.navigate()
    }


}
package com.example.vanalaeropuerto.fragments

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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.IngresoDatosViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class IngresoDatosFragment : Fragment() {

    private lateinit var v : View
    private lateinit var viewModel: IngresoDatosViewModel

    private lateinit var progressBar : ProgressBar

    private lateinit var rbThirdParty : RadioButton
    private lateinit var rbMyself : RadioButton
    private lateinit var thirdPartyFields : LinearLayout
    private lateinit var fabConfirmForm : FloatingActionButton
    private lateinit var etUserName : EditText
    private lateinit var etUserSurname : EditText
    private lateinit var etUserPhoneNumber : EditText
    private lateinit var etUserCuil : EditText
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_ingreso_datos, container, false)

        progressBar = v.findViewById(R.id.progressBarLoading)
        rbThirdParty = v.findViewById(R.id.rb_third_party)
        rbMyself = v.findViewById(R.id.rb_myself)
        thirdPartyFields = v.findViewById(R.id.third_party_fields)
        fabConfirmForm = v.findViewById(R.id.fab_confirm_form)

        etUserName = v.findViewById(R.id.et_user_name)
        etUserSurname = v.findViewById(R.id.et_user_surname)
        etUserPhoneNumber = v.findViewById(R.id.et_user_phone_number)
        etUserCuil = v.findViewById(R.id.et_user_cuil)

        etThirdPartyName = v.findViewById(R.id.et_third_party_name)
        etThirdPartySurname = v.findViewById(R.id.et_third_party_surname)
        etThirdPartyPhoneNumber = v.findViewById(R.id.et_third_party_phone_number)
        etThirdPartyCuil = v.findViewById(R.id.et_third_party_cuil)


        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(IngresoDatosViewModel::class.java)

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
            userName = etUserName.text.toString()
            userSurname = etUserSurname.text.toString()
            userPhoneNumber = etUserPhoneNumber.text.toString()
            userCuil = etUserCuil.text.toString()

            thirdPartyName = etThirdPartyName.text.toString()
            thirdPartySurname = etThirdPartySurname.text.toString()
            thirdPartyPhoneNumber = etThirdPartyPhoneNumber.text.toString()
            thirdPartyCuil = etThirdPartyCuil.text.toString()

            viewModel.validateUserData(radiobuttonChecked,userName, userSurname, userPhoneNumber, userCuil)

            if (!isForUser) {
                viewModel.validateUserData(radiobuttonChecked,thirdPartyName, thirdPartySurname, thirdPartyPhoneNumber, thirdPartyCuil)
            }

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
                        this.showConfirmed()
                    }
                    is ViewState.InvalidParameters -> {
                        this.showInvalidParameters()
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
                val action = IngresoDatosFragmentDirections.actionIngresoDatosFragmentToHomeFragment()
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
    private fun showInvalidParameters() {
        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.invalid_parameters), Snackbar.LENGTH_SHORT).show()
    }

    private fun showConfirmed() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Solicitud confirmada")
        builder.setMessage("Su solicitud ha sido procesada correctamente. Un agente se comunicará con usted en breve.")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
            this.navigate()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
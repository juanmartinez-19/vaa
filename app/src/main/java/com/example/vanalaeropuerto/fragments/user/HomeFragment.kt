package com.example.vanalaeropuerto.fragments.user

import android.app.DatePickerDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.activities.LoginActivity
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.user.HomeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var v: View

    private lateinit var etOriginAddress: AutoCompleteTextView
    private lateinit var etDestinationAddress: AutoCompleteTextView
    private lateinit var etLuggage: EditText
    private lateinit var etDepartureDate: EditText

    private lateinit var btnSearch: FloatingActionButton
    private lateinit var btnSignOut: AppCompatImageButton

    private lateinit var tvAdultCount: TextView
    private lateinit var tvChildCount: TextView
    private lateinit var tvBabyCount: TextView

    private lateinit var progressBar: ProgressBar
    private lateinit var textViewTitle: TextView

    // 🔒 NO NULLABLES
    private lateinit var originAddress: String
    private lateinit var destinationAddress: String

    private var luggage: Float = 0f
    private var selectedDateInMillis: Long = 0L

    private lateinit var viewModel: HomeViewModel

    private val suggestions = arrayOf(
        "Aeropuerto Internacional de Ezeiza (EZE)",
        "Aeropuerto Jorge Newbery (AEP)"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        etOriginAddress = v.findViewById(R.id.etDireccionOrigen)
        etDestinationAddress = v.findViewById(R.id.etDireccionDestino)
        etLuggage = v.findViewById(R.id.etEquipaje)
        etDepartureDate = v.findViewById(R.id.etDepartureDate)

        btnSearch = v.findViewById(R.id.btnBuscar)
        btnSignOut = v.findViewById(R.id.btnSignOut)

        tvAdultCount = v.findViewById(R.id.tvAdultCount)
        tvChildCount = v.findViewById(R.id.tvChildCount)
        tvBabyCount = v.findViewById(R.id.tvBabyCount)

        progressBar = v.findViewById(R.id.progressBarLoading)
        textViewTitle = v.findViewById(R.id.tvTitle)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            suggestions
        )

        etOriginAddress.setAdapter(adapter)
        etDestinationAddress.setAdapter(adapter)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val sessionViewModel =
            ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        sessionViewModel.currentRequester.observe(viewLifecycleOwner) { session ->
            if (session == null || session.getRequesterRole() != Roles.USER) {
                requireActivity().finish()
            }
        }

        btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(
                Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
        }

        etDepartureDate.setOnClickListener { showDatePickerDialog() }

        btnSearch.setOnClickListener {
            collectInputs()
            observeViewState()
        }
    }

    private fun collectInputs() {
        originAddress = etOriginAddress.text.toString()
        destinationAddress = etDestinationAddress.text.toString()

        luggage = etLuggage.text.toString().toFloatOrNull() ?: 0f

        val adults = viewModel.adultCount.value ?: 0
        val children = viewModel.childCount.value ?: 0
        val babies = viewModel.babyCount.value ?: 0

        viewModel.validarDatos(
            originAddress,
            destinationAddress,
            luggage,
            adults,
            children,
            babies,
            selectedDateInMillis,
            emptyList()
        )
    }

    private fun observeViewState() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.Loading -> showLoading()
                is ViewState.InvalidParameters -> showInvalidParameters(state.message)
                is ViewState.Idle -> {
                    hideLoading()
                    navigate()
                }
                else -> showError()
            }
        })
    }

    private fun navigate() {
        val adults = viewModel.adultCount.value ?: 0
        val children = viewModel.childCount.value ?: 0
        val babies = viewModel.babyCount.value ?: 0

        if (selectedDateInMillis <= 0L) {
            showInvalidParameters("Fecha inválida")
            return
        }

        val action = HomeFragmentDirections.actionHomeFragmentToVehiculosFragment(
            luggage = luggage,
            direccionOrigen = originAddress,
            direccionDestino = destinationAddress,
            adults = adults,
            children = children,
            babies = babies,
            fechaSalida = selectedDateInMillis
        )

        findNavController().navigate(action)

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, day, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                selectedDateInMillis = selectedCalendar.timeInMillis
                etDepartureDate.setText("${day}/${month + 1}/${year}")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showError() {
        Snackbar.make(v, getString(R.string.ha_ocurrido_un_error), Snackbar.LENGTH_SHORT).show()
    }

    private fun showInvalidParameters(message: String) {
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show()
    }
}

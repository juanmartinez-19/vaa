package com.example.vanalaeropuerto.fragments.empresa

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.viewmodels.empresa.EditTripViewModel
import java.util.Calendar

class EditTripFragment : Fragment() {

    private lateinit var v : View

    private lateinit var departureDateEditText: EditText
    private lateinit var originAddressEditText: EditText
    private lateinit var destinationAddressEditText: EditText
    private lateinit var priceEditText: EditText
    private var selectedDateInMillis: Long = 0

    private lateinit var pendingTrip : TripRequester

    private lateinit var viewModel: EditTripViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v=  inflater.inflate(R.layout.fragment_edit_trip, container, false)

        pendingTrip = EditTripFragmentArgs.fromBundle(requireArguments()).pendingTrip
        departureDateEditText = v.findViewById(R.id.etDepartureDate)
        originAddressEditText = v.findViewById(R.id.etDireccionOrigen)
        destinationAddressEditText = v.findViewById(R.id.etDireccionDestino)
        priceEditText = v.findViewById(R.id.etPrice)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditTripViewModel::class.java)

        departureDateEditText.setOnClickListener {
            this.showDatePickerDialog()
        }

        pendingTrip.getTrip()?.let { trip ->
            departureDateEditText.setText((trip.getDate() ?: ""))
            originAddressEditText.setText(trip.getOriginAddress() ?: "")
            destinationAddressEditText.setText(trip.getDestinationAddress() ?: "")
            priceEditText.setText(trip.getPrice()?.toString() ?: "0.00")
        }

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            selectedDateInMillis = selectedCalendar.timeInMillis

            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            departureDateEditText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

}
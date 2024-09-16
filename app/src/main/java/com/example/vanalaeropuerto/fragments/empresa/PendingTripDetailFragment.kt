package com.example.vanalaeropuerto.fragments.empresa

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.viewmodels.empresa.PendingTripDetailViewModel

class PendingTripDetailFragment : Fragment() {

    private lateinit var v : View

    private lateinit var viewModel: PendingTripDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pending_trip_detail, container, false)

        return v
    }


}
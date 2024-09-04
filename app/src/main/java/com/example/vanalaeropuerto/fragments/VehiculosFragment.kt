package com.example.vanalaeropuerto.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.viewmodels.VehiculosViewModel

class VehiculosFragment : Fragment() {

    companion object {
        fun newInstance() = VehiculosFragment()
    }

    private lateinit var viewModel: VehiculosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehiculos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VehiculosViewModel::class.java)

    }

}
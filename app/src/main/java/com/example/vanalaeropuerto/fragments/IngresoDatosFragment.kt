package com.example.vanalaeropuerto.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.viewmodels.IngresoDatosViewModel

class IngresoDatosFragment : Fragment() {

    private lateinit var v : View

    private lateinit var rbThirdParty : RadioButton
    private lateinit var rbMyself : RadioButton
    private lateinit var thirdPartyFields : LinearLayout


    private lateinit var viewModel: IngresoDatosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_ingreso_datos, container, false)

        rbThirdParty = v.findViewById(R.id.rb_third_party)
        rbMyself = v.findViewById(R.id.rb_myself)
        thirdPartyFields = v.findViewById(R.id.third_party_fields)


        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(IngresoDatosViewModel::class.java)

        rbThirdParty.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                thirdPartyFields.visibility = View.VISIBLE
            }
        }

        rbMyself.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                thirdPartyFields.visibility = View.GONE
            }
        }

    }

}
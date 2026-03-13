package com.example.vanalaeropuerto.fragments.empresa

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.activities.LoginActivity
import com.example.vanalaeropuerto.adapters.empresa.ViewPagerAdapter
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.ConfirmedTripsViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.HomeEmpresaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeEmpresaFragment : Fragment() {

    private lateinit var v : View
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout
    private val homeViewModel: HomeEmpresaViewModel by viewModels()

    private lateinit var fabSignOut : FloatingActionButton
    private lateinit var sessionViewModel: SessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home_empresa, container, false)

        viewPager = v.findViewById(R.id.view_pager)
        tabLayout = v.findViewById(R.id.tab_layout)
        fabSignOut = v.findViewById(R.id.fabSignOut)

        return v
    }

    override fun onStart() {
        super.onStart()
        sessionViewModel = ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                homeViewModel.selectedTab = position
            }
        })


        viewPager.adapter = ViewPagerAdapter(requireActivity())

        fabSignOut.setOnClickListener {
            sessionViewModel.logout()
        }

        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = "Historial"
                1 -> tab.text = "Solicitados"
                2 -> tab.text = "Confirmados"

                else -> tab.text = "undefined"
            }
        }).attach()

        viewPager.setCurrentItem(homeViewModel.selectedTab, false)

    }


}
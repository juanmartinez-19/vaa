package com.example.vanalaeropuerto.fragments.empresa

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.adapters.empresa.TripsAdapter
import com.example.vanalaeropuerto.adapters.empresa.ViewPagerAdapter
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.empresa.HomeEmpresaViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeEmpresaFragment : Fragment() {

    private lateinit var v : View
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout

    private lateinit var viewModel: HomeEmpresaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home_empresa, container, false)

        viewPager = v.findViewById(R.id.view_pager)
        tabLayout = v.findViewById(R.id.tab_layout)

        return v
    }

    override fun onStart() {
        super.onStart()

        viewModel = ViewModelProvider(this).get(HomeEmpresaViewModel::class.java)

        viewPager.adapter = ViewPagerAdapter(requireActivity())

        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = "Historial"
                1 -> tab.text = "Solicitados"
                2 -> tab.text = "Confirmados"

                else -> tab.text = "undefined"
            }
        }).attach()

        viewPager.setCurrentItem(1, false) // Establece la segunda pestaña como la inicial (sin animación)

    }


}
package com.example.vanalaeropuerto.adapters.empresa

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vanalaeropuerto.fragments.empresa.ConfirmedTripsFragment
import com.example.vanalaeropuerto.fragments.empresa.PendingTripsFragment
import com.example.vanalaeropuerto.fragments.empresa.TripsHistoryFragment

class ViewPagerAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity)
 {

    override fun createFragment(position:Int) : Fragment {
        return when (position) {
            0 -> TripsHistoryFragment()
            1 -> PendingTripsFragment()
            2 -> ConfirmedTripsFragment()

            else -> PendingTripsFragment()
        }
    }

     override fun getItemCount(): Int {
         return 3
     }


}
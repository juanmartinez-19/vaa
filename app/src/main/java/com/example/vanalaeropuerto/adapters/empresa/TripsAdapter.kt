package com.example.vanalaeropuerto.adapters.empresa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.entities.Trip

class TripsAdapter(
    var trips : MutableList<Trip>
) : RecyclerView.Adapter<TripsAdapter.TripHolder>() {
    class TripHolder(view : View) : RecyclerView.ViewHolder(view)
    {

        private var v : View
        init {
            this.v = view
        }

        fun setTripDate(date : String)  {
            val txtDate : TextView = v.findViewById(R.id.tvDate)
            txtDate.text = date
        }

        fun setTripOriginAddress(originAddress : String)  {
            val txtOriginAddress : TextView = v.findViewById(R.id.tvOriginAddress)
            txtOriginAddress.text = originAddress
        }

        fun setTripDestinationAddress(destinationAddress : String)  {
            val txtDestinationAddress : TextView = v.findViewById(R.id.tvDestinationAddress)
            txtDestinationAddress.text = destinationAddress
        }

        fun setTripLuggageKg(luggageKg : Float)  {
            val txtLuggageKg : TextView = v.findViewById(R.id.tvLuggageKg)
            txtLuggageKg.text = luggageKg.toString()
        }

        fun setTripAdults(adults : Int)  {
            val txtAdults : TextView = v.findViewById(R.id.tvAdults)
            txtAdults.text = adults.toString()
        }

        fun setTripBabies(babies : Int)  {
            val txtBabies : TextView = v.findViewById(R.id.tvBabies)
            txtBabies.text = babies.toString()
        }

        fun setTripChildren(children : Int)  {
            val txtChildren : TextView = v.findViewById(R.id.tvChildren)
            txtChildren.text = children.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip,parent,false)
        return (TripHolder(view))
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    fun getSelectedProduct(position: Int): Trip {
        return trips[position]
    }

    fun submitList(newTrips: MutableList<Trip>) {
        trips.clear()
        trips.addAll(newTrips)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TripHolder, position: Int) {
        trips[position].getDate()?.let { holder.setTripDate(it) }
        trips[position].getOriginAddress()?.let { holder.setTripOriginAddress(it) }
        trips[position].getDestinationAddress()?.let { holder.setTripDestinationAddress(it) }
        trips[position].getAdults()?.let { holder.setTripAdults(it) }
        trips[position].getChildren()?.let { holder.setTripChildren(it) }
        trips[position].getBabies()?.let { holder.setTripBabies(it) }
        trips[position].getLuggageKg()?.let { holder.setTripLuggageKg(it) }
    }

}
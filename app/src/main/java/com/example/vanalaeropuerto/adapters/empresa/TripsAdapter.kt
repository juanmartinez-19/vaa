package com.example.vanalaeropuerto.adapters.empresa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.entities.TripRequester
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TripsAdapter(
    private var trips : MutableList<TripRequester>,
    var onClick : (Int) -> Unit
) : RecyclerView.Adapter<TripsAdapter.TripHolder>() {
    class TripHolder(view : View) : RecyclerView.ViewHolder(view)
    {

        private var v : View
        init {
            this.v = view
        }

        fun setPassengerName (name : String)  {
            val txtName : TextView = v.findViewById(R.id.tvDriverName)
            txtName.text = name
        }

        fun setPassengerPhoneNumber (phoneNumber : String)  {
            val txtPhoneNumber : TextView = v.findViewById(R.id.tvPassengerPhone)
            txtPhoneNumber.text = phoneNumber
        }

        fun setTripDate(dateMillis: Long) {
            val txtDate: TextView = v.findViewById(R.id.tvDate)

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            txtDate.text = sdf.format(Date(dateMillis))
        }


        fun setTripOriginAddress(originAddress : String)  {
            val txtOriginAddress : TextView = v.findViewById(R.id.tvOriginAddress)
            txtOriginAddress.text = originAddress
        }

        fun setTripDestinationAddress(destinationAddress : String)  {
            val txtDestinationAddress : TextView = v.findViewById(R.id.tvDestinationAddress)
            txtDestinationAddress.text = destinationAddress
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip,parent,false)
        return (TripHolder(view))
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    fun getSelectedProduct(position: Int): TripRequester {
        return trips[position]
    }

    fun submitList(newTrips: MutableList<TripRequester>) {
        trips.clear()
        trips.addAll(newTrips)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TripHolder, position: Int) {
        trips[position].getTrip()?.getDate()?.let { holder.setTripDate(it) }
        trips[position].getTrip()?.getOriginAddress()?.let { holder.setTripOriginAddress(it) }
        trips[position].getTrip()?.getDestinationAddress()?.let { holder.setTripDestinationAddress(it) }
        trips[position].getRequester()?.getRequesterPhoneNumber()?.let { holder.setPassengerPhoneNumber(it) }

        val name = trips[position].getRequester()?.getRequesterName() ?: ""
        val surname = trips[position].getRequester()?.getRequesterSurname() ?: ""

        val fullName = if (surname.isNotEmpty() && name.isNotEmpty()) {
            "$surname, $name"
        } else {
            "$name$surname"
        }

        holder.setPassengerName(fullName)

        holder.itemView.setOnClickListener() {
            onClick(position)
        }


    }

}
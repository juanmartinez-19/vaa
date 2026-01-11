package com.example.vanalaeropuerto.adapters.empresa

import TripItemUI
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.core.TripState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TripsAdapter(
    private var trips : MutableList<TripItemUI>,
    var onClick : (Int) -> Unit
) : RecyclerView.Adapter<TripsAdapter.TripHolder>() {
    class TripHolder(view : View) : RecyclerView.ViewHolder(view)
    {

        private var v : View
        init {
            this.v = view
        }

        private val tvDriverLabel: TextView = v.findViewById(R.id.tvDriverNameLabel)
        private val tvDriverName: TextView = v.findViewById(R.id.tvDriverNameValue)
        private val txtName : TextView = v.findViewById(R.id.tvRequesterName)
        private val txtPhoneNumber : TextView = v.findViewById(R.id.tvPassengerPhone)
        private val txtDate: TextView = v.findViewById(R.id.tvDate)
        private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        private val txtOriginAddress : TextView = v.findViewById(R.id.tvOriginAddress)
        private val txtDestinationAddress : TextView = v.findViewById(R.id.tvDestinationAddress)
        private val card: CardView = v.findViewById(R.id.cardViewTrip)

        @SuppressLint("SetTextI18n")
        fun bind(item: TripItemUI) {
            // datos comunes
            setPassengerName("${item.getRequester().getRequesterSurname()}, ${item.getRequester().getRequesterName()}")
            item.getTrip().getDate()?.let { setTripDate(it) }
            item.getTrip().getOriginAddress()?.let { setTripOriginAddress(it) }
            item.getTrip().getDestinationAddress()?.let { setTripDestinationAddress(it) }
            item.getRequester().getRequesterPhoneNumber()?.let {setPassengerPhoneNumber(it)}

            this.setTripState(item.getTrip().getState())

            // 👇 SOLO si hay driver
            if (item.getDriver() != null) {
                tvDriverLabel.visibility = View.VISIBLE
                tvDriverName.visibility = View.VISIBLE
                tvDriverName.text =  "${item.getDriver()!!.getDriverSurname()}, ${item.getDriver()?.getDriverName()}"
            } else {
                tvDriverLabel.visibility = View.GONE
                tvDriverName.visibility = View.GONE
            }
        }

        private fun setPassengerName (name : String)  {
            txtName.text = name
        }

        fun setPassengerPhoneNumber (phoneNumber : String)  {
            txtPhoneNumber.text = phoneNumber
        }

        private fun setTripDate(dateMillis: Long) {
            txtDate.text = sdf.format(Date(dateMillis))
        }


        private fun setTripOriginAddress(originAddress : String)  {
            txtOriginAddress.text = originAddress
        }

        private fun setTripDestinationAddress(destinationAddress : String)  {
            txtDestinationAddress.text = destinationAddress
        }
        private fun setTripState(state: String?) {
            val colorRes = when (state) {
                TripState.PENDING -> R.color.trip_pending
                TripState.CONFIRMED -> R.color.trip_confirmed
                TripState.CANCELLED -> R.color.trip_cancelled
                TripState.DONE -> R.color.trip_done
                else -> R.color.trip_done
            }

            card.setCardBackgroundColor(
                ContextCompat.getColor(v.context, colorRes)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip,parent,false)
        return (TripHolder(view))
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    fun getSelectedItem(position: Int): TripItemUI {
        return trips[position]
    }

    fun submitList(newTrips: MutableList<TripItemUI>) {
        trips.clear()
        trips.addAll(newTrips)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TripHolder, position: Int) {
        val item = trips[position]

        holder.bind(item)

        holder.itemView.setOnClickListener() {
            onClick(position)
        }


    }

}
package com.example.vanalaeropuerto.adapters.empresa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.entities.Driver

class DriverAdapter(
    var drivers : MutableList<Driver>,
    var onClick : (Int) -> Unit
) : RecyclerView.Adapter<DriverAdapter.DriverHolder>()
{

    class DriverHolder(view : View) : RecyclerView.ViewHolder(view)
    {

        private var v : View
        init {
            this.v = view
        }

        fun setDriverName(name : String)  {
            val txtTitle : TextView = v.findViewById(R.id.tvDriverName)
            txtTitle.text = name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_driver,parent,false)
        return (DriverHolder(view))
    }

    override fun getItemCount(): Int {
        return drivers.size
    }

    fun getSelectedProduct(position: Int): Driver {
        return drivers[position]
    }

    fun submitList(newDrivers: MutableList<Driver>) {
        drivers.clear()
        drivers.addAll(newDrivers)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DriverHolder, position: Int) {
        val firstName = drivers[position].getDriverName()
        val lastName = drivers[position].getDriverSurname()

        holder.setDriverName("$firstName $lastName")

        holder.itemView.setOnClickListener() {
            onClick(position)
        }
    }

}
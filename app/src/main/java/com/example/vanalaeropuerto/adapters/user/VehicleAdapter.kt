package com.example.vanalaeropuerto.adapters.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.entities.Vehicle
import java.text.NumberFormat
import java.util.Locale

class VehicleAdapter(
    var vehicles : MutableList<Vehicle>,
    var onClick : (Int) -> Unit
) : RecyclerView.Adapter<VehicleAdapter.VehicleHolder>() {

    class VehicleHolder(view : View) : RecyclerView.ViewHolder(view)
    {

        private var v : View
        init {
            this.v = view
        }

        fun setVehicleName(name : String)  {
            val txtTitle : TextView = v.findViewById(R.id.tvVehicleName)
            txtTitle.text = name
        }
        fun setVehiclePrice(price: Double) {
            val txtPrice: TextView = v.findViewById(R.id.etVehiclePrice)

            val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())

            val formattedPrice = numberFormat.format(price)

            txtPrice.text = "$$formattedPrice"
        }

        fun setProductImage(imageUri: String) {
            val imgVehicle: ImageView = v.findViewById(R.id.ivVehicleImage)

            Glide.with(imgVehicle.context)
                .load(imageUri)
                .placeholder(R.drawable.placeholderimage_124)
                .error(R.drawable.image_error_24)
                .into(imgVehicle)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle,parent,false)
        return (VehicleHolder(view))
    }

    override fun getItemCount(): Int {
        return vehicles.size
    }

    fun getSelectedProduct(position: Int):Vehicle {
        return vehicles[position]
    }

    fun submitList(newProducts: MutableList<Vehicle>) {
        vehicles.clear()
        vehicles.addAll(newProducts)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VehicleHolder, position: Int) {
        vehicles[position].getVehicleName()?.let { holder.setVehicleName(it) }
        vehicles[position].getVehiclePrice()?.let { holder.setVehiclePrice(it) }
        vehicles[position].getVehicleUrlImage()?.let { holder.setProductImage(it) }

        holder.itemView.setOnClickListener() {
            onClick(position)
        }
    }

}
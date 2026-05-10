package com.shalenammapride.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shalenammapride.databinding.ItemFacilityBinding
import com.shalenammapride.models.Facility

/**
 * FacilityAdapter - RecyclerView adapter for displaying school facilities in a grid.
 * Each item shows an emoji icon, facility name, and a short description.
 */
class FacilityAdapter(
    private val context: Context,
    private val facilityList: List<Facility>
) : RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder>() {

    /**
     * ViewHolder - Holds references to the views in each grid item.
     * Using ViewBinding for clean, null-safe view access.
     */
    inner class FacilityViewHolder(val binding: ItemFacilityBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder {
        val binding = ItemFacilityBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return FacilityViewHolder(binding)
    }

    // Called to bind data to each ViewHolder
    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        val facility = facilityList[position]

        holder.binding.apply {
            // Set the emoji and text
            tvFacilityEmoji.text = facility.emoji
            tvFacilityName.text = facility.name
            tvFacilityDescription.text = facility.description

            // Set background color from color resource
            cardFacility.setCardBackgroundColor(
                ContextCompat.getColor(context, facility.colorResId)
            )
        }
    }

    // Returns total number of items
    override fun getItemCount() = facilityList.size
}

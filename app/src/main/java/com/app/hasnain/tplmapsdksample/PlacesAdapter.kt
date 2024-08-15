package com.app.hasnain.tplmapsdksample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tplmaps.sdk.places.Place

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/08/2024
 */
class PlacesAdapter(
    private val placesList: List<Place>,
    private val onItemClick: (Place) -> Unit
) : RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(place: Place) {
            placeName.text = place.name
            itemView.setOnClickListener {
                onItemClick(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(placesList[position])
    }

    override fun getItemCount(): Int {
        return placesList.size
    }
}

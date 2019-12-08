package com.example.moviedbapi.main.cinema.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedbapi.R
import com.example.moviedbapi.base.ParentViewHolder
import com.example.moviedbapi.data.roomCinema.Cinema
import com.example.moviedbapi.utilities.AppConstants

class CinemaListAdapter (
    private val context: Context?,
    private val itemClickListener: ItemClickListener
): RecyclerView.Adapter<CinemaListAdapter.CinemaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cinemas = emptyList<Cinema>()

    inner class CinemaViewHolder(itemView: View): ParentViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCinemaName)
        val tvAddress: TextView = itemView.findViewById(R.id.tvCinemaAddress)
        val ivPoster: ImageView = itemView.findViewById(R.id.ivCinemaPoster)

        fun setItemClick(item: Cinema) {
            itemView.setOnClickListener{
                itemClickListener.onItemClick(item)
            }
        }

        override fun clear() { }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CinemaViewHolder {
        val itemView = inflater.inflate(R.layout.row_item_cinema, parent, false)
        return CinemaViewHolder(itemView)
    }

    override fun getItemCount(): Int = cinemas.size

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        val current = cinemas[position]
        holder.tvName.text = current.name
        holder.tvAddress.text = current.address
        holder.setItemClick(current)
        val imageUrl = "${AppConstants.POSTER_CINEMA_BASE_URL}${current.poster}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.ivPoster)
    }

    internal fun setCinemas(cinemas: List<Cinema>) {
        this.cinemas = cinemas
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onItemClick(item: Cinema)
    }
}
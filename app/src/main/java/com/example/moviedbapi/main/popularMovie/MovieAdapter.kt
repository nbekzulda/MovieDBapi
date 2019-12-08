package com.example.moviedbapi.main.popularMovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedbapi.R
import com.example.moviedbapi.base.ParentViewHolder
import com.example.moviedbapi.data.models.MovieData
import com.example.moviedbapi.utilities.AppConstants


class MovieAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ParentViewHolder>() {

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1

    private var isLoaderVisible = false

    private val movieList = ArrayList<MovieData>()


    fun clearAll() {
        movieList.clear()
        notifyDataSetChanged()
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = movieList.size - 1
        if (movieList.isNotEmpty()) {
            val item = getItem(position)
            if (item != null) {
                movieList.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun getItem(position: Int) : MovieData? {
        return movieList[position]
    }

    fun addItems(list: List<MovieData>) {
        movieList.addAll(list)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        movieList.add(MovieData(id = -1))
        notifyItemInserted(movieList.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            VIEW_TYPE_NORMAL -> MovieViewHolder (
                inflater.inflate(R.layout.row_item_movie, parent, false)
            )
            VIEW_TYPE_LOADING -> ProgressViewHolder (
                inflater.inflate(R.layout.row_item_loading, parent, false)
            )
            else -> throw Throwable("invalid view")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(isLoaderVisible) {
            if (position == movieList.size - 1) {
                VIEW_TYPE_LOADING
            } else {
                VIEW_TYPE_NORMAL
            }
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            val movie = movieList[position]
            holder.bind(movie)
            holder.setItemClick(movie)
        }
    }

    inner class MovieViewHolder(view: View): ParentViewHolder(view) {
        private val tvName: TextView
        private val tvDate: TextView
        private val ivPoster: ImageView

        init {
            tvName = view.findViewById(R.id.tvName)
            tvDate = view.findViewById(R.id.tvDate)
            ivPoster = view.findViewById(R.id.ivPoster)
        }

        fun bind(movie: MovieData) {
            tvName.text = movie.title
            movie.releaseDate.let{ date ->
                tvDate.text = date
            }
            val imageUrl = "${AppConstants.POSTER_BASE_URL}${movie.posterPath}"

            Glide.with(itemView.context)
                .load(imageUrl)
                .into(ivPoster)

        }
        fun setItemClick(item: MovieData) {
            itemView.setOnClickListener{
                itemClickListener.onItemClick(item)
            }
        }
        override fun clear() { }
    }

    inner class ProgressViewHolder(view:View): ParentViewHolder(view) {
        override fun clear() { }
    }

    interface ItemClickListener {
        fun onItemClick(item: MovieData)
    }
}
package com.baka3k.architecture.clean.movie.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.baka3k.architecture.clean.R
import com.baka3k.architecture.domain.model.Movie
import com.bumptech.glide.Glide

class PopularAdapter(
    data: List<Movie>,
    private val onItemClicked: (Movie) -> Unit
) :
    RecyclerView.Adapter<PopularAdapter.PopularMovieViewHolder>() {
    private val movies: MutableList<Movie> = mutableListOf()

    init {
        movies.addAll(data)
    }

    fun reloadData(data: List<Movie>) {
        movies.clear()
        movies.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        val mView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_popular_movie, parent, false)

        return PopularMovieViewHolder(mView) {
            onItemClicked(movies[it])
        }
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        holder.onBindViewHolder(movie = movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class PopularMovieViewHolder(
        itemView: View,
        onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            imageView.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun onBindViewHolder(movie: Movie) {
            val urlImage = movie.posterPath
            Log.d("PopularMovieViewHolder", "urlImage $urlImage")
            Glide.with(imageView)
                .load(urlImage)
                .optionalCenterCrop()
                .into(imageView)
        }
    }
}

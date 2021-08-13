package com.baka3k.architecture.clean.movie.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.baka3k.architecture.clean.R
import com.baka3k.architecture.clean.movie.viewstate.MovieView
import com.baka3k.architecture.domain.model.Movie
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import kotlin.random.Random

class MoviesAdapter(
    data: List<MovieView>,
    private val onItemClicked: (Movie) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val movies = mutableListOf<MovieView>()


    init {
        movies.addAll(data)
    }

    fun reloadData(data: List<MovieView>) {
        movies.clear()
        movies.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.popular_movie_item, parent, false)
            PopularViewHolder(view) {
                onItemClicked(it)
            }
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_nowplaying, parent, false)
            NowPlayingViewHolder(view) {
                onItemClicked(movies[it].movie!!)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieView = movies[position]
        if (holder.itemViewType == 0) {
            if (!movieView.popularList.isNullOrEmpty()) {
                (holder as PopularViewHolder).onBindViewHolder(movieView.popularList)
            }
        } else {
            if (movieView.movie != null) {
                (holder as NowPlayingViewHolder).onBindViewHolder(movieView.movie!!)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return movies[position].type
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class PopularViewHolder(
        itemView: View,
        private val onItemClicked: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val viewPager: ViewPager2 = itemView.findViewById(R.id.pager)

        fun onBindViewHolder(popularList: List<Movie>) {
            viewPager.adapter = PopularAdapter(popularList) {
                onItemClicked(it)
            }
        }
    }

    class NowPlayingViewHolder(
        itemView: View,
        onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
        private val icon: AppCompatImageView = itemView.findViewById(R.id.icon)
        private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val subTitle: TextView = itemView.findViewById(R.id.subTitle)
        private val content: TextView = itemView.findViewById(R.id.content)
        private val icons = arrayListOf(R.drawable.icon1, R.drawable.icon2, R.drawable.icon3)

        init {
            cardView.setOnClickListener {
                Log.d("imageView", "setOnClickListener")
                onItemClicked(adapterPosition)
            }
        }

        fun onBindViewHolder(movie: Movie) {
            title.text = movie.title
            subTitle.text = movie.originalTitle
            content.text = movie.overview
            val urlImage = movie.posterPath
            Log.d("NowPlayingViewHolder", "urlImage $urlImage")
            Glide.with(imageView)
                .load(urlImage)
                .override(imageView.width, itemView.height)
                .optionalCenterCrop()
                .into(imageView)

            Glide.with(icon)
                .load(icons[Random.nextInt(0, 2)])
                .override(120, 120)
                .optionalCenterCrop()
                .into(icon)
        }
    }
}

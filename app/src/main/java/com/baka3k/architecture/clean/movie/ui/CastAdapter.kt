package com.baka3k.architecture.clean.movie.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baka3k.architecture.clean.R
import com.baka3k.architecture.domain.model.Actor
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions


class CastAdapter(
    data: List<Actor>,
    private val onItemClicked: (Actor) -> Unit
) : RecyclerView.Adapter<CastAdapter.ActorViewHolder>() {
    private val actors: MutableList<Actor> = mutableListOf()

    init {
        actors.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actor, parent, false)
        return ActorViewHolder(view) {
            onItemClicked(actors[it])
        }
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.onBindViewHolder(actor = actors[position])
    }

    override fun getItemCount(): Int {
        return actors.size
    }

    class ActorViewHolder(
        itemView: View,
        onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageAvatar)
        private val name: TextView = itemView.findViewById(R.id.name)

        init {
            imageView.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun onBindViewHolder(actor: Actor) {
            var data = arrayListOf<String>(
                "https://image.tmdb.org/t/p/w200/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
                "https://image.tmdb.org/t/p/w200/be1bVF7qGX91a6c5WeRPs5pKXln.jpg",
                "https://image.tmdb.org/t/p/w200/6EZaBiQHx3Xlz3j0D6ttDxHXaxr.jpg",
                "https://image.tmdb.org/t/p/w200/ieINP7YlragZ999aU0zfuve1lM2.jpg",
                "https://image.tmdb.org/t/p/w200/c4TTWy1WntzDxpgIe8kbODjWOfD.jpg",
                "https://image.tmdb.org/t/p/w200/qDRGPAcQoW8Wuig9bvoLpHwf1gU.jpg",
            )

            name.text = actor.name
            val urlImage = actor.image
//            val urlImage = data.random()
            Log.d("CastAdapter", "onBindViewHolder $urlImage")
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(imageView)
                .applyDefaultRequestOptions(requestOptions)
                .load(urlImage)
                .override(200, 200)
                .optionalCenterCrop()
                .priority(Priority.HIGH)
                .into(imageView)

        }
    }
}

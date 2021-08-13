package com.baka3k.architecture.clean.movie.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baka3k.architecture.clean.R
import com.baka3k.architecture.clean.base.ui.BaseFragment
import com.baka3k.architecture.clean.base.viewmodel.ViewModelFactory
import com.baka3k.architecture.clean.movie.viewmodel.MovieDetailViewModel
import com.baka3k.architecture.clean.utils.NetworkUtils
import com.baka3k.architecture.data.exceptions.NetworkExceptions
import com.baka3k.architecture.data.utils.connectivity.NetworkConnection
import com.baka3k.architecture.domain.model.Actor
import com.baka3k.architecture.domain.model.MovieDetail
import com.bumptech.glide.Glide

class MovieDetailFragment : BaseFragment() {
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var cast: TextView
    private lateinit var title: TextView
    private lateinit var content: TextView
    private lateinit var time: TextView
    private lateinit var languge: TextView
    private lateinit var rating: TextView
    private lateinit var category: TextView
    private lateinit var coverMovie: AppCompatImageView

    private lateinit var progressLoading: ContentLoadingProgressBar
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var networkConnection: NetworkConnection

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coverMovie = view.findViewById(R.id.coverMovie)
        recyclerView = view.findViewById(R.id.recyclerView)
        cast = view.findViewById(R.id.cast)
        title = view.findViewById(R.id.title)
        content = view.findViewById(R.id.content)
        time = view.findViewById(R.id.time)
        languge = view.findViewById(R.id.language)
        rating = view.findViewById(R.id.rating)
        category = view.findViewById(R.id.category)
        progressLoading = view.findViewById(R.id.progressLoading)
        recyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    override fun onCreated() {
        super.onCreated()
        initViewModel()
        observeData()
    }

    override fun onStart() {
        super.onStart()
        loadMovieDetail()
    }

    private fun loadMovieDetail() {
        Log.d("test","#loadMovieDetail ${args.movieId}") //436969
        viewModel.loadMovie(args.movieId)
//        viewModel.loadMovie(436969)
    }

    private fun observeData() {

        viewModel.errorLiveEvent.observe(this) { error ->
            when (error) {
                is NetworkExceptions -> {
                    Toast.makeText(context, "NetworkError", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.movie },
            observer = { data ->
                updateMovieDetailView(data)
            }
        )
        viewModel.store.observeAnyway(
            owner = this,
            selector = { state -> state.loading },
            observer = { loading ->
                if (loading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        )
    }

    private fun updateMovieDetailView(data: MovieDetail) {
//        category.text = data.category
        content.text = data.content
//        languge.text = data.languge
//        rating.text = data.rating
//        time.text = data.time
        title.text = data.title
        Glide.with(this)
            .load(data.cover)
            .optionalCenterCrop()
            .into(coverMovie)
        updateActorView(data.actors)
    }

    private fun updateActorView(actors: MutableList<Actor>) {

        val adapter = CastAdapter(actors) {
            val direction = MovieDetailFragmentDirections.actionMovieDetailFragmentToCameraFragment()
            findNavController().navigate(direction)
        }
        recyclerView.adapter = adapter
    }

    private fun showLoading() {
        progressLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressLoading.visibility = View.GONE
    }

    private fun initViewModel() {
        networkConnection = NetworkUtils.getNetworkConnection(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory(networkConnection)).get(
            MovieDetailViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }
}
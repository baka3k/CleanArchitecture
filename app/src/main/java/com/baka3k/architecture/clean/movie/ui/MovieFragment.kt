package com.baka3k.architecture.clean.movie.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.baka3k.architecture.clean.R
import com.baka3k.architecture.clean.base.ui.BaseFragment
import com.baka3k.architecture.clean.base.viewmodel.ViewModelFactory
import com.baka3k.architecture.clean.movie.viewmodel.MoviesViewModel
import com.baka3k.architecture.clean.utils.NetworkUtils
import com.baka3k.architecture.clean.utils.ToastUtils
import com.baka3k.architecture.data.exceptions.NetworkExceptions
import com.baka3k.architecture.data.utils.connectivity.NetworkConnection
import com.baka3k.architecture.domain.model.Movie

class MovieFragment : BaseFragment() {

    companion object {
        fun newInstance() = MovieFragment()
    }

    private lateinit var progressLoading: ContentLoadingProgressBar
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: MoviesViewModel
    private lateinit var networkConnection: NetworkConnection
    private var moviesAdapter: MoviesAdapter? = null
    private lateinit var swipeRefresherLayout: SwipeRefreshLayout
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        loadNowPlaying()
        swipeRefresherLayout.isRefreshing = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movies_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressLoading = view.findViewById(R.id.progressLoading)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefresherLayout = view.findViewById(R.id.swipeRefresherLayout)
        swipeRefresherLayout.setColorSchemeResources(
            R.color.colorAccent,
            R.color.colorPrimary,
            R.color.design_default_color_primary
        )
        swipeRefresherLayout.setOnRefreshListener(onRefreshListener)

        observeData(viewLifecycleOwner)

        observeData(this.viewLifecycleOwner)

        loadNowPlaying()
    }

    private fun loadNowPlaying() {
        viewModel.loadMovies()
    }

    override fun onCreated() {
        super.onCreated()
        initViewModel()
    }

    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.errorLiveEvent.observe(lifecycleOwner) { error ->
            when (error) {
                is NetworkExceptions -> {
                    ToastUtils.show(requireContext(), "NetworkError", Toast.LENGTH_SHORT)
                }
                else -> {
                    ToastUtils.show(requireContext(), "${error.message}", Toast.LENGTH_SHORT)
                }
            }
        }
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.movies },
            observer = { data ->
                Log.d("test", "viewModel.store.observe")
                if (data.isNotEmpty()) {
                    moviesAdapter = MoviesAdapter(data) {
                        navigateToMovieDetail(it)
                    }
                    recyclerView.adapter = moviesAdapter
                }
            }
        )
        viewModel.store.observeAnyway(
            owner = lifecycleOwner,
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

    /**
     * Routing view by navController
     * */
    private fun navigateToMovieDetail(movie: Movie) {
        val direction = MovieFragmentDirections
            .actionMovieFragmentToMovieDetailFragment(movie.id)
        findNavController().navigate(direction)
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
            MoviesViewModel::class.java
        )
    }
}
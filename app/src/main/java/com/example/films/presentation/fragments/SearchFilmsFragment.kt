package com.example.films.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.example.films.R
import com.example.films.common.Constants
import com.example.films.common.SharedPref
import com.example.films.databinding.FragmentSearchFilmsBinding
import com.example.films.presentation.BaseApplication.Companion.prefs
import com.example.films.presentation.adapters.FilmsAdapter
import com.example.films.presentation.adapters.LoadFilmAdapter
import com.example.films.presentation.base.BaseFragment
import com.example.films.presentation.viewmodels.FilmsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
@ExperimentalPagingApi
class SearchFilmsFragment : BaseFragment<FragmentSearchFilmsBinding>() {
    private val viewModel: FilmsViewModel by viewModels()
    private lateinit var filmAdapter: FilmsAdapter
    private lateinit var sharedPref: SharedPref



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())
        initAdapter()
        swipeRefresh()
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val query = prefs?.saveQuery
            if (query != null) {
                viewModel.getFilms(query).collectLatest {
                    filmAdapter.submitData(it)
                }
            }
        }

        filmAdapter.setOnItemClickListner {
            val bundle = Bundle().apply {
                putString("id", it.imdbID)
                putString("title", it.Title)
            }
            findNavController().navigate(R.id.action_searchFilmsFragment_to_ditailsFragment, bundle)
        }
        searchFilms()
        binding.logout.setOnClickListener {
             exit()
        }
    }


    private fun searchFilms() {
        var job: Job? = null
        binding.edQuery.addTextChangedListener { editable ->
            job?.cancel()
           job = MainScope().launch {
                delay(Constants.TIME_SEARCH)
               editable.let {
                   if (editable.toString().isNotEmpty()){
                       val search = editable.toString()
                       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                           prefs?.saveQuery  = search
                           viewModel.getFilms(search).collectLatest {
                               filmAdapter.submitData(it)
                           }
                       }
                       binding.edQuery.hideKeyboard()
                   }
               }
            }
        }
    }

private fun initAdapter() {
    filmAdapter = FilmsAdapter()
    binding.rvFilms.adapter = filmAdapter.withLoadStateFooter(
        footer = LoadFilmAdapter()
    )
    filmAdapter.addLoadStateListener { state: CombinedLoadStates ->
        val refreshState = state.refresh
        binding.rvFilms.isVisible = refreshState != LoadState.Loading
        binding.progressBar.isVisible = refreshState == LoadState.Loading
        if (refreshState is LoadState.Error) {
            snackBar("${refreshState.error}")
        }
    }
}

    private fun swipeRefresh() {
        binding.swipe.setOnRefreshListener {
            filmAdapter.refresh()
            binding.swipe.isRefreshing = false
        }
    }

    private fun exit() {
        val editor = sharedPref.preferences.edit()
        editor.clear()
        editor.apply()
        findNavController().navigate(R.id.action_searchFilmsFragment_to_registrationFragment)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchFilmsBinding.inflate(inflater, container, false)
}
package com.example.fims.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fims.R
import com.example.fims.common.Constants
import com.example.fims.common.Resources
import com.example.fims.databinding.FragmentSearchFilmsBinding
import com.example.fims.presentation.adapters.FilmsAdapter
import com.example.fims.presentation.base.BaseFragment
import com.example.fims.presentation.viewmodels.FilmsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFilmsFragment : BaseFragment<FragmentSearchFilmsBinding>() {
    private val viewModel: FilmsViewModel by viewModels()
    private lateinit var filmAdapter: FilmsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        lifecycleScope.launchWhenCreated {
            viewModel.films().collectLatest { result->
              when(result) {
                  is Resources.Loading -> {
                     showProgressBar()
                  }
                  is Resources.Success -> {
                          result.data?.let { response->
                              hideProgressbar()
                              response.Search?.let {
                                  filmAdapter.submitList(it)
                              } ?: snackBar("invalid response")
                          }
                  }
                  is Resources.Error -> {
                      result.message?.let {
                          showProgressBar()
                          snackBar(it)
                      }
                  }
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
    }

    private fun searchFilms() {
        var job: Job? = null
       binding.edQuery.addTextChangedListener {
           editable->
           job?.cancel()
           job = MainScope().launch {
               delay(Constants.TIME_SEARCH)
               editable?.let {
                   if (editable.toString().isNotEmpty()) {
                       viewModel.searchFilms(editable.toString())
                       binding.edQuery.hideKeyboard()
                   }
               }
           }
       }
    }

private fun initAdapter() {
    filmAdapter = FilmsAdapter()
    binding.rvFilms.adapter = filmAdapter
}

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchFilmsBinding.inflate(inflater, container, false)
}
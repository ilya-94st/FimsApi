package com.example.fims.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.fims.R
import com.example.fims.common.Resources
import com.example.fims.databinding.FragmentDitailsBinding
import com.example.fims.presentation.base.BaseFragment
import com.example.fims.presentation.viewmodels.DetailsFilmViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDitailsBinding>() {
    private val viewModel: DetailsFilmViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backPress.setOnClickListener {
            findNavController().popBackStack()
        }

       viewModel.getDetailsFilm(args.id)

        lifecycleScope.launchWhenCreated {
            viewModel.details().collectLatest { result->
                when(result) {
                    is Resources.Loading -> {
                       showProgressBar()
                    }
                    is Resources.Success -> {
                        result.data?.let {
                                response->
                            hideProgressbar()
                            binding.title.text = response.Title
                            binding.tvActors.text = response.Actors
                            binding.tvImdbRating.text = response.imdbRating
                            binding.tvImdbVotes.text = response.imdbVotes
                            binding.tvTitleAppBar.text = response.Title
                            binding.tvReleased.text = response.Released
                            binding.tvWriter.text = response.Writer
                            Glide.with(requireContext()).load(response.Poster).into(binding.ivPoster)
                        }
                    }
                    is Resources.Error -> {
                        hideProgressbar()
                        result.message?.let {
                            snackBar(it)
                        }
                    }
                }
            }
        }

        binding.ivYouTube.setOnClickListener {
           getChromeCustomTabs("https://www.youtube.com/results?search_query=${args.title}")
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun getChromeCustomTabs(url: String) {
        val builder = CustomTabsIntent.Builder()
        val params = CustomTabColorSchemeParams.Builder()
        params.setToolbarColor(ContextCompat.getColor(requireContext(),
            R.color.purple_700))
        builder.setDefaultColorSchemeParams(params.build())
        builder.setShowTitle(true)
        val customBuilder = builder.build()
        getCustomTabsPackages(requireContext())
        customBuilder.launchUrl(requireContext(), Uri.parse(url))
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun getCustomTabsPackages(context: Context): ArrayList<ResolveInfo> {
        val pm = context.packageManager
        val activityIntent = Intent().
                setAction(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.fromParts("http", "", null))
        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packageSupportingCustomTabs: ArrayList<ResolveInfo> = ArrayList()
        for(info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            if(pm.resolveService(serviceIntent, 0) != null) {
                packageSupportingCustomTabs.add(info)
            }
        }
        return packageSupportingCustomTabs
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDitailsBinding.inflate(inflater, container, false)
}
package com.example.films.presentation.fragments

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
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.films.R
import com.example.films.common.Resource
import com.example.films.databinding.FragmentDitailsBinding
import com.example.films.presentation.base.BaseFragment
import com.example.films.presentation.viewmodels.DetailsFilmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDitailsBinding>() {
    private val viewModel: DetailsFilmViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backPress.setOnClickListener {
            findNavController().popBackStack()
        }

       viewModel.getFilmsDetails(args.id).observe(viewLifecycleOwner, { response->
           binding.title.text = response.data?.Title
           binding.tvActors.text = response.data?.Actors
           binding.tvImdbRating.text = response.data?.imdbRating
           binding.tvImdbVotes.text = response.data?.imdbVotes
           binding.tvTitleAppBar.text = response.data?.Title
           binding.tvReleased.text = response.data?.Released
           binding.tvWriter.text = response.data?.Writer
           Glide.with(requireContext()).load(response.data?.Poster).into(binding.ivPoster)
           binding.progressBar.isVisible = response is Resource.Loading
       })



        binding.ivYouTube.setOnClickListener {
           getChromeCustomTabs("https://www.youtube.com/results?search_query=${args.title}")
        }
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
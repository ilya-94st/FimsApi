package com.example.films.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.films.R
import com.example.films.common.Constants
import com.example.films.common.SharedPref
import com.example.films.databinding.FragmentRegistrationBinding
import com.example.films.presentation.base.BaseFragment
import com.example.films.presentation.prefs
import com.example.films.presentation.viewmodels.GoogleSignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {
    private lateinit var auth: FirebaseAuth
    private var saveCheckBox: Boolean = false
    private lateinit var pref: SharedPref
    private lateinit var viewModels: GoogleSignInViewModel

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegistrationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModels = ViewModelProvider(this).get(GoogleSignInViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        pref = SharedPref(requireContext())
        saveCheckBox = prefs.save
        saveChoose()

        binding.googleSigIn.setOnClickListener {
            viewModels.getGoogleSignIn(requireContext()).signInIntent.also {
                startActivityForResult(it, Constants.REQUEST_CODE_GOOGLE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constants.REQUEST_CODE_GOOGLE){
            if (resultCode == 0) {
                snackBar("ты не зарегестрировался")
            } else {
                val account = data?.let { viewModels.getGoogleAccount(it) }
                account?.let {
                    googleAuthForFirebase(it)
                }
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credential = viewModels.getCredential(account)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val checkBox = binding.checkBox.isChecked
                prefs.save = checkBox
                auth.signInWithCredential(credential).await()
                findNavController().navigate(R.id.action_registrationFragment_to_searchFilmsFragment)
            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    snackBar("$e")
                }
            }
        }
    }

    private fun saveChoose() {
        if(saveCheckBox) {
            findNavController().navigate(R.id.action_registrationFragment_to_searchFilmsFragment)
        }
    }
}
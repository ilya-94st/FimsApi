package com.example.films.presentation.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.films.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider


class GoogleSignInViewModel : ViewModel() {
    fun getGoogleSignIn(context: Context): GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.webclient_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, options)
    }

    fun getGoogleAccount(intent: Intent): GoogleSignInAccount? {
        return GoogleSignIn.getSignedInAccountFromIntent(intent).result
    }

    fun getCredential(account: GoogleSignInAccount): AuthCredential {
        return GoogleAuthProvider.getCredential(account.idToken, null)
    }
}
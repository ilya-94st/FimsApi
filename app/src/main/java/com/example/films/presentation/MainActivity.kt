package com.example.films.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.films.data.network.ConnectivityReceiver
import com.example.films.databinding.ActivityMainBinding
import com.example.films.presentation.tools.SnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
             showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onPause() {
        super.onPause()
        ConnectivityReceiver.connectivityReceiverListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
       LocalBroadcastManager.getInstance(this).unregisterReceiver(ConnectivityReceiver())
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if(!isConnected) {
            SnackBar(binding.root, "You are offline")
        } else {
            SnackBar(binding.root, "You are online")
        }
    }
}
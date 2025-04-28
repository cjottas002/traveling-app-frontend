package org.example.travelingapp.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import org.example.travelingapp.core.network.interfaces.INetworkChecker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidNetworkChecker @Inject constructor(
    @param:ApplicationContext private val context: Context
) : INetworkChecker {

    override fun isInternetAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}

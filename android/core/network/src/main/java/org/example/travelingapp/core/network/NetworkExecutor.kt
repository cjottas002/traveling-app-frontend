package org.example.travelingapp.core.network

import org.example.travelingapp.core.network.interfaces.INetworkChecker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkExecutor @Inject constructor(private val networkChecker: INetworkChecker) {

    fun executeWithNetworkCheck(onlineAction: () -> Unit, offlineAction: () -> Unit) {

        if (networkChecker.isInternetAvailable())
            onlineAction()
        else
            offlineAction()
    }
}
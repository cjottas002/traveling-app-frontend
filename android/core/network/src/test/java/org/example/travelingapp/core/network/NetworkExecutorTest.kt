package org.example.travelingapp.core.network

import org.example.travelingapp.core.network.interfaces.INetworkChecker
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkExecutorTest {

    @Test
    fun executeWithNetworkCheck_callsOnlineAction_whenInternetIsAvailable() {
        val executor = NetworkExecutor(FakeNetworkChecker(available = true))
        var onlineCalled = false
        var offlineCalled = false

        executor.executeWithNetworkCheck(
            onlineAction = { onlineCalled = true },
            offlineAction = { offlineCalled = true }
        )

        assertTrue(onlineCalled)
        assertFalse(offlineCalled)
    }

    @Test
    fun executeWithNetworkCheck_callsOfflineAction_whenInternetIsUnavailable() {
        val executor = NetworkExecutor(FakeNetworkChecker(available = false))
        var onlineCalled = false
        var offlineCalled = false

        executor.executeWithNetworkCheck(
            onlineAction = { onlineCalled = true },
            offlineAction = { offlineCalled = true }
        )

        assertFalse(onlineCalled)
        assertTrue(offlineCalled)
    }

    private class FakeNetworkChecker(
        private val available: Boolean
    ) : INetworkChecker {
        override fun isInternetAvailable(): Boolean = available
    }
}

package org.example.travelingapp.core.extensions

import org.example.travelingapp.core.request.FrameworkRequest
import org.junit.Assert.assertEquals
import org.junit.Test

class FrameworkRequestExtensionTest {

    @Test
    fun toQueryMap_returnsExpectedValues() {
        val request = FrameworkRequest(
            pageIndex = 3,
            pageSize = 25,
            orderBy = "username",
            orderByAsc = false
        )

        val map = request.toQueryMap()

        assertEquals("3", map["pageIndex"])
        assertEquals("25", map["pageSize"])
        assertEquals("username", map["orderBy"])
        assertEquals("false", map["orderByAsc"])
        assertEquals(4, map.size)
    }
}

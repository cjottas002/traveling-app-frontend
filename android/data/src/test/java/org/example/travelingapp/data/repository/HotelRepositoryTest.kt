package org.example.travelingapp.data.repository

import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.example.travelingapp.domain.entities.hotelmodel.Address
import org.example.travelingapp.domain.entities.hotelmodel.Coordinate
import org.example.travelingapp.domain.entities.hotelmodel.Features
import org.example.travelingapp.domain.entities.hotelmodel.GuestReviews
import org.example.travelingapp.domain.entities.hotelmodel.Hotel
import org.example.travelingapp.data.remote.hotelmodel.HotelResponse
import org.example.travelingapp.domain.entities.hotelmodel.OptimizedThumbUrls
import org.example.travelingapp.domain.entities.hotelmodel.Price
import org.example.travelingapp.domain.entities.hotelmodel.RatePlan
import org.example.travelingapp.data.remote.services.IHotelService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class HotelRepositoryTest {

    @Test
    fun fetchHotels_returnsList_whenServiceIsSuccessful() = runBlocking {
        val repository = HotelRepository(
            FakeHotelService { Response.success(sampleHotelResponse()) }
        )

        val result = repository.fetchHotels()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
        assertEquals("Hotel 1", result.getOrThrow().first().name)
    }

    @Test
    fun fetchHotels_returnsFailure_whenServiceReturnsHttpError() = runBlocking {
        val repository = HotelRepository(
            FakeHotelService {
                Response.error(
                    500,
                    "server-error".toResponseBody("text/plain".toMediaType())
                )
            }
        )

        val result = repository.fetchHotels()

        assertTrue(result.isFailure)
        assertEquals("Error fetching hotels: 500", result.exceptionOrNull()?.message)
    }

    @Test
    fun fetchHotels_wrapsException_whenServiceThrows() = runBlocking {
        val repository = HotelRepository(
            FakeHotelService { throw RuntimeException("timeout") }
        )

        val result = repository.fetchHotels()

        assertTrue(result.isFailure)
        assertEquals("Network error. Please try again.", result.exceptionOrNull()?.message)
        assertNotNull(result.exceptionOrNull()?.cause)
        assertEquals("timeout", result.exceptionOrNull()?.cause?.message)
    }

    private fun sampleHotelResponse(): HotelResponse {
        return HotelResponse(
            totalCount = 1,
            results = listOf(sampleHotel(1)),
            pagination = null
        )
    }

    private fun sampleHotel(id: Int): Hotel {
        return Hotel(
            id = id,
            name = "Hotel $id",
            starRating = 4,
            address = Address(
                streetAddress = "Street 1",
                extendedAddress = null,
                locality = "Madrid",
                postalCode = "28001",
                region = "Madrid",
                countryName = "Spain",
                countryCode = "ES",
                obfuscate = false
            ),
            guestReviews = GuestReviews(
                unformattedRating = 8.9,
                rating = "8.9",
                total = 100,
                scale = 10,
                badge = null,
                badgeText = null
            ),
            ratePlan = RatePlan(
                price = Price(
                    current = "120",
                    exactCurrent = 120.0,
                    old = null
                ),
                features = Features(
                    paymentPreference = true,
                    noCCRequired = false
                )
            ),
            neighbourhood = "Center",
            coordinate = Coordinate(lat = 40.4, lon = -3.7),
            providerType = "provider",
            supplierHotelId = id,
            isAlternative = false,
            optimizedThumbUrls = OptimizedThumbUrls(
                srpDesktop = "https://example.com/$id.jpg"
            )
        )
    }

    private class FakeHotelService(
        private val responseProvider: suspend () -> Response<HotelResponse>
    ) : IHotelService {
        override suspend fun getHotels(): Response<HotelResponse> = responseProvider()
    }
}

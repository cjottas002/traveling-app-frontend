package org.example.travelingapp.domain.services

import kotlinx.coroutines.runBlocking
import org.example.travelingapp.data.remote.services.IAccountService
import org.example.travelingapp.data.remote.services.IHotelService
import org.example.travelingapp.data.remote.services.IUserService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.example.travelingapp.core.request.login.LoginRequest
import org.example.travelingapp.core.request.register.RegisterRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceContractsTest {

    private lateinit var server: MockWebServer
    private lateinit var accountService: IAccountService
    private lateinit var userService: IUserService
    private lateinit var hotelService: IHotelService

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        accountService = retrofit.create(IAccountService::class.java)
        userService = retrofit.create(IUserService::class.java)
        hotelService = retrofit.create(IHotelService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun accountLogin_contract_methodPathBodyAndResponse() = runBlocking {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(
                """
                {"data":{"userId":"u-1","token":"token-1"},"count":1,"errors":[],"success":true}
                """.trimIndent()
            )
        )

        val response = accountService.login(LoginRequest(username = "john", password = "123"))
        val request = server.takeRequest()
        val body = request.body.readUtf8()

        assertEquals("POST", request.method)
        assertEquals("/api/account/login", request.path)
        assertTrue(body.contains("\"username\":\"john\""))
        assertTrue(body.contains("\"password\":\"123\""))
        assertNotNull(response.body()?.data)
        assertEquals("u-1", response.body()?.data?.userId)
        assertEquals("token-1", response.body()?.data?.token)
    }

    @Test
    fun accountRegister_contract_methodPathBodyAndResponse() = runBlocking {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(
                """
                {"data":{"isRegistered":true},"count":1,"errors":[],"success":true}
                """.trimIndent()
            )
        )

        val response = accountService.register(RegisterRequest(username = "mary", password = "abc"))
        val request = server.takeRequest()
        val body = request.body.readUtf8()

        assertEquals("POST", request.method)
        assertEquals("/api/account/register", request.path)
        assertTrue(body.contains("\"username\":\"mary\""))
        assertTrue(body.contains("\"password\":\"abc\""))
        assertEquals(true, response.body()?.data?.isRegistered)
    }

    @Test
    fun userService_contract_headerAndQueryMap() = runBlocking {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(
                """
                {"data":[{"id":"u-1","name":"John","email":"john@mail.com","password":"123","updatedAt":10}],"count":1,"errors":[],"success":true}
                """.trimIndent()
            )
        )

        val response = userService.getAllUsers(
            token = "Bearer token-1",
            query = mapOf("pageIndex" to "2", "pageSize" to "25", "orderBy" to "name", "orderByAsc" to "true")
        )
        val request = server.takeRequest()
        val url = request.requestUrl!!

        assertEquals("GET", request.method)
        assertEquals("/api/user/getAllUsers", url.encodedPath)
        assertEquals("2", url.queryParameter("pageIndex"))
        assertEquals("25", url.queryParameter("pageSize"))
        assertEquals("name", url.queryParameter("orderBy"))
        assertEquals("true", url.queryParameter("orderByAsc"))
        assertEquals("Bearer token-1", request.getHeader("Authorization"))
        assertEquals(1, response.body()?.data?.size)
        assertEquals("u-1", response.body()?.data?.first()?.id)
    }

    @Test
    fun hotelService_contract_methodPathAndParsing() = runBlocking {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(
                """
                {
                  "totalCount": 1,
                  "results": [
                    {
                      "id": 1,
                      "name": "Hotel 1",
                      "starRating": 4,
                      "address": {
                        "streetAddress": "Street 1",
                        "extendedAddress": null,
                        "locality": "Madrid",
                        "postalCode": "28001",
                        "region": "Madrid",
                        "countryName": "Spain",
                        "countryCode": "ES",
                        "obfuscate": false
                      },
                      "guestReviews": {
                        "unformattedRating": 8.9,
                        "rating": "8.9",
                        "total": 100,
                        "scale": 10,
                        "badge": null,
                        "badgeText": null
                      },
                      "ratePlan": {
                        "price": {
                          "current": "120",
                          "exactCurrent": 120.0,
                          "old": null
                        },
                        "features": {
                          "paymentPreference": true,
                          "noCCRequired": false
                        }
                      },
                      "neighbourhood": "Center",
                      "coordinate": {"lat": 40.4, "lon": -3.7},
                      "providerType": "provider",
                      "supplierHotelId": 10,
                      "isAlternative": false,
                      "optimizedThumbUrls": {"srpDesktop": "https://example.com/1.jpg"}
                    }
                  ],
                  "pagination": {
                    "currentPage": 1,
                    "pageGroup": "A",
                    "nextPageStartIndex": 20,
                    "nextPageNumber": 2,
                    "nextPageGroup": "B"
                  }
                }
                """.trimIndent()
            )
        )

        val response = hotelService.getHotels()
        val request = server.takeRequest()

        assertEquals("GET", request.method)
        assertEquals("/listHotels", request.path)
        assertEquals(1, response.body()?.results?.size)
        assertEquals("Hotel 1", response.body()?.results?.first()?.name)
    }
}

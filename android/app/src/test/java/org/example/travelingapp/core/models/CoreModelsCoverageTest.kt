package org.example.travelingapp.core.models

import org.example.travelingapp.core.request.FrameworkRequest
import org.example.travelingapp.core.request.login.LoginRequest
import org.example.travelingapp.core.request.register.RegisterRequest
import org.example.travelingapp.core.request.user.UserRequest
import org.example.travelingapp.core.response.FrameworkListResponse
import org.example.travelingapp.core.response.FrameworkResponse
import org.example.travelingapp.core.response.ResponseDto
import org.example.travelingapp.core.response.ValidationError
import org.example.travelingapp.core.response.login.LoginResponse
import org.example.travelingapp.core.response.login.dtos.LoginDto
import org.example.travelingapp.core.response.register.RegisterResponse
import org.example.travelingapp.core.response.register.dtos.RegisterDto
import org.example.travelingapp.core.response.user.UserListResponse
import org.example.travelingapp.core.response.user.UserResponse
import org.example.travelingapp.core.response.user.dtos.UserDto
import org.example.travelingapp.data.local.entities.TransportEntity
import org.example.travelingapp.data.local.entities.UserEntity
import org.example.travelingapp.data.mapper.toEntities
import org.example.travelingapp.data.mapper.toTransport
import org.example.travelingapp.data.mapper.toUser
import org.example.travelingapp.data.remote.Pagination
import org.example.travelingapp.data.remote.User
import org.example.travelingapp.domain.entities.PageData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CoreModelsCoverageTest {

    @Test
    fun requestModels_holdExpectedValues() {
        val frameworkRequest = FrameworkRequest(
            pageIndex = 2,
            pageSize = 20,
            orderBy = "name",
            orderByAsc = false
        )
        val userRequest = UserRequest().apply {
            pageIndex = 3
            pageSize = 15
            orderBy = "updatedAt"
            orderByAsc = true
        }
        val loginRequest = LoginRequest(username = "john", password = "123")
        val registerRequest = RegisterRequest(username = "mary", password = "abc")

        assertEquals(2, frameworkRequest.pageIndex)
        assertEquals(20, frameworkRequest.pageSize)
        assertEquals("name", frameworkRequest.orderBy)
        assertFalse(frameworkRequest.orderByAsc)
        assertEquals(3, userRequest.pageIndex)
        assertEquals(15, userRequest.pageSize)
        assertEquals("updatedAt", userRequest.orderBy)
        assertTrue(userRequest.orderByAsc)
        assertEquals("john", loginRequest.username)
        assertEquals("123", loginRequest.password)
        assertEquals("mary", registerRequest.username)
        assertEquals("abc", registerRequest.password)
    }

    @Test
    fun responseModels_computeSuccessFromErrors() {
        val loginDto = LoginDto(userId = "id-1", token = "token-1")
        val registerDto = RegisterDto(isRegistered = true)
        val validationError = ValidationError(fields = listOf("email"), message = "Invalid email")

        val okResponse = FrameworkResponse(
            data = loginDto,
            count = 1,
            errors = emptyList()
        )
        val errorResponse = FrameworkResponse<LoginDto>(
            data = null,
            count = 0,
            errors = listOf(validationError)
        )
        val listResponse = FrameworkListResponse(
            data = listOf(registerDto),
            count = 1,
            errors = emptyList()
        )

        assertTrue(okResponse.success)
        assertFalse(errorResponse.success)
        assertTrue(listResponse.success)
        assertEquals("Invalid email", errorResponse.errors.first().message)
    }

    @Test
    fun specializedResponses_andDtos_areMutableAsExpected() {
        val loginResponse = LoginResponse().apply {
            data = LoginDto(userId = "u-1", token = "tk-1")
        }
        val registerResponse = RegisterResponse().apply {
            data = RegisterDto(isRegistered = true)
        }
        val userResponse = UserResponse().apply {
            data = UserDto(
                id = "id",
                username = "John",
                email = "john@mail.com",
                updatedAt = 10L
            )
        }
        val userListResponse = UserListResponse().apply {
            data = listOf(
                UserDto(
                    id = "id-2",
                    username = "Mary",
                    email = "mary@mail.com",
                    updatedAt = 22L
                )
            )
        }

        assertEquals("u-1", loginResponse.data?.userId)
        assertTrue(registerResponse.data?.isRegistered == true)
        assertEquals("John", userResponse.data?.username)
        assertEquals(1, userListResponse.data.size)
    }

    @Test
    fun entityAndMapperModels_coverDefaultsAndTransformations() {
        val transportEntity = TransportEntity(name = "Bike", imageRes = 10, price = "9")
        val userEntity = UserEntity(
            id = "id-10",
            username = "john",
            email = "john@mail.com",
            updateAt = 1L
        )
        val mappedTransport = transportEntity.toTransport()
        val mappedUser = userEntity.toUser()
        val usersResponse = UserListResponse().apply {
            data = listOf(
                UserDto("1", "A", "a@mail.com", 4L),
                UserDto(null, null, null, 5L)
            )
        }
        val entities = usersResponse.toEntities()

        assertEquals(0, transportEntity.id)
        assertEquals("Bike", mappedTransport.name)
        assertEquals(10, mappedTransport.imageRes)
        assertEquals("9", mappedTransport.price)
        assertEquals("john", mappedUser.username)
        assertEquals("1", entities[0].id)
        assertEquals("", entities[1].id)
        assertEquals("", entities[1].username)
    }

    @Test
    fun remoteAndDomainModels_coverConstructorsAndToString() {
        val remoteUser = User(
            nombre = "John",
            pass = "123",
            edad = 30,
            genero = 1,
            userToken = "token",
            idBdReference = 9
        )
        val alternativeUser = User(name = "Mary", pass = "abc")
        val emptyUser = User()
        val pagination = Pagination(
            currentPage = 1,
            pageGroup = "A",
            nextPageStartIndex = 20,
            nextPageNumber = 2,
            nextPageGroup = "B"
        )
        val pageData = PageData(image = 3, title = "T", desc = "D")

        assertTrue(remoteUser.toString().contains("John"))
        assertEquals("Mary", alternativeUser.nombre)
        assertEquals("", emptyUser.nombre)
        assertEquals(1, pagination.currentPage)
        assertEquals("A", pagination.pageGroup)
        assertEquals(3, pageData.image)
        assertEquals("T", pageData.title)
        assertEquals("D", pageData.desc)
        val dto = ResponseDto()
        assertEquals(ResponseDto::class, dto::class)
    }
}

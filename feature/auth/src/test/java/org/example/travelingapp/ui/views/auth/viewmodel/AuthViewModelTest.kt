package org.example.travelingapp.ui.views.auth.viewmodel

import android.content.Context
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.example.travelingapp.core.datastore.TokenManager
import org.example.travelingapp.core.network.NetworkExecutor
import org.example.travelingapp.core.response.login.LoginResponse
import org.example.travelingapp.core.response.login.dtos.LoginDto
import org.example.travelingapp.core.response.register.RegisterResponse
import org.example.travelingapp.core.response.register.dtos.RegisterDto
import org.example.travelingapp.domain.repository.IAccountRepository
import org.example.travelingapp.domain.repository.IUserRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: AuthViewModel
    private val accountRepository: IAccountRepository = mockk(relaxed = true)
    private val userRepository: IUserRepository = mockk(relaxed = true)
    private val tokenManager: TokenManager = mockk(relaxed = true)
    private val networkExecutor: NetworkExecutor = mockk()
    private val context: Context = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { context.getString(any()) } returns "error"

        viewModel = AuthViewModel(
            accountRepository = accountRepository,
            userRepository = userRepository,
            tokenManager = tokenManager,
            networkExecutor = networkExecutor,
            context = context
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `username and password have default values`() {
        assertEquals("admin", viewModel.username.value)
        assertEquals("Admin123!", viewModel.password.value)
    }

    @Test
    fun `onUsernameChanged updates username`() {
        viewModel.onUsernameChanged("newuser")
        assertEquals("newuser", viewModel.username.value)
    }

    @Test
    fun `onPasswordChanged updates password`() {
        viewModel.onPasswordChanged("newpass")
        assertEquals("newpass", viewModel.password.value)
    }

    @Test
    fun `onConfirmPasswordChanged updates confirmPassword`() {
        viewModel.onConfirmPasswordChanged("mypass")
        assertEquals("mypass", viewModel.confirmPassword.value)
    }

    @Test
    fun `isLoginEnabled is true when both fields are not blank`() {
        viewModel.onUsernameChanged("user")
        viewModel.onPasswordChanged("pass")
        assertTrue(viewModel.isLoginEnabled.value)
    }

    @Test
    fun `isLoginEnabled is false when username is blank`() {
        viewModel.onUsernameChanged("")
        viewModel.onPasswordChanged("pass")
        assertFalse(viewModel.isLoginEnabled.value)
    }

    @Test
    fun `isLoginEnabled is false when password is blank`() {
        viewModel.onUsernameChanged("user")
        viewModel.onPasswordChanged("")
        assertFalse(viewModel.isLoginEnabled.value)
    }

    @Test
    fun `isRegisterEnabled is false when terms not accepted`() {
        viewModel.onUsernameChanged("user")
        viewModel.onPasswordChanged("Pass123!")
        viewModel.onConfirmPasswordChanged("Pass123!")
        viewModel.onTermsChanged(false)
        assertFalse(viewModel.isRegisterEnabled.value)
    }

    @Test
    fun `isRegisterEnabled is false when passwords dont match`() {
        viewModel.onUsernameChanged("user")
        viewModel.onPasswordChanged("Pass123!")
        viewModel.onConfirmPasswordChanged("different")
        viewModel.onTermsChanged(true)
        assertFalse(viewModel.isRegisterEnabled.value)
    }

    @Test
    fun `isRegisterEnabled is false when password too short`() {
        viewModel.onUsernameChanged("user")
        viewModel.onPasswordChanged("12345")
        viewModel.onConfirmPasswordChanged("12345")
        viewModel.onTermsChanged(true)
        assertFalse(viewModel.isRegisterEnabled.value)
    }

    @Test
    fun `isRegisterEnabled is true when all conditions met`() {
        viewModel.onUsernameChanged("user")
        viewModel.onPasswordChanged("Pass123!")
        viewModel.onConfirmPasswordChanged("Pass123!")
        viewModel.onTermsChanged(true)
        assertTrue(viewModel.isRegisterEnabled.value)
    }

    @Test
    fun `passwordsMatch is true when passwords are equal`() {
        viewModel.onPasswordChanged("Pass123!")
        viewModel.onConfirmPasswordChanged("Pass123!")
        assertTrue(viewModel.passwordsMatch.value)
    }

    @Test
    fun `passwordsMatch is false when passwords differ`() {
        viewModel.onPasswordChanged("Pass123!")
        viewModel.onConfirmPasswordChanged("Other456!")
        assertFalse(viewModel.passwordsMatch.value)
    }

    @Test
    fun `login calls onError when fields are blank`() {
        viewModel.onUsernameChanged("")
        viewModel.onPasswordChanged("")

        var errorCalled = false
        viewModel.login(
            onSuccess = {},
            onError = { errorCalled = true }
        )

        assertTrue(errorCalled)
    }

    @Test
    fun `login online success saves token and calls onSuccess`() = runTest {
        val onlineSlot = slot<() -> Unit>()
        every {
            networkExecutor.executeWithNetworkCheck(
                onlineAction = capture(onlineSlot),
                offlineAction = any()
            )
        } answers { onlineSlot.captured.invoke() }

        val response = LoginResponse().apply {
            data = LoginDto(userId = "1", token = "jwt-token-123")
        }
        coEvery { accountRepository.remoteLogin("admin", "Admin123!") } returns response
        coEvery { tokenManager.fetchToken() } returns "jwt-token-123"

        var successToken = ""
        viewModel.login(
            onSuccess = { successToken = it },
            onError = {}
        )

        assertEquals("jwt-token-123", successToken)
        coVerify { tokenManager.saveToken("jwt-token-123") }
    }

    @Test
    fun `login online failure calls onError`() = runTest {
        val onlineSlot = slot<() -> Unit>()
        every {
            networkExecutor.executeWithNetworkCheck(
                onlineAction = capture(onlineSlot),
                offlineAction = any()
            )
        } answers { onlineSlot.captured.invoke() }

        coEvery { accountRepository.remoteLogin(any(), any()) } throws RuntimeException("Network error")

        var errorMsg = ""
        viewModel.login(
            onSuccess = {},
            onError = { errorMsg = it }
        )

        assertTrue(errorMsg.isNotEmpty())
    }

    @Test
    fun `register calls onError when passwords dont match`() {
        viewModel.onUsernameChanged("user")
        viewModel.onPasswordChanged("Pass123!")
        viewModel.onConfirmPasswordChanged("Different!")

        var errorCalled = false
        viewModel.register(
            onSuccess = {},
            onError = { errorCalled = true }
        )

        assertTrue(errorCalled)
    }

    @Test
    fun `register success calls onSuccess`() = runTest {
        viewModel.onUsernameChanged("newuser")
        viewModel.onPasswordChanged("Pass123!")
        viewModel.onConfirmPasswordChanged("Pass123!")
        viewModel.onTermsChanged(true)

        val response = RegisterResponse().apply {
            data = RegisterDto(isRegistered = true)
        }
        coEvery { accountRepository.remoteRegister("newuser", "Pass123!") } returns response

        var successCalled = false
        viewModel.register(
            onSuccess = { successCalled = true },
            onError = {}
        )

        assertTrue(successCalled)
    }

    @Test
    fun `register failure calls onError with message`() = runTest {
        viewModel.onUsernameChanged("user")
        viewModel.onPasswordChanged("Pass123!")
        viewModel.onConfirmPasswordChanged("Pass123!")
        viewModel.onTermsChanged(true)

        val response = RegisterResponse().apply {
            data = RegisterDto(isRegistered = false)
        }
        coEvery { accountRepository.remoteRegister(any(), any()) } returns response

        var errorCalled = false
        viewModel.register(
            onSuccess = {},
            onError = { errorCalled = true }
        )

        assertTrue(errorCalled)
    }
}

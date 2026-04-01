package org.example.travelingapp.ui.views.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.travelingapp.feature.auth.R
import org.example.travelingapp.core.datastore.TokenManager
import org.example.travelingapp.core.network.NetworkExecutor
import org.example.travelingapp.core.request.user.UserRequest
import org.example.travelingapp.domain.repository.IAccountRepository
import org.example.travelingapp.domain.repository.IUserRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val accountRepository: IAccountRepository,
    private val userRepository: IUserRepository,
    private val tokenManager: TokenManager,
    private val networkExecutor: NetworkExecutor,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _username = MutableStateFlow("admin")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("Admin123!")
    val password: StateFlow<String> = _password.asStateFlow()


    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _acceptedTerms = MutableStateFlow(false)
    val acceptedTerms: StateFlow<Boolean> = _acceptedTerms.asStateFlow()

    val isLoginEnabled: StateFlow<Boolean> = combine(_username, _password) { user, pass ->
        user.isNotBlank() && pass.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val passwordsMatch: StateFlow<Boolean> = combine(_password, _confirmPassword) { pass, confirm ->
        pass.isNotBlank() && pass == confirm
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isRegisterEnabled: StateFlow<Boolean> = combine(
        _username, _password, _confirmPassword, _acceptedTerms
    ) { username, password, confirm, terms ->
        username.isNotBlank() && password.length >= 6 && password == confirm && terms
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChanged(value: String) {
        _confirmPassword.value = value
    }

    fun onTermsChanged(accepted: Boolean) {
        _acceptedTerms.value = accepted
    }

    fun login(onSuccess: (token: String) -> Unit, onError: (String) -> Unit) {
        val user = _username.value
        val pass = _password.value
        if (user.isBlank() || pass.isBlank()) {
            onError(context.getString(R.string.register_error_empty_fields))
            return
        }

        viewModelScope.launch {
            // 1. Try offline first (instant)
            val offlineResponse = accountRepository.localLogin(user, pass)
            if (offlineResponse.success && offlineResponse.data != null) {
                tokenManager.saveToken(offlineResponse.data!!.token)
                onSuccess(offlineResponse.data!!.token)

                // Background: get real JWT for API calls
                launch {
                    runCatching {
                        val online = accountRepository.remoteLogin(user, pass)
                        if (online.success && online.data?.token != null) {
                            tokenManager.saveToken(online.data!!.token)
                        }
                    }
                }
                return@launch
            }

            // 2. No cached credentials — must go online
            runCatching { accountRepository.remoteLogin(user, pass) }
                .onSuccess { response ->
                    if (response.success && response.data?.token != null) {
                        tokenManager.saveToken(response.data!!.token)
                        syncServerData()
                        onSuccess(response.data!!.token)
                    } else {
                        onError(response.errors.joinToString("\n") { it.message })
                    }
                }
                .onFailure {
                    onError(context.getString(R.string.login_failed))
                }
        }
    }

    private suspend fun syncServerData() {
        val token = tokenManager.fetchToken() ?: return
        userRepository.syncAndPersistUsers(token, UserRequest())
    }

    private fun loginOfflineAction(
        username: String,
        pass: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            runCatching { accountRepository.localLogin(username, pass) }
                .onFailure {
                    onError(context.getString(R.string.login_offline_not_available))
                }
                .onSuccess { response ->
                    val data = response.data
                    if (response.success && data != null) {
                        val token = data.token
                        tokenManager.saveToken(token)
                        onSuccess(token)
                    } else {
                        onError(response.errors.joinToString("\n") { it.message })
                    }
                }
        }
    }

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_username.value.isBlank() || _password.value.isBlank()) {
            onError(context.getString(R.string.register_error_empty_fields))
            return
        }
        if (_password.value != _confirmPassword.value) {
            onError(context.getString(R.string.register_error_passwords_dont_match))
            return
        }
        viewModelScope.launch {
            val response = accountRepository.remoteRegister(_username.value, _password.value)
            if (response.success && response.data?.isRegistered == true) {
                onSuccess()
            } else {
                val msg = response.errors.joinToString("\n") { it.message }
                onError(msg)
            }
        }
    }
}

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
import kotlinx.coroutines.flow.map
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

    private val invalidCharacters = emptyArray<Char>()

    private val _username = MutableStateFlow("admin")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("Admin123!")
    val password: StateFlow<String> = _password.asStateFlow()


    private val _isAdult = MutableStateFlow(false)
    val isAdult: StateFlow<Boolean> = _isAdult.asStateFlow()

    val isNameValid: StateFlow<Boolean> = _username
        .map { input -> invalidCharacters.none { it in input } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isLastNameValid: StateFlow<Boolean> = _password
        .map { input -> invalidCharacters.none { it in input } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isLoginEnabled: StateFlow<Boolean> = combine(_username, _password) { user, pass ->
        user.isNotBlank() && pass.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val isRegisterEnabled: StateFlow<Boolean> = combine(
        _username, _password, _isAdult, isNameValid, isLastNameValid
    ) { username, password, adult, nameValid, lastValid ->
        username.isNotBlank() && password.isNotBlank() && adult && nameValid && lastValid
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun setIsAdult(adult: Boolean) {
        _isAdult.value = adult
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
        if (!_isAdult.value) {
            onError(context.getString(R.string.register_error_not_adult))
            return
        }
        if (!isNameValid.value) {
            onError(context.getString(R.string.register_error_invalid_name))
            return
        }
        if (!isLastNameValid.value) {
            onError(context.getString(R.string.register_error_invalid_lastname))
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

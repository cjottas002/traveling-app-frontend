package org.example.travelingapp.ui.views.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.example.travelingapp.feature.auth.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.auth.viewmodel.AuthViewModel
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.AppTextField
import org.example.travelingapp.ui.views.components.TravelCard
import org.example.travelingapp.ui.views.components.TravelCardStyle
import org.example.travelingapp.ui.views.components.TravelLinkButton
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.VerticalSpacer
import org.example.travelingapp.ui.testtags.AuthTestTags

@Composable
fun LoginView(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val username by authViewModel.username.collectAsState()
    val password by authViewModel.password.collectAsState()
    val isLoginEnabled by authViewModel.isLoginEnabled.collectAsState()
    val context = LocalContext.current
    val availableSoonText = stringResource(R.string.itd_available_soon)

    LoginContent(
        username = username,
        password = password,
        isLoginEnabled = isLoginEnabled,
        onUsernameChanged = authViewModel::onUsernameChanged,
        onPasswordChanged = authViewModel::onPasswordChanged,
        onLoginClicked = {
            authViewModel.login(
                onSuccess = { onNavigateToHome() },
                onError = { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            )
        },
        onForgotPasswordClicked = {
            Toast.makeText(context, availableSoonText, Toast.LENGTH_SHORT).show()
        },
        onCreateAccountClicked = onNavigateToRegister
    )
}

/**
 * Stateless login screen. Extracted from [LoginView] so Compose previews can
 * render it without requiring the Hilt-scoped [AuthViewModel]. The hosting
 * [LoginView] wires this content to the real view model.
 */
@Composable
fun LoginContent(
    username: String,
    password: String,
    isLoginEnabled: Boolean,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(AuthTestTags.LOGIN_SCREEN)
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_nomads_city_tour),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.85f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = Dimens.screenPadding)
                .padding(bottom = Dimens.screenBottomPadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            AppText(
                textRes = R.string.login,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = Dimens.cardSpacing)
            )

            TravelCard(style = TravelCardStyle.Translucent) {
                AppTextField(
                    value = username,
                    onValueChange = onUsernameChanged,
                    labelRes = R.string.username,
                    modifier = Modifier.testTag(AuthTestTags.LOGIN_USERNAME_FIELD)
                )

                VerticalSpacer(Dimens.cardSpacing)

                AppTextField(
                    value = password,
                    onValueChange = onPasswordChanged,
                    labelRes = R.string.password,
                    isPassword = true,
                    trailingIconRes = R.drawable.login_ic_lock,
                    keyboardType = KeyboardType.Password,
                    modifier = Modifier.testTag(AuthTestTags.LOGIN_PASSWORD_FIELD)
                )

                VerticalSpacer(Dimens.cardSpacing)

                TravelPrimaryButton(
                    textRes = R.string.login,
                    enabled = isLoginEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(AuthTestTags.LOGIN_SUBMIT_BUTTON),
                    onClick = onLoginClicked
                )
            }

            VerticalSpacer(Dimens.cardSpacing)

            TravelLinkButton(
                textRes = R.string.forgot_password,
                onClick = onForgotPasswordClicked,
                color = Color.White.copy(alpha = 0.85f)
            )

            TravelLinkButton(
                textRes = R.string.create_account_text,
                onClick = onCreateAccountClicked,
                color = Color.White,
                modifier = Modifier.testTag(AuthTestTags.LOGIN_CREATE_ACCOUNT_ACTION)
            )
        }
    }
}

@Preview(showBackground = true, name = "Login - empty")
@Composable
private fun LoginContentEmptyPreview() {
    TravelingAppTheme {
        var user by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        LoginContent(
            username = user,
            password = pass,
            isLoginEnabled = false,
            onUsernameChanged = { user = it },
            onPasswordChanged = { pass = it },
            onLoginClicked = {},
            onForgotPasswordClicked = {},
            onCreateAccountClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Login - filled")
@Composable
private fun LoginContentFilledPreview() {
    TravelingAppTheme {
        var user by remember { mutableStateOf("admin") }
        var pass by remember { mutableStateOf("Admin123!") }
        LoginContent(
            username = user,
            password = pass,
            isLoginEnabled = true,
            onUsernameChanged = { user = it },
            onPasswordChanged = { pass = it },
            onLoginClicked = {},
            onForgotPasswordClicked = {},
            onCreateAccountClicked = {}
        )
    }
}

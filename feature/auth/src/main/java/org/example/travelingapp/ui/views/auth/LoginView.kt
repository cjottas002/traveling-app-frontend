package org.example.travelingapp.ui.views.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.example.travelingapp.feature.auth.R
import org.example.travelingapp.ui.testtags.AuthTestTags
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.auth.viewmodel.AuthViewModel
import org.example.travelingapp.ui.views.components.BrandMarkSize
import org.example.travelingapp.ui.views.components.TravelBrandMark
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
import org.example.travelingapp.ui.views.components.TravelLinkButton
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.TravelTextField
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer

@Composable
fun LoginView(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val username by authViewModel.username.collectAsState()
    val password by authViewModel.password.collectAsState()
    val isLoginEnabled by authViewModel.isLoginEnabled.collectAsState()
    val context = LocalContext.current

    LoginContent(
        username = username,
        password = password,
        isLoginEnabled = isLoginEnabled,
        onUsernameChanged = authViewModel::onUsernameChanged,
        onPasswordChanged = authViewModel::onPasswordChanged,
        onLoginClicked = {
            authViewModel.login(
                onSuccess = { onNavigateToHome() },
                onError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
            )
        },
        onForgotPasswordClicked = onNavigateToForgotPassword,
        onCreateAccountClicked = onNavigateToRegister
    )
}

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.screenPadding)
            .testTag(AuthTestTags.LOGIN_SCREEN)
    ) {
        Header()

        Spacer(Modifier.padding(top = Dimens.spacingXl))

        TravelEditorialBlock(
            kicker = stringResource(R.string.login_kicker),
            title = stringResource(R.string.login_title),
            accent = stringResource(R.string.login_title_accent),
            sub = stringResource(R.string.login_subtitle)
        )

        Spacer(Modifier.padding(top = Dimens.sectionSpacing))

        TravelTextField(
            value = username,
            onValueChange = onUsernameChanged,
            labelRes = R.string.username,
            modifier = Modifier.testTag(AuthTestTags.LOGIN_USERNAME_FIELD)
        )

        TravelVerticalSpacer(Dimens.spacingMd)

        TravelTextField(
            value = password,
            onValueChange = onPasswordChanged,
            labelRes = R.string.password,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            modifier = Modifier.testTag(AuthTestTags.LOGIN_PASSWORD_FIELD)
        )

        TravelVerticalSpacer(Dimens.sectionSpacing)

        TravelPrimaryButton(
            textRes = R.string.login_action,
            enabled = isLoginEnabled,
            trailingArrow = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(AuthTestTags.LOGIN_SUBMIT_BUTTON),
            onClick = onLoginClicked
        )

        TravelVerticalSpacer(Dimens.spacingSm)

        TravelLinkButton(
            textRes = R.string.create_account_text,
            onClick = onCreateAccountClicked,
            modifier = Modifier.testTag(AuthTestTags.LOGIN_CREATE_ACCOUNT_ACTION)
        )

        TravelLinkButton(
            textRes = R.string.forgot_password,
            onClick = onForgotPasswordClicked,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TravelVerticalSpacer(Dimens.screenBottomPadding)
    }
}

@Composable
private fun Header() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.spacingMd)
    ) {
        TravelBrandMark(size = BrandMarkSize.Small)
        Text(
            text = "ES",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
        var user by remember { mutableStateOf("isabel.morais") }
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

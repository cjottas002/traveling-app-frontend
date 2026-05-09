package org.example.travelingapp.ui.views.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import org.example.travelingapp.feature.auth.R
import org.example.travelingapp.ui.testtags.AuthTestTags
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.auth.viewmodel.AuthViewModel
import org.example.travelingapp.ui.views.components.TravelCheckbox
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
import org.example.travelingapp.ui.views.components.TravelIconButton
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.TravelTextField
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer

@Composable
fun RegisterView(navController: NavController, onNavigateToLogin: () -> Unit) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val username by authViewModel.username.collectAsState()
    val password by authViewModel.password.collectAsState()
    val confirmPassword by authViewModel.confirmPassword.collectAsState()
    val isRegisterEnabled by authViewModel.isRegisterEnabled.collectAsState()
    val acceptedTerms by authViewModel.acceptedTerms.collectAsState()
    val passwordsMatch by authViewModel.passwordsMatch.collectAsState()
    val context = LocalContext.current

    RegisterContent(
        username = username,
        password = password,
        confirmPassword = confirmPassword,
        acceptedTerms = acceptedTerms,
        passwordsMatch = passwordsMatch,
        isRegisterEnabled = isRegisterEnabled,
        onBackClicked = { navController.popBackStack() },
        onUsernameChanged = authViewModel::onUsernameChanged,
        onPasswordChanged = authViewModel::onPasswordChanged,
        onConfirmPasswordChanged = authViewModel::onConfirmPasswordChanged,
        onTermsChanged = authViewModel::onTermsChanged,
        onRegisterClicked = {
            authViewModel.register(
                onSuccess = { onNavigateToLogin() },
                onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            )
        },
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun RegisterContent(
    username: String,
    password: String,
    confirmPassword: String,
    acceptedTerms: Boolean,
    passwordsMatch: Boolean,
    isRegisterEnabled: Boolean,
    onBackClicked: () -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onTermsChanged: (Boolean) -> Unit,
    onRegisterClicked: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.screenPadding)
            .testTag(AuthTestTags.REGISTER_SCREEN)
    ) {
        Header(onBackClicked = onBackClicked)

        Spacer(Modifier.padding(top = Dimens.spacingXl))

        TravelEditorialBlock(
            kicker = stringResource(R.string.register_kicker),
            title = stringResource(R.string.register_title),
            accent = stringResource(R.string.register_title_accent),
            sub = stringResource(R.string.register_subtitle)
        )

        Spacer(Modifier.padding(top = Dimens.sectionSpacing))

        TravelTextField(
            value = username,
            onValueChange = onUsernameChanged,
            labelRes = R.string.username,
            modifier = Modifier.testTag(AuthTestTags.REGISTER_NAME_FIELD)
        )

        TravelVerticalSpacer(Dimens.spacingMd)

        TravelTextField(
            value = password,
            onValueChange = onPasswordChanged,
            labelRes = R.string.password,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            modifier = Modifier.testTag(AuthTestTags.REGISTER_PASSWORD_FIELD)
        )

        TravelVerticalSpacer(Dimens.spacingMd)

        TravelTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChanged,
            labelRes = R.string.confirm_password,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            modifier = Modifier.testTag(AuthTestTags.REGISTER_CONFIRM_PASSWORD_FIELD)
        )

        if (confirmPassword.isNotBlank() && !passwordsMatch) {
            TravelVerticalSpacer(Dimens.spacingXs)
            Text(
                text = stringResource(R.string.passwords_dont_match),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        TravelVerticalSpacer(Dimens.spacingLg)

        TermsRow(
            checked = acceptedTerms,
            onCheckedChange = onTermsChanged
        )

        TravelVerticalSpacer(Dimens.spacingLg)

        TravelPrimaryButton(
            textRes = R.string.register_action,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(AuthTestTags.REGISTER_SUBMIT_BUTTON),
            enabled = isRegisterEnabled,
            trailingArrow = true,
            onClick = onRegisterClicked
        )

        TravelVerticalSpacer(Dimens.spacingSm)

        Text(
            text = stringResource(R.string.already_have_account),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToLogin() }
                .padding(vertical = Dimens.spacingSm)
        )

        TravelVerticalSpacer(Dimens.screenBottomPadding)
    }
}

@Composable
private fun Header(onBackClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.spacingSm)
    ) {
        TravelIconButton(
            iconRes = R.drawable.ic_arrow_back,
            contentDescription = null,
            iconTint = MaterialTheme.colorScheme.onBackground,
            onClick = onBackClicked
        )
        Spacer(Modifier.width(Dimens.spacingSm))
        Text(
            text = stringResource(R.string.register_step_meta),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TermsRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
        modifier = Modifier.fillMaxWidth()
    ) {
        TravelCheckbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = stringResource(R.string.accept_terms),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true, name = "Register - filled")
@Composable
private fun RegisterContentPreview() {
    TravelingAppTheme {
        var username by remember { mutableStateOf("isabel.morais") }
        var password by remember { mutableStateOf("Admin123!") }
        var confirmPassword by remember { mutableStateOf("Admin123!") }
        var acceptedTerms by remember { mutableStateOf(true) }
        RegisterContent(
            username = username,
            password = password,
            confirmPassword = confirmPassword,
            acceptedTerms = acceptedTerms,
            passwordsMatch = password == confirmPassword,
            isRegisterEnabled = username.isNotBlank() &&
                password.length >= 8 &&
                password == confirmPassword &&
                acceptedTerms,
            onBackClicked = {},
            onUsernameChanged = { username = it },
            onPasswordChanged = { password = it },
            onConfirmPasswordChanged = { confirmPassword = it },
            onTermsChanged = { acceptedTerms = it },
            onRegisterClicked = {},
            onNavigateToLogin = {}
        )
    }
}

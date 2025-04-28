package org.example.travelingapp.ui.views.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.example.travelingapp.feature.auth.R
import org.example.travelingapp.ui.theme.Black
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.Gray
import org.example.travelingapp.ui.theme.White
import org.example.travelingapp.ui.views.auth.viewmodel.AuthViewModel
import org.example.travelingapp.ui.views.components.LoginButton
import org.example.travelingapp.ui.views.components.AppImage
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.AppTextField
import org.example.travelingapp.ui.views.components.VerticalSpacer
import androidx.compose.runtime.getValue
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .testTag(AuthTestTags.LOGIN_SCREEN)
            .padding(Dimens.medium),
        horizontalAlignment = Alignment.Start
    ) {
        AppImage(
            resId = R.drawable.login_nomads_city_tour,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentScale = ContentScale.Fit
        )

        VerticalSpacer(Dimens.medium)

        AppText(
            textRes = R.string.login,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 10.dp, top = 20.dp)
        )

        VerticalSpacer(Dimens.medium)

        AppTextField(
            value = username,
            onValueChange = { newUsername ->
                authViewModel.onUsernameChanged(newUsername)
            },
            labelRes = R.string.username,
            modifier = Modifier.testTag(AuthTestTags.LOGIN_USERNAME_FIELD)
        )

        VerticalSpacer(Dimens.medium)

        AppTextField(
            value = password,
            onValueChange = { newPass ->
                authViewModel.onPasswordChanged(newPass)
            },
            labelRes = R.string.password,
            isPassword = true,
            trailingIconRes = R.drawable.login_ic_lock,
            keyboardType = KeyboardType.Password,
            modifier = Modifier.testTag(AuthTestTags.LOGIN_PASSWORD_FIELD)
        )

        VerticalSpacer(Dimens.large)

        LoginButton(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(AuthTestTags.LOGIN_SUBMIT_BUTTON),
            textRes = R.string.login,
            iconRes = R.drawable.common_ic_arrow_right,
            iconSize = 24.dp,
            iconOffsetY = 1.dp,
            textStyle = MaterialTheme.typography.titleLarge,
            textColor = White,
            spacing = 8.dp,
            shape = RoundedCornerShape(5.dp),
            borderStroke = BorderStroke(2.dp, Black),
            defaultContainerColor = Black,
            pressedContainerColor = Gray,
            defaultContentColor = White,
            isEnabled = isLoginEnabled
        ) {
            authViewModel.login(
                onSuccess = {
                    onNavigateToHome()
                },
                onError = { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            )
        }

        VerticalSpacer(Dimens.medium)

        AppText(
            textRes = R.string.forgot_password,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Toast.makeText(
                        context,
                        availableSoonText,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .padding(vertical = 8.dp)
        )

        AppText(
            textRes = R.string.create_account_text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(AuthTestTags.LOGIN_CREATE_ACCOUNT_ACTION)
                .clickable {
                    onNavigateToRegister()
                }
                .padding(vertical = 8.dp)
        )
    }
}

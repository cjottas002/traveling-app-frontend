package org.example.travelingapp.ui.views.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.example.travelingapp.feature.auth.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.views.auth.viewmodel.AuthViewModel
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.AppTextField
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
                .padding(horizontal = Dimens.spacingLg),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            AppText(
                textRes = R.string.login,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = Dimens.spacingMd)
            )

            Surface(
                shape = RoundedCornerShape(Dimens.radiusLg),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                tonalElevation = Dimens.elevationMd,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(Dimens.spacingLg)) {
                    AppTextField(
                        value = username,
                        onValueChange = { authViewModel.onUsernameChanged(it) },
                        labelRes = R.string.username,
                        modifier = Modifier.testTag(AuthTestTags.LOGIN_USERNAME_FIELD)
                    )

                    VerticalSpacer(Dimens.spacingMd)

                    AppTextField(
                        value = password,
                        onValueChange = { authViewModel.onPasswordChanged(it) },
                        labelRes = R.string.password,
                        isPassword = true,
                        trailingIconRes = R.drawable.login_ic_lock,
                        keyboardType = KeyboardType.Password,
                        modifier = Modifier.testTag(AuthTestTags.LOGIN_PASSWORD_FIELD)
                    )

                    VerticalSpacer(Dimens.spacingLg)

                    TravelPrimaryButton(
                        textRes = R.string.login,
                        enabled = isLoginEnabled,
                        modifier = Modifier.testTag(AuthTestTags.LOGIN_SUBMIT_BUTTON),
                        onClick = {
                            authViewModel.login(
                                onSuccess = { onNavigateToHome() },
                                onError = { msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
                }
            }

            VerticalSpacer(Dimens.spacingMd)

            AppText(
                textRes = R.string.forgot_password,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast.makeText(context, availableSoonText, Toast.LENGTH_SHORT).show()
                    }
                    .padding(vertical = Dimens.spacingSm)
            )

            AppText(
                textRes = R.string.create_account_text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(AuthTestTags.LOGIN_CREATE_ACCOUNT_ACTION)
                    .clickable { onNavigateToRegister() }
                    .padding(vertical = Dimens.spacingSm)
            )

            VerticalSpacer(Dimens.spacingMd)
        }
    }
}

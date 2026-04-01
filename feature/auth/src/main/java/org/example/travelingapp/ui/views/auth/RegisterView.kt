package org.example.travelingapp.ui.views.auth

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import org.example.travelingapp.feature.auth.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.views.auth.viewmodel.AuthViewModel
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.AppTextField
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.VerticalSpacer
import org.example.travelingapp.ui.testtags.AuthTestTags

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(AuthTestTags.REGISTER_SCREEN)
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
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.spacingLg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.spacingSm),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(Dimens.iconXl)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            VerticalSpacer(Dimens.spacingLg)

            AppText(
                textRes = R.string.register_text,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            VerticalSpacer(Dimens.spacingXs)
            AppText(
                textRes = R.string.register_subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )

            VerticalSpacer(Dimens.spacingLg)

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
                        leadingIconRes = R.drawable.login_ic_person,
                        modifier = Modifier.testTag(AuthTestTags.REGISTER_NAME_FIELD)
                    )

                    VerticalSpacer(Dimens.spacingMd)

                    AppTextField(
                        value = password,
                        onValueChange = { authViewModel.onPasswordChanged(it) },
                        labelRes = R.string.password,
                        isPassword = true,
                        trailingIconRes = R.drawable.login_ic_lock,
                        keyboardType = KeyboardType.Password,
                        modifier = Modifier.testTag(AuthTestTags.REGISTER_LASTNAME_FIELD)
                    )

                    VerticalSpacer(Dimens.spacingMd)

                    AppTextField(
                        value = confirmPassword,
                        onValueChange = { authViewModel.onConfirmPasswordChanged(it) },
                        labelRes = R.string.confirm_password,
                        isPassword = true,
                        trailingIconRes = R.drawable.login_ic_lock,
                        keyboardType = KeyboardType.Password,
                        modifier = Modifier.testTag(AuthTestTags.REGISTER_AGE_FIELD)
                    )

                    if (confirmPassword.isNotBlank() && !passwordsMatch) {
                        VerticalSpacer(Dimens.spacingXs)
                        AppText(
                            textRes = R.string.passwords_dont_match,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    VerticalSpacer(Dimens.spacingMd)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = acceptedTerms,
                            onCheckedChange = { authViewModel.onTermsChanged(it) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        AppText(
                            textRes = R.string.accept_terms,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    VerticalSpacer(Dimens.spacingMd)

                    TravelPrimaryButton(
                        textRes = R.string.sign_me_up,
                        modifier = Modifier.testTag(AuthTestTags.REGISTER_SUBMIT_BUTTON),
                        enabled = isRegisterEnabled,
                        onClick = {
                            authViewModel.register(
                                onSuccess = { onNavigateToLogin() },
                                onError = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
                }
            }

            VerticalSpacer(Dimens.spacingMd)

            AppText(
                textRes = R.string.already_have_account,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToLogin() }
                    .padding(vertical = Dimens.spacingSm)
            )

            PrivacyPolicyText(context)

            VerticalSpacer(Dimens.spacingMd)
        }
    }
}

@Composable
private fun PrivacyPolicyText(context: Context) {
    AppText(
        textRes = R.string.privacy_policy,
        style = MaterialTheme.typography.bodySmall,
        color = Color.White.copy(alpha = 0.6f),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val url = "https://developers.google.com/ml-kit/terms"
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                context.startActivity(intent)
            }
            .padding(vertical = Dimens.spacingSm)
    )
}

package org.example.travelingapp.ui.views.auth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import org.example.travelingapp.feature.auth.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.views.auth.viewmodel.AuthViewModel
import org.example.travelingapp.ui.views.components.AppIconButton
import org.example.travelingapp.ui.views.components.AppImage
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.AppTextField
import org.example.travelingapp.ui.views.components.AppToolBar
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.VerticalSpacer
import org.example.travelingapp.ui.testtags.AuthTestTags

@SuppressLint("ContextCastToActivity")
@Composable
fun RegisterView(navController: NavController, onNavigateToLogin: () -> Unit) {
    Scaffold(
        topBar = {
            AppToolBar(
                showBack = true,
                titleRes = R.string.register_text,
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .testTag(AuthTestTags.REGISTER_SCREEN)
                .padding(Dimens.spacingMd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImagePicker(LocalContext.current)
            VerticalSpacer(Dimens.spacingMd)
            TextsAndButton(onNavigateToLogin)
            VerticalSpacer(Dimens.spacingMd)
            PrivacyPolicyText(LocalContext.current)
        }
    }
}

@Composable
fun ProfileImagePicker(context: Context) {
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val takePicturePreview = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        photoBitmap = bitmap
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            takePicturePreview.launch(null)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.camera_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(modifier = Modifier.size(Dimens.avatarLg)) {
        AppImage(
            bitmap = photoBitmap?.asImageBitmap(),
            resId = R.drawable.register_ic_profile,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        AppIconButton(
            iconRes = R.drawable.register_ic_cam,
            contentDescription = stringResource(R.string.content_description_take_photo),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(Dimens.iconXl),
            iconSize = Dimens.iconXl
        ) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextsAndButton(
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val name by authViewModel.username.collectAsState()
    val lastName by authViewModel.password.collectAsState()
    val isRegisterEnabled by authViewModel.isRegisterEnabled.collectAsState()
    val context = LocalContext.current

    val ageOptions = (18..100).map { it.toString() }
    var expanded by remember { mutableStateOf(false) }
    val age by remember { mutableStateOf(ageOptions[0]) }

    AppTextField(
        value = name,
        onValueChange = { authViewModel.onUsernameChanged(it) },
        labelRes = R.string.whats_your_name,
        leadingIconRes = R.drawable.login_ic_person,
        modifier = Modifier.testTag(AuthTestTags.REGISTER_NAME_FIELD)
    )

    VerticalSpacer(Dimens.spacingSm)

    AppTextField(
        value = lastName,
        onValueChange = { authViewModel.onPasswordChanged(it) },
        labelRes = R.string.whats_your_lastname,
        leadingIconRes = R.drawable.login_ic_person,
        modifier = Modifier.testTag(AuthTestTags.REGISTER_LASTNAME_FIELD)
    )

    VerticalSpacer(Dimens.spacingSm)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        AppTextField(
            value = age,
            onValueChange = { },
            labelRes = R.string.age,
            readOnly = true,
            trailingIconRes = R.drawable.onboarding3_arrow_down,
            modifier = Modifier
                .testTag(AuthTestTags.REGISTER_AGE_FIELD)
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            ageOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        authViewModel.setIsAdult(option.toInt() >= 18)
                        expanded = false
                    }
                )
            }
        }
    }

    VerticalSpacer(Dimens.spacingLg)

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

@Composable
private fun PrivacyPolicyText(context: Context) {
    AppText(
        textRes = R.string.privacy_policy,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
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

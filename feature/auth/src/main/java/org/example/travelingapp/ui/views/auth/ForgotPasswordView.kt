package org.example.travelingapp.ui.views.auth

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import org.example.travelingapp.feature.auth.R
import org.example.travelingapp.ui.testtags.AuthTestTags
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.components.TravelBannerTone
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
import org.example.travelingapp.ui.views.components.TravelIconButton
import org.example.travelingapp.ui.views.components.TravelInlineBanner
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.TravelTextField
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer

@Composable
fun ForgotPasswordView(
    onBackClicked: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var linkSent by remember { mutableStateOf(false) }

    ForgotPasswordContent(
        email = email,
        linkSent = linkSent,
        onEmailChanged = {
            email = it
            linkSent = false
        },
        onSendClicked = { linkSent = true },
        onBackClicked = onBackClicked
    )
}

@Composable
fun ForgotPasswordContent(
    email: String,
    linkSent: Boolean,
    onEmailChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.screenPadding)
            .testTag(AuthTestTags.FORGOT_PASSWORD_SCREEN)
    ) {
        Header(onBackClicked = onBackClicked)

        TravelVerticalSpacer(Dimens.spacingXl)

        TravelEditorialBlock(
            kicker = stringResource(R.string.forgot_password_kicker),
            title = stringResource(R.string.forgot_password_title),
            accent = stringResource(R.string.forgot_password_title_accent),
            sub = stringResource(R.string.forgot_password_subtitle)
        )

        TravelVerticalSpacer(Dimens.sectionSpacing)

        TravelTextField(
            value = email,
            onValueChange = onEmailChanged,
            labelRes = R.string.email,
            keyboardType = KeyboardType.Email,
            modifier = Modifier.testTag(AuthTestTags.FORGOT_PASSWORD_EMAIL_FIELD)
        )

        if (linkSent) {
            TravelVerticalSpacer(Dimens.spacingLg)
            TravelInlineBanner(
                title = stringResource(R.string.forgot_password_sent_title),
                body = stringResource(R.string.forgot_password_sent_body),
                tone = TravelBannerTone.Synced
            )
        }

        TravelVerticalSpacer(Dimens.sectionSpacing)

        TravelPrimaryButton(
            textRes = if (linkSent) {
                R.string.forgot_password_resend_action
            } else {
                R.string.forgot_password_send_action
            },
            enabled = email.isNotBlank(),
            trailingArrow = true,
            onClick = onSendClicked,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(AuthTestTags.FORGOT_PASSWORD_SUBMIT_BUTTON)
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
            text = stringResource(R.string.forgot_password_step_meta),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, name = "Forgot password - empty")
@Composable
private fun ForgotPasswordEmptyPreview() {
    TravelingAppTheme {
        ForgotPasswordContent(
            email = "",
            linkSent = false,
            onEmailChanged = {},
            onSendClicked = {},
            onBackClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Forgot password - sent")
@Composable
private fun ForgotPasswordSentPreview() {
    TravelingAppTheme {
        ForgotPasswordContent(
            email = "isabel.morais@hey.com",
            linkSent = true,
            onEmailChanged = {},
            onSendClicked = {},
            onBackClicked = {}
        )
    }
}

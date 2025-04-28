package org.example.travelingapp.ui.views.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.travelingapp.core.ui.R
import org.example.travelingapp.ui.theme.Black
import org.example.travelingapp.ui.theme.Gray
import org.example.travelingapp.ui.theme.RegisterButtonYellowColor
import org.example.travelingapp.ui.theme.RegisterButtonYellowColorPressed
import org.example.travelingapp.ui.theme.White

@Composable
fun SkipButton(modifier: Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, Color.Black),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        interactionSource = interactionSource
    ) {
        Text(
            text = stringResource(R.string.skip),
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun NextButton(modifier: Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(5.dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) Gray else Black,
            contentColor = White
        )
    ) {
        Text(
            text = stringResource(R.string.next),
            color = White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int = R.string.login,
    @DrawableRes iconRes: Int = R.drawable.onboarding3_arrow_down,
    iconSize: Dp = 24.dp,
    iconOffsetY: Dp = (-4).dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textColor: Color = Color.Black,
    spacing: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(3.dp),
    borderStroke: BorderStroke = BorderStroke(2.dp, Color.Black),
    defaultContainerColor: Color = Color.White,
    pressedContainerColor: Color = Gray,
    defaultContentColor: Color = Color.Black,
    isEnabled: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(60.dp),
        shape = shape,
        border = borderStroke,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isPressed) pressedContainerColor else defaultContainerColor,
            contentColor = defaultContentColor
        ),
        interactionSource = interactionSource,
        enabled = isEnabled
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(textRes),
                style = textStyle,
                color = textColor
            )
            Spacer(Modifier.width(spacing))
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .offset(y = iconOffsetY)
            )
        }
    }
}

@Composable
fun RegisterButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(5.dp),
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    textAlign: TextAlign = TextAlign.Center,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(60.dp),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) RegisterButtonYellowColorPressed else RegisterButtonYellowColor,
            disabledContainerColor = RegisterButtonYellowColor.copy(alpha = 0.6f),
            contentColor = White
        ),
    ) {
        Text(
            text = stringResource(textRes),
            style = textStyle,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )
    }
}


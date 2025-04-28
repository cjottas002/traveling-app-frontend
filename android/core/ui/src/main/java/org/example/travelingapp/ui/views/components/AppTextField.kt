package org.example.travelingapp.ui.views.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.example.travelingapp.ui.theme.BoxStrokeColor
import org.example.travelingapp.ui.theme.TextInputBackground

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelRes: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    focusedBorderColor: Color = BoxStrokeColor,
    unfocusedBorderColor: Color = BoxStrokeColor,
    disabledContainerColor: Color = TextInputBackground,
    disabledBorderColor: Color = BoxStrokeColor,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    readOnly: Boolean = false,
    @DrawableRes leadingIconRes: Int? = null,
    @DrawableRes trailingIconRes: Int? = null
) {
    val visualTransformation: VisualTransformation =
        if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        readOnly = readOnly,
        enabled = true,
        label = { Text(stringResource(labelRes)) },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = leadingIconRes?.let { iconRes ->
            {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null
                )
            }
        },
        trailingIcon = trailingIconRes?.let { iconRes ->
            {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = disabledContainerColor,
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            disabledBorderColor = disabledBorderColor
        )
    )
}

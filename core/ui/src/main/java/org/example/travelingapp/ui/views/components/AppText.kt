package org.example.travelingapp.ui.views.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit


@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val finalStyle = style.copy(
        fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
        fontWeight = fontWeight ?: style.fontWeight
    )
    Text(
        text = text,
        modifier = modifier,
        style = finalStyle,
        color = color,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}


@Composable
fun AppText(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    AppText(
        text = stringResource(textRes),
        modifier = modifier,
        style = style,
        color = color,
        textAlign = textAlign,
        fontSize = fontSize,
        fontWeight = fontWeight,
        maxLines = maxLines,
        overflow = overflow
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Text styles")
@Composable
private fun AppTextPreview() {
    org.example.travelingapp.ui.theme.TravelingAppTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(org.example.travelingapp.ui.theme.Dimens.screenPadding)
        ) {
            AppText(
                text = "Headline Medium",
                style = MaterialTheme.typography.headlineMedium
            )
            AppText(
                text = "Title Large",
                style = MaterialTheme.typography.titleLarge
            )
            AppText(
                text = "Body Large — the quick brown fox jumps over the lazy dog.",
                style = MaterialTheme.typography.bodyLarge
            )
            AppText(
                text = "Body Medium — smaller support text.",
                style = MaterialTheme.typography.bodyMedium
            )
            AppText(
                text = "Label Small caption",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

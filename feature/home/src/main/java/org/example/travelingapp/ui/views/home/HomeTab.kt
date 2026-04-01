package org.example.travelingapp.ui.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import org.example.travelingapp.domain.entities.Destination
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.VerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.DestinationViewModel

@Composable
fun HomeTab(viewModel: DestinationViewModel = hiltViewModel()) {
    val destinations by viewModel.destinations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.spacingMd)
    ) {
        item {
            VerticalSpacer(Dimens.spacingMd)
            AppText(
                text = "\uD83C\uDF0D",
                style = MaterialTheme.typography.headlineLarge
            )
            VerticalSpacer(Dimens.spacingXs)
            AppText(
                textRes = R.string.upcoming_meetups,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            VerticalSpacer(Dimens.spacingLg)
        }

        if (isLoading && destinations.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(Dimens.spacingXl),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        if (destinations.isEmpty() && !isLoading) {
            item {
                AppText(
                    text = stringResource(R.string.coming_soon_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(Dimens.spacingMd)
                )
            }
        }

        items(destinations, key = { it.id }) { destination ->
            DestinationCard(destination = destination)
        }
    }
}

@Composable
fun DestinationCard(destination: Destination) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.spacingMd),
        shape = RoundedCornerShape(Dimens.radiusLg),
        elevation = CardDefaults.cardElevation(Dimens.elevationSm),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            if (destination.imageUrl.isNotBlank()) {
                val imageModifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.cardImageHeight)

                if (destination.imageUrl.startsWith("local:")) {
                    val resName = destination.imageUrl.removePrefix("local:")
                    val context = LocalContext.current
                    val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
                    if (resId != 0) {
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = destination.name,
                            modifier = imageModifier,
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    AsyncImage(
                        model = destination.imageUrl,
                        contentDescription = destination.name,
                        modifier = imageModifier,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(modifier = Modifier.padding(Dimens.spacingMd)) {
                AppText(
                    text = destination.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                VerticalSpacer(Dimens.spacingXs)

                AppText(
                    text = "${destination.country} · ${destination.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (destination.description.isNotBlank()) {
                    VerticalSpacer(Dimens.spacingXs)
                    AppText(
                        text = destination.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

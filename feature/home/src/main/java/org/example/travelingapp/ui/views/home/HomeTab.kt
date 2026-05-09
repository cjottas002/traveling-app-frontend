package org.example.travelingapp.ui.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import org.example.travelingapp.domain.entities.Destination
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Alpha
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelGradients
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
import org.example.travelingapp.ui.views.components.TravelFab
import org.example.travelingapp.ui.views.components.TravelLoader
import org.example.travelingapp.ui.views.components.TravelScaffold
import org.example.travelingapp.ui.views.components.TravelText
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.DestinationViewModel
import java.time.LocalTime

@Composable
fun HomeTab(
    isAdmin: Boolean = false,
    onCreateDestination: () -> Unit = {},
    onDestinationClick: (String) -> Unit = {},
    viewModel: DestinationViewModel = hiltViewModel()
) {
    val destinations by viewModel.destinations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val username by viewModel.username.collectAsState()

    HomeTabContent(
        destinations = destinations,
        isLoading = isLoading,
        username = username,
        isAdmin = isAdmin,
        onCreateDestination = onCreateDestination,
        onDestinationClick = onDestinationClick
    )
}

@Composable
private fun HomeTabContent(
    destinations: List<Destination>,
    isLoading: Boolean,
    username: String?,
    isAdmin: Boolean,
    onCreateDestination: () -> Unit,
    onDestinationClick: (String) -> Unit
) {
    TravelScaffold(
        floatingActionButton = {
            if (isAdmin) {
                TravelFab(
                    onClick = onCreateDestination,
                    icon = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.create_destination)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.screenPadding)
        ) {
            item {
                GreetingHeader(username = username)
                TravelVerticalSpacer(Dimens.spacingXl)
                TravelEditorialBlock(
                    kicker = stringResource(R.string.home_kicker),
                    title = stringResource(R.string.home_title),
                    accent = stringResource(R.string.home_title_accent)
                )
                TravelVerticalSpacer(Dimens.spacingLg)
            }

            val featured = destinations.firstOrNull()
            if (featured != null) {
                item {
                    FeaturedDestinationCard(
                        destination = featured,
                        onClick = { onDestinationClick(featured.id) }
                    )
                    TravelVerticalSpacer(Dimens.sectionSpacing)
                }
            }

            if (destinations.size > 1) {
                item {
                    SectionHeader(label = stringResource(R.string.home_section_nearby))
                    TravelVerticalSpacer(Dimens.spacingMd)
                }
            }

            if (isLoading && destinations.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(Dimens.spacingXl),
                        contentAlignment = Alignment.Center
                    ) { TravelLoader() }
                }
            }

            if (destinations.isEmpty() && !isLoading) {
                item {
                    TravelText(
                        text = stringResource(R.string.no_destinations),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = Dimens.spacingLg)
                    )
                }
            }

            items(destinations.drop(1), key = { it.id }) { destination ->
                DestinationRow(
                    destination = destination,
                    onClick = { onDestinationClick(destination.id) }
                )
            }

            item { TravelVerticalSpacer(Dimens.screenBottomPadding) }
        }
    }
}

@Composable
private fun GreetingHeader(username: String?) {
    val greetingRes = when (LocalTime.now().hour) {
        in 5..11  -> R.string.greeting_morning
        in 12..20 -> R.string.greeting_afternoon
        in 21..23, in 0..4 -> R.string.greeting_evening
        else -> R.string.greeting_default
    }
    val displayName = username?.takeIf { it.isNotBlank() }
        ?.substringBefore('.')
        ?.replaceFirstChar { it.uppercase() }
        ?: stringResource(R.string.greeting_default)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.spacingMd)
    ) {
        Column {
            Text(
                text = stringResource(greetingRes).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(Dimens.spacingXxs))
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Avatar(displayName = displayName)
    }
}

@Composable
private fun Avatar(displayName: String) {
    val initial = displayName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    Box(
        modifier = Modifier
            .size(Dimens.avatarSm)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSecondary
            )
        )
    }
}

@Composable
private fun SectionHeader(label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "— ${label.uppercase()}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(R.string.home_view_all).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

/** Big ink hero card for the first destination (the editorial "featured"). */
@Composable
private fun FeaturedDestinationCard(destination: Destination, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(Dimens.radiusMd))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() }
    ) {
        DestinationImage(
            imageUrl = destination.imageUrl,
            contentDescription = destination.name,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TravelGradients.heroOverlay)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.spacingMd),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = destination.country.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.height(Dimens.spacingXxs))
            Text(
                text = destination.name,
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
            )
            if (destination.category.isNotBlank()) {
                Spacer(Modifier.height(Dimens.spacingXxs))
                Text(
                    text = destination.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = Alpha.muted)
                )
            }
        }
    }
}

/** Compact row for non-featured destinations. */
@Composable
private fun DestinationRow(destination: Destination, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = Dimens.spacingMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.avatarMd)
                .clip(RoundedCornerShape(Dimens.radiusXs))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            DestinationImage(
                imageUrl = destination.imageUrl,
                contentDescription = destination.name,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.size(Dimens.spacingMd))
        Column(modifier = Modifier.fillMaxHeight()) {
            Text(
                text = destination.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(Dimens.spacingXxs))
            Text(
                text = "${destination.country} · ${destination.category}".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Renders either a remote URL via Coil or a local drawable via "local:" prefix. */
@Composable
private fun DestinationImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    if (imageUrl.isBlank()) return
    if (imageUrl.startsWith("local:")) {
        val resName = imageUrl.removePrefix("local:")
        val context = LocalContext.current
        val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
        if (resId != 0) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true, name = "Home - explore")
@Composable
private fun HomeTabContentPreview() {
    org.example.travelingapp.ui.theme.TravelingAppTheme {
        HomeTabContent(
            destinations = listOf(
                Destination(
                    id = "atlas",
                    name = "Atlas en otoño",
                    description = "Montañas, riads y rutas lentas.",
                    country = "Morocco",
                    imageUrl = "local:destination_alps",
                    category = "Mountain",
                    createdBy = "preview",
                    createdAt = 0L,
                    updatedAt = 0L
                ),
                Destination(
                    id = "cancun",
                    name = "Cancun coast",
                    description = "A bright coastline for slow days.",
                    country = "Mexico",
                    imageUrl = "local:destination_cancun",
                    category = "Beach",
                    createdBy = "preview",
                    createdAt = 0L,
                    updatedAt = 0L
                )
            ),
            isLoading = false,
            username = "isabel.morais",
            isAdmin = true,
            onCreateDestination = {},
            onDestinationClick = {}
        )
    }
}

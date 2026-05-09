package org.example.travelingapp.ui.views.home

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.example.travelingapp.domain.entities.Transport
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelMonoFamily
import org.example.travelingapp.ui.views.components.TravelChipRow
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
import org.example.travelingapp.ui.views.components.TravelFilterChip
import org.example.travelingapp.ui.views.components.TravelHairlineRow
import org.example.travelingapp.ui.views.components.TravelText
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.TransportViewModel

@Composable
fun TransportTab(transportViewModel: TransportViewModel = hiltViewModel()) {
    val transports by transportViewModel.transports.collectAsState(emptyList())
    val context = LocalContext.current

    TransportTabContent(
        transports = transports,
        onTransportClick = { transport ->
            Toast.makeText(context, transport.name, Toast.LENGTH_SHORT).show()
        }
    )
}

@Composable
private fun TransportTabContent(
    transports: List<Transport>,
    onTransportClick: (Transport) -> Unit
) {
    var selectedMode by rememberSaveable { mutableStateOf(TransportMode.Flight) }
    val filteredTransports = transports.filter { it.mode() == selectedMode }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.screenPadding)
    ) {
        item {
            TravelVerticalSpacer(Dimens.spacingMd)
            TravelEditorialBlock(
                kicker = stringResource(R.string.transport_kicker),
                title = stringResource(R.string.transport_title),
                accent = stringResource(R.string.transport_title_accent),
                sub = stringResource(R.string.transport_results_count, filteredTransports.size)
            )
            TravelVerticalSpacer(Dimens.spacingLg)
            TransportFilters(
                selectedMode = selectedMode,
                onModeSelected = { selectedMode = it }
            )
            TravelVerticalSpacer(Dimens.spacingMd)
        }

        if (filteredTransports.isEmpty()) {
            item {
                TravelText(
                    text = stringResource(R.string.transport_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.spacingLg)
                )
            }
        }

        items(filteredTransports, key = { it.name }) { transport ->
            TransportRow(transport = transport) { onTransportClick(transport) }
        }

        item { TravelVerticalSpacer(Dimens.screenBottomPadding) }
    }
}

@Composable
private fun TransportFilters(
    selectedMode: TransportMode,
    onModeSelected: (TransportMode) -> Unit
) {
    TravelChipRow {
        TransportMode.entries.forEach { mode ->
            TravelFilterChip(
                textRes = mode.labelRes,
                selected = selectedMode == mode,
                onClick = { onModeSelected(mode) }
            )
        }
    }
}

/** Dense editorial transport row: 44dp bone icon · title · meta mono · ember mono price right. */
@Composable
private fun TransportRow(transport: Transport, onClick: () -> Unit) {
    val mode = transport.mode()

    TravelHairlineRow(
        onClick = onClick,
        leading = {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(Dimens.radiusXs))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = transport.imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit
                )
            }
        },
        trailing = {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = transport.price,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = TravelMonoFamily),
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = stringResource(R.string.transport_price_label).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    ) {
        Text(
            text = transport.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "${stringResource(mode.labelRes)} · ${stringResource(R.string.transport_direct_route)}".uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = TravelMonoFamily),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private enum class TransportMode(@param:StringRes val labelRes: Int) {
    Flight(R.string.transport_filter_flights),
    Train(R.string.transport_filter_trains),
    Bus(R.string.transport_filter_buses)
}

private fun Transport.mode(): TransportMode {
    val routeText = name.lowercase()
    return when {
        routeText.contains("train") || routeText.contains("tren") -> TransportMode.Train
        routeText.contains("bus") || routeText.contains("coach") -> TransportMode.Bus
        else -> TransportMode.Flight
    }
}

@Preview(showBackground = true, name = "Transport")
@Composable
private fun TransportTabContentPreview() {
    org.example.travelingapp.ui.theme.TravelingAppTheme {
        TransportTabContent(
            transports = listOf(
                Transport(
                    name = "Madrid -> Marrakech",
                    imageRes = R.drawable.common_ic_castle,
                    price = "€ 184"
                ),
                Transport(
                    name = "Barcelona -> Casablanca",
                    imageRes = R.drawable.common_ic_castle,
                    price = "€ 96"
                ),
                Transport(
                    name = "Train Rabat -> Fes",
                    imageRes = R.drawable.common_ic_castle,
                    price = "€ 24"
                ),
                Transport(
                    name = "Bus Marrakech -> Essaouira",
                    imageRes = R.drawable.common_ic_castle,
                    price = "€ 12"
                )
            ),
            onTransportClick = {}
        )
    }
}

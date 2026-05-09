package org.example.travelingapp.ui.views.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
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
                accent = stringResource(R.string.transport_title_accent)
            )
            TravelVerticalSpacer(Dimens.spacingLg)
        }

        items(transports) { transport ->
            TransportRow(transport = transport) { onTransportClick(transport) }
        }

        item { TravelVerticalSpacer(Dimens.screenBottomPadding) }
    }
}

/** Dense editorial transport row: 44dp bone icon · title · meta mono · ember mono price right. */
@Composable
private fun TransportRow(transport: Transport, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = Dimens.spacingMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        Spacer(Modifier.size(Dimens.spacingMd))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transport.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = transport.price,
            style = MaterialTheme.typography.titleMedium.copy(fontFamily = TravelMonoFamily),
            color = MaterialTheme.colorScheme.secondary
        )
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
                )
            ),
            onTransportClick = {}
        )
    }
}

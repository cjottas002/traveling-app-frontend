package org.example.travelingapp.ui.views.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.example.travelingapp.domain.entities.Transport
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.views.components.AppImage
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.VerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.TransportViewModel

@Composable
fun TransportTab(transportViewModel: TransportViewModel = hiltViewModel()) {
    val transports by transportViewModel.transports.collectAsState(emptyList())
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        items(transports) { transport ->
            TransportItem(transport = transport) {
                Toast.makeText(context, "Selected: ${transport.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun TransportItem(transport: Transport, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.spacingSm)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(start = Dimens.spacingLg)) {
                AppText(text = transport.name, style = MaterialTheme.typography.titleLarge)
                VerticalSpacer(Dimens.spacingMd)
                AppText(text = transport.price, style = MaterialTheme.typography.bodyMedium)
            }
            AppImage(
                resId = transport.imageRes,
                contentDescription = null,
                modifier = Modifier
                    .height(90.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}

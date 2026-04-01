package org.example.travelingapp.ui.views.home

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import org.example.travelingapp.domain.entities.hotelmodel.Hotel
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.VerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.HotelViewModel

@Composable
fun HotelTab(hotelViewModel: HotelViewModel = hiltViewModel()) {
    val hotels by hotelViewModel.hotels.collectAsState(emptyList())
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.spacingSm)
    ) {
        items(hotels, key = { it.id }) { hotel ->
            HotelItem(hotel) {
                Toast.makeText(
                    context,
                    hotel.address.locality,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun HotelItem(hotel: Hotel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.spacingSm)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.radiusLg),
        elevation = CardDefaults.cardElevation(Dimens.elevationMd),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(Dimens.spacingMd)) {
            AsyncImage(
                model = hotel.optimizedThumbUrls.srpDesktop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.cardImageHeight)
                    .clip(RoundedCornerShape(Dimens.radiusMd))
            )
            VerticalSpacer(Dimens.spacingSm)
            AppText(
                text = hotel.address.streetAddress,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = Bold
            )
            AppText(
                text = hotel.address.locality,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppText(
                text = "${hotel.ratePlan.price.current} €",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = Bold
            )
            AppText(
                text = hotel.guestReviews.rating,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

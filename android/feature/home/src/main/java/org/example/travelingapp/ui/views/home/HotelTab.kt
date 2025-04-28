package org.example.travelingapp.ui.views.home


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import org.example.travelingapp.domain.entities.hotelmodel.Hotel
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.ToolbarColor
import org.example.travelingapp.ui.theme.White
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.VerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.HotelViewModel

@Composable
fun HotelTab(hotelViewModel: HotelViewModel = hiltViewModel()) {
    val hotels by hotelViewModel.hotels.collectAsState(emptyList())
    val context = LocalContext.current

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        items(hotels, key = { it.id }) { hotel ->
            HotelItem(hotel) {
                Toast.makeText(
                    context,
                    "Selected hotel: ${hotel.address.locality}",
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
            .padding(vertical = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(Dimens.small),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = hotel.optimizedThumbUrls.srpDesktop,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            VerticalSpacer(Dimens.small)
            AppText(
                text = hotel.address.streetAddress,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = Bold
            )
            AppText(
                text = hotel.address.locality,
                style = MaterialTheme.typography.bodyMedium
            )
            AppText(
                text = "${hotel.ratePlan.price.current} €",
                style = MaterialTheme.typography.titleLarge,
                color = ToolbarColor,
                fontWeight = Bold

            )
            AppText(
                text = "Rating: ${hotel.guestReviews.rating}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

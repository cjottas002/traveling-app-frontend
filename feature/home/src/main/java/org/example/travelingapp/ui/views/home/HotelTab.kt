package org.example.travelingapp.ui.views.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import org.example.travelingapp.domain.entities.hotelmodel.Address
import org.example.travelingapp.domain.entities.hotelmodel.Coordinate
import org.example.travelingapp.domain.entities.hotelmodel.Features
import org.example.travelingapp.domain.entities.hotelmodel.GuestReviews
import org.example.travelingapp.domain.entities.hotelmodel.Hotel
import org.example.travelingapp.domain.entities.hotelmodel.OptimizedThumbUrls
import org.example.travelingapp.domain.entities.hotelmodel.Price
import org.example.travelingapp.domain.entities.hotelmodel.RatePlan
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelMonoFamily
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
import org.example.travelingapp.ui.views.components.TravelHairlineRow
import org.example.travelingapp.ui.views.components.TravelLoader
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.HotelViewModel

@Composable
fun HotelTab(hotelViewModel: HotelViewModel = hiltViewModel()) {
    val hotels by hotelViewModel.hotels.collectAsState(emptyList())
    val context = LocalContext.current

    HotelTabContent(
        hotels = hotels,
        onHotelClick = { hotel ->
            Toast.makeText(context, hotel.address.locality, Toast.LENGTH_SHORT).show()
        }
    )
}

@Composable
private fun HotelTabContent(
    hotels: List<Hotel>,
    onHotelClick: (Hotel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.screenPadding)
    ) {
        item {
            TravelVerticalSpacer(Dimens.spacingMd)
            TravelEditorialBlock(
                kicker = stringResource(R.string.hotels_kicker),
                title = stringResource(R.string.hotels_title),
                accent = stringResource(R.string.hotels_title_accent)
            )
            TravelVerticalSpacer(Dimens.spacingLg)
        }

        items(hotels, key = { it.id }) { hotel ->
            HotelRow(hotel) { onHotelClick(hotel) }
        }

        item { TravelVerticalSpacer(Dimens.screenBottomPadding) }
    }
}

/** Editorial hotel row: 96dp thumb · title · location mono · price mono right-aligned. */
@Composable
private fun HotelRow(hotel: Hotel, onClick: () -> Unit) {
    val context = LocalContext.current
    TravelHairlineRow(
        onClick = onClick,
        leading = {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(hotel.optimizedThumbUrls.srpDesktop)
                    .crossfade(300)
                    .build(),
                contentDescription = hotel.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(Dimens.radiusXs))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                loading = {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        TravelLoader()
                    }
                }
            )
        },
        trailing = {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "€ ${hotel.ratePlan.price.current}",
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = TravelMonoFamily),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.hotels_per_night).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    ) {
        Text(
            text = hotel.address.streetAddress,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = hotel.address.locality.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "★ ${hotel.guestReviews.rating}",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = TravelMonoFamily),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, name = "Hotels")
@Composable
private fun HotelTabContentPreview() {
    org.example.travelingapp.ui.theme.TravelingAppTheme {
        HotelTabContent(
            hotels = listOf(
                previewHotel(
                    id = 1,
                    name = "Riad Yasmine",
                    street = "Riad Yasmine",
                    locality = "Marrakech · Medina",
                    price = "124",
                    rating = "4.8"
                ),
                previewHotel(
                    id = 2,
                    name = "Hotel Sahara View",
                    street = "Hotel Sahara View",
                    locality = "Merzouga · Desert",
                    price = "92",
                    rating = "4.9"
                )
            ),
            onHotelClick = {}
        )
    }
}

private fun previewHotel(
    id: Int,
    name: String,
    street: String,
    locality: String,
    price: String,
    rating: String
) = Hotel(
    id = id,
    name = name,
    starRating = 4,
    address = Address(
        streetAddress = street,
        extendedAddress = null,
        locality = locality,
        postalCode = "00000",
        region = "Preview",
        countryName = "Morocco",
        countryCode = "MA",
        obfuscate = false
    ),
    guestReviews = GuestReviews(
        unformattedRating = rating.toDouble(),
        rating = rating,
        total = 120,
        scale = 5,
        badge = null,
        badgeText = null
    ),
    ratePlan = RatePlan(
        price = Price(current = price, exactCurrent = price.toDouble(), old = null),
        features = Features(paymentPreference = false, noCCRequired = false)
    ),
    neighbourhood = "Preview",
    coordinate = Coordinate(lat = 0.0, lon = 0.0),
    providerType = "preview",
    supplierHotelId = id,
    isAlternative = false,
    optimizedThumbUrls = OptimizedThumbUrls(srpDesktop = "")
)

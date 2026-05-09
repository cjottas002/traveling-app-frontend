package org.example.travelingapp.ui.views.rentcar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelMonoFamily
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
import org.example.travelingapp.ui.views.components.TravelHairlineRow
import org.example.travelingapp.ui.views.components.TravelIconButton
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer

@Composable
fun RentCarView(navController: NavController) {
    RentCarContent(onBackClicked = { navController.popBackStack() })
}

@Composable
private fun RentCarContent(onBackClicked: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Dimens.screenPadding)
    ) {
        item {
            RentCarHeader(onBackClicked)
            TravelVerticalSpacer(Dimens.spacingMd)
            TravelEditorialBlock(
                kicker = stringResource(R.string.rentcar_kicker),
                title = stringResource(R.string.rentcar_title),
                accent = stringResource(R.string.rentcar_title_accent),
                sub = stringResource(R.string.rentcar_results_count, rentalCars.size)
            )
            TravelVerticalSpacer(Dimens.spacingLg)
        }

        items(rentalCars, key = { it.nameRes }) { car ->
            RentalCarRow(car)
        }

        item { TravelVerticalSpacer(Dimens.screenBottomPadding) }
    }
}

@Composable
private fun RentCarHeader(onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.spacingSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TravelIconButton(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            iconTint = MaterialTheme.colorScheme.onBackground,
            onClick = onBackClicked
        )
        Spacer(Modifier.width(Dimens.spacingSm))
        Text(
            text = stringResource(R.string.rent_a_car).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RentalCarRow(car: RentalCar) {
    TravelHairlineRow(
        onClick = {},
        leading = {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(Dimens.radiusXs))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(car.imageRes),
                    contentDescription = stringResource(car.nameRes),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimens.spacingXs),
                    contentScale = ContentScale.Fit
                )
            }
        },
        trailing = {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = car.price,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = TravelMonoFamily),
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = stringResource(R.string.rentcar_per_day).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    ) {
        Text(
            text = stringResource(car.nameRes),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(car.categoryRes).uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = TravelMonoFamily),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private data class RentalCar(
    @param:StringRes val nameRes: Int,
    @param:StringRes val categoryRes: Int,
    @param:DrawableRes val imageRes: Int,
    val price: String
)

private val rentalCars = listOf(
    RentalCar(
        nameRes = R.string.rentcar_classic_car,
        categoryRes = R.string.rentcar_category_classic,
        imageRes = R.drawable.pestania1_classiccar,
        price = "€ 34"
    ),
    RentalCar(
        nameRes = R.string.rentcar_electric_car,
        categoryRes = R.string.rentcar_category_electric,
        imageRes = R.drawable.pestania1_electriccar,
        price = "€ 45"
    ),
    RentalCar(
        nameRes = R.string.rentcar_flying_car,
        categoryRes = R.string.rentcar_category_premium,
        imageRes = R.drawable.pestania1_flyingcar,
        price = "€ 500"
    ),
    RentalCar(
        nameRes = R.string.rentcar_motorhome,
        categoryRes = R.string.rentcar_category_family,
        imageRes = R.drawable.pestania1_motorhome,
        price = "€ 23"
    ),
    RentalCar(
        nameRes = R.string.rentcar_pickup_car,
        categoryRes = R.string.rentcar_category_utility,
        imageRes = R.drawable.pestania1_pickupcar,
        price = "€ 10"
    ),
    RentalCar(
        nameRes = R.string.rentcar_sport_car,
        categoryRes = R.string.rentcar_category_sport,
        imageRes = R.drawable.pestania1_sportcart,
        price = "€ 55"
    )
)

@Preview(showBackground = true, name = "Rent car")
@Composable
private fun RentCarContentPreview() {
    TravelingAppTheme {
        RentCarContent(onBackClicked = {})
    }
}

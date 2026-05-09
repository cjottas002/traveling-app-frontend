package org.example.travelingapp.ui.views.home.destinations

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.components.TravelBannerTone
import org.example.travelingapp.ui.views.components.TravelDropdown
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
import org.example.travelingapp.ui.views.components.TravelInlineBanner
import org.example.travelingapp.ui.views.components.TravelLoader
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.TravelScaffold
import org.example.travelingapp.ui.views.components.TravelTextField
import org.example.travelingapp.ui.views.components.TravelToolBar
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.CreateDestinationViewModel

@Composable
fun CreateDestinationView(
    navController: NavController,
    viewModel: CreateDestinationViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val description by viewModel.description.collectAsState()
    val country by viewModel.country.collectAsState()
    val imageUrl by viewModel.imageUrl.collectAsState()
    val category by viewModel.category.collectAsState()
    val isEnabled by viewModel.isEnabled.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val context = LocalContext.current

    TravelScaffold(
        topBar = {
            TravelToolBar(
                showBack = true,
                titleRes = R.string.create_destination,
                navController = navController
            )
        }
    ) { innerPadding ->
        CreateDestinationContent(
            name = name,
            country = country,
            category = category,
            description = description,
            imageUrl = imageUrl,
            isEnabled = isEnabled,
            isSaving = isSaving,
            onNameChanged = viewModel::onNameChanged,
            onCountryChanged = viewModel::onCountryChanged,
            onCategoryChanged = viewModel::onCategoryChanged,
            onDescriptionChanged = viewModel::onDescriptionChanged,
            onImageUrlChanged = viewModel::onImageUrlChanged,
            onCreate = {
                viewModel.create(
                    onSuccess = { navController.popBackStack() },
                    onError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
                )
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun CreateDestinationContent(
    name: String,
    country: String,
    category: String,
    description: String,
    imageUrl: String,
    isEnabled: Boolean,
    isSaving: Boolean,
    onNameChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onImageUrlChanged: (String) -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("beach", "mountain", "city", "nature")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.spacingMd)
    ) {
        TravelEditorialBlock(
            kicker = stringResource(R.string.create_destination_kicker),
            title = stringResource(R.string.create_destination_title),
            accent = stringResource(R.string.create_destination_title_accent),
            sub = stringResource(R.string.create_destination_subtitle)
        )
        TravelVerticalSpacer(Dimens.spacingLg)
        TravelInlineBanner(
            titleRes = R.string.create_destination_required_fields,
            tone = TravelBannerTone.Info
        )
        TravelVerticalSpacer(Dimens.spacingLg)

        TravelTextField(
            value = name,
            onValueChange = onNameChanged,
            labelRes = R.string.destination_name,
            leadingIconRes = R.drawable.common_ic_castle
        )
        TravelVerticalSpacer(Dimens.spacingSm)
        TravelTextField(
            value = country,
            onValueChange = onCountryChanged,
            labelRes = R.string.destination_country
        )
        TravelVerticalSpacer(Dimens.spacingSm)
        TravelDropdown(
            value = category,
            labelRes = R.string.destination_category,
            options = categories,
            onSelected = onCategoryChanged
        )
        TravelVerticalSpacer(Dimens.spacingSm)
        TravelTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            labelRes = R.string.destination_description
        )
        TravelVerticalSpacer(Dimens.spacingSm)
        TravelTextField(
            value = imageUrl,
            onValueChange = onImageUrlChanged,
            labelRes = R.string.destination_image_url
        )
        TravelVerticalSpacer(Dimens.spacingLg)

        if (isSaving) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.spacingMd),
                contentAlignment = Alignment.Center
            ) {
                TravelLoader(color = MaterialTheme.colorScheme.secondary)
            }
        }

        TravelPrimaryButton(
            textRes = R.string.create_destination,
            enabled = isEnabled,
            onClick = onCreate
        )
    }
}

@Preview(showBackground = true, name = "Create destination")
@Composable
private fun CreateDestinationContentPreview() {
    TravelingAppTheme {
        CreateDestinationContent(
            name = "Atlas en otoño",
            country = "Morocco",
            category = "mountain",
            description = "Montañas, riads y rutas lentas.",
            imageUrl = "local:destination_alps",
            isEnabled = true,
            isSaving = false,
            onNameChanged = {},
            onCountryChanged = {},
            onCategoryChanged = {},
            onDescriptionChanged = {},
            onImageUrlChanged = {},
            onCreate = {}
        )
    }
}

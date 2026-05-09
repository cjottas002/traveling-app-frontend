package org.example.travelingapp.ui.views.home.destinations

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.components.TravelDialog
import org.example.travelingapp.ui.views.components.TravelDropdown
import org.example.travelingapp.ui.views.components.TravelIconButton
import org.example.travelingapp.ui.views.components.TravelLoader
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.TravelScaffold
import org.example.travelingapp.ui.views.components.TravelSecondaryButton
import org.example.travelingapp.ui.views.components.TravelText
import org.example.travelingapp.ui.views.components.TravelTextField
import org.example.travelingapp.ui.views.components.TravelToolBar
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.DestinationDetailViewModel

@Composable
fun DestinationDetailView(
    destinationId: String,
    navController: NavController,
    isAdmin: Boolean = false,
    viewModel: DestinationDetailViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val description by viewModel.description.collectAsState()
    val country by viewModel.country.collectAsState()
    val imageUrl by viewModel.imageUrl.collectAsState()
    val category by viewModel.category.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var loadRequested by remember(destinationId) { mutableStateOf(false) }

    val categories = listOf("beach", "mountain", "city", "nature")
    val hasDestination = name.isNotBlank() || description.isNotBlank() || country.isNotBlank()

    LaunchedEffect(destinationId) {
        loadRequested = true
        viewModel.loadIfNeeded(destinationId)
    }

    TravelScaffold(
        topBar = {
            TravelToolBar(
                showBack = true,
                titleRes = if (isEditing) R.string.edit_destination else R.string.destination_detail,
                navController = navController
            ) {
                if (isAdmin && !isEditing) {
                    TravelIconButton(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        onClick = { showDeleteDialog = true }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.spacingMd)
        ) {
            when {
                (!loadRequested || isLoading) && !hasDestination -> DestinationLoadingState()
                !hasDestination -> DestinationEmptyState()
                isEditing && isAdmin -> DestinationEditContent(
                    name = name,
                    country = country,
                    category = category,
                    description = description,
                    imageUrl = imageUrl,
                    categories = categories,
                    onNameChanged = viewModel::onNameChanged,
                    onCountryChanged = viewModel::onCountryChanged,
                    onCategoryChanged = viewModel::onCategoryChanged,
                    onDescriptionChanged = viewModel::onDescriptionChanged,
                    onImageUrlChanged = viewModel::onImageUrlChanged,
                    onCancel = viewModel::cancelEdit,
                    onSave = {
                        viewModel.save(
                            onSuccess = { navController.popBackStack() },
                            onError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
                        )
                    }
                )
                else -> DestinationReadContent(
                    name = name,
                    country = country,
                    category = category,
                    description = description,
                    imageUrl = imageUrl,
                    isAdmin = isAdmin,
                    onEdit = viewModel::startEdit
                )
            }
        }
    }

    if (showDeleteDialog) {
        TravelDialog(
            onDismissRequest = { showDeleteDialog = false },
            titleRes = R.string.delete_destination,
            textRes = R.string.delete_destination_confirm,
            confirmTextRes = R.string.delete_destination,
            onConfirm = {
                showDeleteDialog = false
                viewModel.delete(
                    onSuccess = { navController.popBackStack() },
                    onError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
                )
            },
            dismissTextRes = R.string.cancel,
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Preview(showBackground = true, name = "Destination detail - read")
@Composable
private fun DestinationReadContentPreview() {
    TravelingAppTheme {
        Column(modifier = Modifier.padding(Dimens.screenPadding)) {
            DestinationReadContent(
                name = "Atlas en otoño",
                country = "Morocco",
                category = "mountain",
                description = "Montañas, riads y rutas lentas para viajar sin prisa.",
                imageUrl = "local:destination_alps",
                isAdmin = true,
                onEdit = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Destination detail - edit")
@Composable
private fun DestinationEditContentPreview() {
    TravelingAppTheme {
        Column(modifier = Modifier.padding(Dimens.screenPadding)) {
            DestinationEditContent(
                name = "Cancun coast",
                country = "Mexico",
                category = "beach",
                description = "A bright coastline for slow days.",
                imageUrl = "local:destination_cancun",
                categories = listOf("beach", "mountain", "city", "nature"),
                onNameChanged = {},
                onCountryChanged = {},
                onCategoryChanged = {},
                onDescriptionChanged = {},
                onImageUrlChanged = {},
                onCancel = {},
                onSave = {}
            )
        }
    }
}

@Composable
private fun DestinationLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.spacingXl),
        contentAlignment = Alignment.Center
    ) {
        TravelLoader()
    }
}

@Composable
private fun DestinationEmptyState() {
    TravelText(
        text = stringResource(R.string.destination_not_found),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = Dimens.spacingLg)
    )
}

@Composable
private fun DestinationReadContent(
    name: String,
    country: String,
    category: String,
    description: String,
    imageUrl: String,
    isAdmin: Boolean,
    onEdit: () -> Unit
) {
    DestinationHeroImage(imageUrl = imageUrl, contentDescription = name)
    TravelVerticalSpacer(Dimens.spacingLg)

    TravelText(
        text = country.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    TravelVerticalSpacer(Dimens.spacingXs)
    TravelText(
        text = name,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground
    )
    if (category.isNotBlank()) {
        TravelVerticalSpacer(Dimens.spacingXs)
        TravelText(
            text = category.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
    if (description.isNotBlank()) {
        TravelVerticalSpacer(Dimens.spacingLg)
        TravelText(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    if (isAdmin) {
        TravelVerticalSpacer(Dimens.spacingLg)
        TravelPrimaryButton(
            textRes = R.string.edit_destination,
            onClick = onEdit
        )
    }
}

@Composable
private fun DestinationEditContent(
    name: String,
    country: String,
    category: String,
    description: String,
    imageUrl: String,
    categories: List<String>,
    onNameChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onImageUrlChanged: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    DestinationHeroImage(imageUrl = imageUrl, contentDescription = name)
    TravelVerticalSpacer(Dimens.spacingLg)
    TravelTextField(value = name, onValueChange = onNameChanged, labelRes = R.string.destination_name)
    TravelVerticalSpacer(Dimens.spacingSm)
    TravelTextField(value = country, onValueChange = onCountryChanged, labelRes = R.string.destination_country)
    TravelVerticalSpacer(Dimens.spacingSm)
    TravelDropdown(
        value = category,
        labelRes = R.string.destination_category,
        options = categories,
        onSelected = onCategoryChanged
    )
    TravelVerticalSpacer(Dimens.spacingSm)
    TravelTextField(value = description, onValueChange = onDescriptionChanged, labelRes = R.string.destination_description)
    TravelVerticalSpacer(Dimens.spacingSm)
    TravelTextField(value = imageUrl, onValueChange = onImageUrlChanged, labelRes = R.string.destination_image_url)
    TravelVerticalSpacer(Dimens.spacingLg)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
    ) {
        TravelSecondaryButton(
            textRes = R.string.cancel,
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        )
        TravelPrimaryButton(
            textRes = R.string.save,
            onClick = onSave,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DestinationHeroImage(imageUrl: String, contentDescription: String) {
    if (imageUrl.isBlank()) return

    val context = LocalContext.current
    val modifier = Modifier
        .fillMaxWidth()
        .height(Dimens.cardImageHeight)
        .clip(RoundedCornerShape(Dimens.radiusSm))
        .background(MaterialTheme.colorScheme.surfaceVariant)

    if (imageUrl.startsWith("local:")) {
        val resName = imageUrl.removePrefix("local:")
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

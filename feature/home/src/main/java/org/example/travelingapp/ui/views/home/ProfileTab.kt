package org.example.travelingapp.ui.views.home

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Alpha
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.LocalTravelColors
import org.example.travelingapp.ui.theme.TravelMonoFamily
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.components.TravelEditorialBlock
import org.example.travelingapp.ui.views.components.TravelHairlineRow
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer
import org.example.travelingapp.ui.views.home.viewmodels.ProfileViewModel

@Composable
fun ProfileTab(profileViewModel: ProfileViewModel = hiltViewModel()) {
    val username by profileViewModel.username.collectAsState()
    val role by profileViewModel.role.collectAsState()
    val pendingOperations by profileViewModel.pendingOperations.collectAsState()
    val isOnline by profileViewModel.isOnline.collectAsState()

    ProfileTabContent(
        username = username,
        role = role,
        pendingOperations = pendingOperations,
        isOnline = isOnline
    )
}

@Composable
private fun ProfileTabContent(
    username: String?,
    role: String?,
    pendingOperations: Int,
    isOnline: Boolean
) {
    val displayName = username.toDisplayName(stringResource(R.string.profile_guest_name))
    val roleLabel = role?.takeIf { it.isNotBlank() } ?: stringResource(R.string.profile_role_fallback)
    val syncState = resolveSyncState(
        pendingOperations = pendingOperations,
        isOnline = isOnline
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.screenPadding)
    ) {
        item {
            TravelVerticalSpacer(Dimens.spacingMd)
            TravelEditorialBlock(
                kicker = stringResource(R.string.profile_kicker),
                title = stringResource(R.string.profile_title),
                accent = stringResource(R.string.profile_title_accent),
                sub = stringResource(R.string.profile_subtitle)
            )
            TravelVerticalSpacer(Dimens.spacingLg)
            ProfileHeaderRow(
                displayName = displayName,
                role = roleLabel
            )
            TravelVerticalSpacer(Dimens.sectionSpacing)
        }

        item {
            ProfileSectionLabel(label = stringResource(R.string.profile_account_section))
            TravelVerticalSpacer(Dimens.spacingSm)
            ProfileActionRow(
                icon = Icons.Filled.Person,
                titleRes = R.string.profile_account_title,
                meta = username?.takeIf { it.isNotBlank() } ?: stringResource(R.string.profile_guest_meta)
            )
            ProfileActionRow(
                icon = Icons.Filled.CheckCircle,
                titleRes = R.string.profile_status_title,
                meta = stringResource(R.string.profile_status_meta)
            )
            TravelVerticalSpacer(Dimens.sectionSpacing)
        }

        item {
            ProfileSectionLabel(label = stringResource(R.string.profile_sync_section))
            TravelVerticalSpacer(Dimens.spacingSm)
            SyncStatusCard(
                syncState = syncState,
                pendingOperations = pendingOperations
            )
            TravelVerticalSpacer(Dimens.sectionSpacing)
        }

        item {
            ProfileSectionLabel(label = stringResource(R.string.profile_reservations_section))
            TravelVerticalSpacer(Dimens.spacingSm)
            ReservationsEmptyState()
            TravelVerticalSpacer(Dimens.sectionSpacing)
        }

        item {
            ProfileSectionLabel(label = stringResource(R.string.profile_preferences_section))
            TravelVerticalSpacer(Dimens.spacingSm)
            ProfileActionRow(
                icon = Icons.Filled.Notifications,
                titleRes = R.string.profile_notifications_title,
                meta = stringResource(R.string.profile_notifications_meta)
            )
            ProfileActionRow(
                icon = Icons.Filled.CreditCard,
                titleRes = R.string.profile_payments_title,
                meta = stringResource(R.string.profile_payments_meta)
            )
        }

        item { TravelVerticalSpacer(Dimens.screenBottomPadding) }
    }
}

@Composable
private fun SyncStatusCard(
    syncState: ProfileSyncState,
    pendingOperations: Int
) {
    val travelColors = LocalTravelColors.current
    val accent = when (syncState) {
        ProfileSyncState.Synced -> travelColors.synced
        ProfileSyncState.Pending -> travelColors.pending
        ProfileSyncState.Offline -> travelColors.offline
    }
    val titleRes = when (syncState) {
        ProfileSyncState.Synced -> R.string.profile_sync_synced_title
        ProfileSyncState.Pending -> R.string.profile_sync_pending_title
        ProfileSyncState.Offline -> R.string.profile_sync_offline_title
    }
    val body = when (syncState) {
        ProfileSyncState.Synced -> stringResource(R.string.profile_sync_synced_body)
        ProfileSyncState.Pending -> stringResource(R.string.profile_sync_pending_body)
        ProfileSyncState.Offline -> stringResource(R.string.profile_sync_offline_body)
    }
    val labelRes = when (syncState) {
        ProfileSyncState.Synced -> R.string.profile_sync_synced_label
        ProfileSyncState.Pending -> R.string.profile_sync_pending_label
        ProfileSyncState.Offline -> R.string.profile_sync_offline_label
    }
    val iconTint = when (syncState) {
        ProfileSyncState.Pending -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.onPrimary
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.radiusXs),
        color = accent.copy(alpha = 0.12f),
        border = BorderStroke(
            width = 1.dp,
            color = accent.copy(alpha = Alpha.subtle)
        )
    ) {
        Row(
            modifier = Modifier.padding(Dimens.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMd)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(Dimens.radiusXs))
                    .background(accent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (syncState == ProfileSyncState.Synced) {
                        Icons.Filled.CheckCircle
                    } else {
                        Icons.Filled.Sync
                    },
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(Dimens.iconMd)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.spacingXxs)
            ) {
                Text(
                    text = stringResource(labelRes).uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = TravelMonoFamily),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = pendingOperations.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(fontFamily = TravelMonoFamily),
                    color = accent
                )
                Text(
                    text = stringResource(R.string.profile_sync_queue_label).uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = TravelMonoFamily),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProfileHeaderRow(
    displayName: String,
    role: String
) {
    TravelHairlineRow(
        leading = {
            ProfileAvatar(displayName = displayName)
        },
        trailing = {
            Surface(
                shape = RoundedCornerShape(Dimens.radiusXs),
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Text(
                    text = role.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = TravelMonoFamily),
                    modifier = Modifier.padding(
                        horizontal = Dimens.spacingSm,
                        vertical = Dimens.spacingXs
                    )
                )
            }
        }
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.profile_account_meta).uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = TravelMonoFamily),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProfileAvatar(displayName: String) {
    val initial = displayName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Box(
        modifier = Modifier
            .size(Dimens.avatarMd)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun ProfileActionRow(
    icon: ImageVector,
    @StringRes titleRes: Int,
    meta: String
) {
    TravelHairlineRow(
        leading = {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(Dimens.radiusXs))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimens.iconMd)
                )
            }
        }
    ) {
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = meta.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = TravelMonoFamily),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ReservationsEmptyState() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.radiusXs),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = Alpha.subtle)
        )
    ) {
        Row(
            modifier = Modifier.padding(Dimens.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMd)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(Dimens.radiusXs))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimens.iconMd)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.profile_reservations_empty_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.profile_reservations_empty_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = Dimens.spacingXxs)
                )
            }
            Text(
                text = "0",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = TravelMonoFamily),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun ProfileSectionLabel(label: String) {
    Text(
        text = "-- ${label.uppercase()}",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

private fun String?.toDisplayName(fallback: String): String {
    val value = this?.takeIf { it.isNotBlank() } ?: return fallback
    return value.substringBefore('@')
        .substringBefore('.')
        .replaceFirstChar { it.uppercase() }
}

private enum class ProfileSyncState {
    Synced,
    Pending,
    Offline
}

private fun resolveSyncState(
    pendingOperations: Int,
    isOnline: Boolean
): ProfileSyncState = when {
    !isOnline -> ProfileSyncState.Offline
    pendingOperations > 0 -> ProfileSyncState.Pending
    else -> ProfileSyncState.Synced
}

@Preview(showBackground = true, name = "Profile")
@Composable
private fun ProfileTabContentPreview() {
    TravelingAppTheme {
        ProfileTabContent(
            username = "admin",
            role = "Admin",
            pendingOperations = 0,
            isOnline = true
        )
    }
}

@Preview(showBackground = true, name = "Profile - pending sync")
@Composable
private fun ProfileTabPendingContentPreview() {
    TravelingAppTheme {
        ProfileTabContent(
            username = "admin",
            role = "Admin",
            pendingOperations = 3,
            isOnline = true
        )
    }
}

@Preview(showBackground = true, name = "Profile - offline sync")
@Composable
private fun ProfileTabOfflineContentPreview() {
    TravelingAppTheme {
        ProfileTabContent(
            username = "admin",
            role = "Admin",
            pendingOperations = 2,
            isOnline = false
        )
    }
}

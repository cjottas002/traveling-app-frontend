package org.example.travelingapp.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Design tokens for spacing, sizing, elevation, and corner radius.
 *
 * All values use density-independent pixels (dp) so they scale correctly
 * across devices of different densities and sizes (Galaxy S, Pixel, etc.).
 *
 * Usage: `Dimens.spacingMd` instead of raw `16.dp` literals.
 */
object Dimens {

    // ── Spacing ────────────────────────────────────────────
    val spacingXxs: Dp = 2.dp
    val spacingXs: Dp = 4.dp
    val spacingSm: Dp = 8.dp
    val spacingMd: Dp = 16.dp
    val spacingLg: Dp = 24.dp
    val spacingXl: Dp = 32.dp
    val spacingXxl: Dp = 48.dp

    // ── Icon sizes ─────────────────────────────────────────
    val iconSm: Dp = 16.dp
    val iconMd: Dp = 24.dp
    val iconLg: Dp = 32.dp
    val iconXl: Dp = 48.dp

    // ── Component heights (touch targets ≥ 48 dp per Material) ─
    val buttonHeight: Dp = 48.dp
    val textFieldHeight: Dp = 56.dp
    val toolbarHeight: Dp = 64.dp
    val bottomNavHeight: Dp = 80.dp

    // ── Card / image ───────────────────────────────────────
    val cardImageHeight: Dp = 180.dp
    val avatarSm: Dp = 40.dp
    val avatarMd: Dp = 64.dp
    val avatarLg: Dp = 96.dp

    // ── Corner radius ──────────────────────────────────────
    val radiusSm: Dp = 8.dp
    val radiusMd: Dp = 12.dp
    val radiusLg: Dp = 16.dp
    val radiusXl: Dp = 24.dp
    val radiusFull: Dp = 100.dp

    // ── Elevation ──────────────────────────────────────────
    val elevationNone: Dp = 0.dp
    val elevationSm: Dp = 1.dp
    val elevationMd: Dp = 3.dp
    val elevationLg: Dp = 6.dp
}

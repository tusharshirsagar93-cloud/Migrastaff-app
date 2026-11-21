package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val AuroraPrismColorScheme = darkColorScheme(
    primary = GlacierCyan,
    onPrimary = DeepNavyInk,
    primaryContainer = GlacierCyanSoft,
    onPrimaryContainer = GlacierCyan,
    secondary = AuroraMint,
    onSecondary = DeepNavyInk,
    secondaryContainer = AuroraMintSoft,
    onSecondaryContainer = AuroraMint,
    tertiary = RoyalAmethyst,
    onTertiary = DeepNavyInk,
    tertiaryContainer = RoyalAmethystSoft,
    onTertiaryContainer = RoyalAmethyst,
    background = AuroraVoid,
    onBackground = IceWhite,
    surface = AuroraSurface,
    onSurface = IceWhite,
    surfaceVariant = AuroraSurfaceElevated,
    onSurfaceVariant = ArcticMist,
    surfaceContainer = AuroraSurface,
    surfaceContainerHigh = AuroraSurfaceElevated,
    surfaceContainerHighest = AuroraSurfaceBright,
    outline = AuroraPrismBorder,
    outlineVariant = AuroraPrismHairline,
    error = CoralRose,
    onError = IceWhite,
    errorContainer = CoralRoseSoft,
    onErrorContainer = CoralRose
)

private val MigraShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun MigraStaffTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AuroraPrismColorScheme,
        typography = MigraTypography,
        shapes = MigraShapes,
        content = content
    )
}

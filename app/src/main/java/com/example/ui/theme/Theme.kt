package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

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
    val isNight = MigraThemeState.isNight
    val scheme = if (isNight) {
        darkColorScheme(
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
    } else {
        lightColorScheme(
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
            onError = DeepNavyInk,
            errorContainer = CoralRoseSoft,
            onErrorContainer = CoralRose
        )
    }
    MaterialTheme(
        colorScheme = scheme,
        typography = MigraTypography,
        shapes = MigraShapes,
        content = content
    )
}

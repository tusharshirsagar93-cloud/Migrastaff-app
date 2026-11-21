package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object MigraThemeState {
    var isNight by mutableStateOf(false)
}

val AuroraVoid get() = if (MigraThemeState.isNight) Color(0xFF050811) else Color(0xFFF1F5F9)
val AuroraDeepCanvas get() = if (MigraThemeState.isNight) Color(0xFF090F1F) else Color(0xFFE8EEF5)
val AuroraSurface get() = if (MigraThemeState.isNight) Color(0xFF0E172A) else Color(0xFFFFFFFF)
val AuroraSurfaceElevated get() = if (MigraThemeState.isNight) Color(0xFF142038) else Color(0xFFF8FAFC)
val AuroraSurfaceBright get() = if (MigraThemeState.isNight) Color(0xFF1B2B4B) else Color(0xFFEFF6FF)
val AuroraPrismBorder get() = if (MigraThemeState.isNight) Color(0xFF283E6B) else Color(0xFFCBD5E1)
val AuroraPrismHairline get() = if (MigraThemeState.isNight) Color(0xFF1A2A4A) else Color(0xFFE2E8F0)

val GlacierCyan get() = if (MigraThemeState.isNight) Color(0xFF38BDF8) else Color(0xFF1D4ED8)
val GlacierCyanSoft get() = if (MigraThemeState.isNight) Color(0xFF0E2542) else Color(0xFFEFF6FF)
val HyperCobalt get() = if (MigraThemeState.isNight) Color(0xFF60A5FA) else Color(0xFF0284C7)
val AuroraCobaltDeep get() = if (MigraThemeState.isNight) Color(0xFF1E3A8A) else Color(0xFF0B192C)

val AuroraMint get() = if (MigraThemeState.isNight) Color(0xFF10B981) else Color(0xFF059669)
val AuroraMintSoft get() = if (MigraThemeState.isNight) Color(0xFF082D24) else Color(0xFFECFDF5)
val AuroraMintBorder get() = if (MigraThemeState.isNight) Color(0xFF0F6B50) else Color(0xFFA7F3D0)

val RoyalAmethyst get() = if (MigraThemeState.isNight) Color(0xFFA78BFA) else Color(0xFF4F46E5)
val RoyalAmethystSoft get() = if (MigraThemeState.isNight) Color(0xFF231845) else Color(0xFFEEF2FF)

val SolarGold get() = if (MigraThemeState.isNight) Color(0xFFFBBF24) else Color(0xFFD97706)
val SolarGoldSoft get() = if (MigraThemeState.isNight) Color(0xFF2D2207) else Color(0xFFFEF3C7)
val SolarGoldText get() = if (MigraThemeState.isNight) Color(0xFFFDE68A) else Color(0xFF92400E)

val CoralRose get() = if (MigraThemeState.isNight) Color(0xFFFB7185) else Color(0xFFDC2626)
val CoralRoseSoft get() = if (MigraThemeState.isNight) Color(0xFF36101E) else Color(0xFFFEE2E2)

val IceWhite get() = if (MigraThemeState.isNight) Color(0xFFF4F8FF) else Color(0xFF0B192C)
val ArcticMist get() = if (MigraThemeState.isNight) Color(0xFFA5BBE0) else Color(0xFF334155)
val SlateMuted get() = if (MigraThemeState.isNight) Color(0xFF7089B5) else Color(0xFF64748B)
val DeepNavyInk get() = Color(0xFFFFFFFF)

val VaultSurfaceTop get() = if (MigraThemeState.isNight) Color(0xFF0B1833) else Color(0xFFEFF6FF)
val VaultSurfaceBottom get() = if (MigraThemeState.isNight) Color(0xFF08262B) else Color(0xFFECFDF5)
val QrModuleNavy get() = Color(0xFF0B192C)

val AuroraActionGradient get() = if (MigraThemeState.isNight) {
    Brush.horizontalGradient(colors = listOf(Color(0xFF2563EB), Color(0xFF0EA5E9)))
} else {
    Brush.horizontalGradient(colors = listOf(Color(0xFF1D4ED8), Color(0xFF0284C7)))
}

val AuroraMintGradient get() = Brush.horizontalGradient(
    colors = listOf(Color(0xFF059669), Color(0xFF10B981))
)

val AuroraHeroGradient get() = if (MigraThemeState.isNight) {
    Brush.linearGradient(colors = listOf(Color(0xFF0B1530), Color(0xFF18224A), Color(0xFF07282A)))
} else {
    Brush.linearGradient(colors = listOf(Color(0xFFEFF6FF), Color(0xFFE0F2FE), Color(0xFFECFDF5)))
}

val AuroraPrismBorderBrush get() = if (MigraThemeState.isNight) {
    Brush.horizontalGradient(colors = listOf(Color(0xFF38BDF8), Color(0xFF818CF8), Color(0xFFFBBF24)))
} else {
    Brush.horizontalGradient(colors = listOf(Color(0xFF1D4ED8), Color(0xFF0284C7), Color(0xFF059669)))
}

val AuroraCanvasGradient get() = Brush.verticalGradient(
    colors = listOf(AuroraVoid, AuroraDeepCanvas, AuroraDeepCanvas)
)

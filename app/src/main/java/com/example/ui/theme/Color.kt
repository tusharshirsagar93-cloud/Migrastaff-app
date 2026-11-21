package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// MigraStaff "Obsidian Emerald & Liquid Gold" — Flagship Futuristic Palette
val AuroraVoid = Color(0xFF03050A)
val AuroraDeepCanvas = Color(0xFF060B16)
val AuroraSurface = Color(0xFF0B1324)
val AuroraSurfaceElevated = Color(0xFF111D35)
val AuroraSurfaceBright = Color(0xFF18294A)
val AuroraPrismBorder = Color(0xFF243B6B)
val AuroraPrismHairline = Color(0xFF172747)

val GlacierCyan = Color(0xFF00F0FF)
val GlacierCyanSoft = Color(0xFF082738)
val HyperCobalt = Color(0xFF3B82F6)
val AuroraCobaltDeep = Color(0xFF1E3A8A)

val AuroraMint = Color(0xFF00FFA3)
val AuroraMintSoft = Color(0xFF062921)
val AuroraMintBorder = Color(0xFF0E6B53)

val RoyalAmethyst = Color(0xFFD8B4FE)
val RoyalAmethystSoft = Color(0xFF241347)

val SolarGold = Color(0xFFFFD166)
val SolarGoldSoft = Color(0xFF2D2207)
val SolarGoldText = Color(0xFFFFE399)

val CoralRose = Color(0xFFFF4D79)
val CoralRoseSoft = Color(0xFF360E1D)

val IceWhite = Color(0xFFF4F8FF)
val ArcticMist = Color(0xFFA5BBE0)
val SlateMuted = Color(0xFF7089B5)
val DeepNavyInk = Color(0xFF030814)

// Cryptographic Vault Surface
val VaultSurfaceTop = Color(0xFF081633)
val VaultSurfaceBottom = Color(0xFF05262B)
val QrModuleNavy = Color(0xFF061538)

// Signature Flagship Gradients
val AuroraActionGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00FFA3), Color(0xFF00C2FF))
)

val AuroraMintGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00FFA3), Color(0xFF10B981))
)

val AuroraHeroGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF0B1530),
        Color(0xFF1B1142),
        Color(0xFF052E2B)
    )
)

val AuroraPrismBorderBrush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF00FFA3),
        Color(0xFF00F0FF),
        Color(0xFFFFD166)
    )
)

val AuroraCanvasGradient = Brush.verticalGradient(
    colors = listOf(
        AuroraVoid,
        AuroraDeepCanvas,
        Color(0xFF051324)
    )
)

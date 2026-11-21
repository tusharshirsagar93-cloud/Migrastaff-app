package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// MigraStaff "Pearl Platinum & Midnight Ink" — Swiss Private Bank Edition
val AuroraVoid = Color(0xFFF1F5F9)            // Pearl Platinum Canvas
val AuroraDeepCanvas = Color(0xFFE8EEF5)      // Soft Platinum Mist
val AuroraSurface = Color(0xFFFFFFFF)         // Pure Crisp White Card
val AuroraSurfaceElevated = Color(0xFFF8FAFC) // Elevated Ivory-Slate
val AuroraSurfaceBright = Color(0xFFEFF6FF)   // Subtle Sapphire Tint
val AuroraPrismBorder = Color(0xFFCBD5E1)     // Crisp Platinum Border
val AuroraPrismHairline = Color(0xFFE2E8F0)   // Hairline Divider

val GlacierCyan = Color(0xFF1D4ED8)           // Royal Cobalt Accent
val GlacierCyanSoft = Color(0xFFEFF6FF)       // Soft Cobalt Pill
val HyperCobalt = Color(0xFF0284C7)           // Swiss Sapphire
val AuroraCobaltDeep = Color(0xFF0B192C)      // Midnight Ink

val AuroraMint = Color(0xFF059669)            // Swiss Emerald
val AuroraMintSoft = Color(0xFFECFDF5)        // Soft Emerald Pill
val AuroraMintBorder = Color(0xFFA7F3D0)      // Emerald Border

val RoyalAmethyst = Color(0xFF4F46E5)         // Indigo Accent
val RoyalAmethystSoft = Color(0xFFEEF2FF)     // Soft Indigo Pill

val SolarGold = Color(0xFFD97706)             // Warm Amber Gold
val SolarGoldSoft = Color(0xFFFEF3C7)         // Soft Gold Pill
val SolarGoldText = Color(0xFF92400E)         // Deep Gold Text

val CoralRose = Color(0xFFDC2626)             // Crisp Alert Red
val CoralRoseSoft = Color(0xFFFEE2E2)         // Soft Red Pill

val IceWhite = Color(0xFF0B192C)              // Midnight Ink (Primary Text)
val ArcticMist = Color(0xFF334155)            // Slate 700 (Secondary Text)
val SlateMuted = Color(0xFF64748B)            // Slate 500 (Muted Labels)
val DeepNavyInk = Color(0xFFFFFFFF)           // Pure White (Button Ink on Dark/Vibrant)

// Cryptographic Vault Surface
val VaultSurfaceTop = Color(0xFFEFF6FF)
val VaultSurfaceBottom = Color(0xFFECFDF5)
val QrModuleNavy = Color(0xFF0B192C)

// Signature Flagship Gradients
val AuroraActionGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF1D4ED8), Color(0xFF0284C7))
)

val AuroraMintGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF059669), Color(0xFF10B981))
)

val AuroraHeroGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFEFF6FF),
        Color(0xFFE0F2FE),
        Color(0xFFECFDF5)
    )
)

val AuroraPrismBorderBrush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF1D4ED8),
        Color(0xFF0284C7),
        Color(0xFF059669)
    )
)

val AuroraCanvasGradient = Brush.verticalGradient(
    colors = listOf(
        AuroraVoid,
        AuroraDeepCanvas,
        Color(0xFFE2E8F0)
    )
)

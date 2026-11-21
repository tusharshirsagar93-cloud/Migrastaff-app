package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MigraCatalogData
import com.example.data.MigraStrings
import com.example.data.fx.FxRepository
import com.example.model.AppLang
import com.example.ui.theme.ArcticMist
import com.example.ui.theme.AuroraHeroGradient
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.AuroraPrismBorder
import com.example.ui.theme.AuroraSurface
import com.example.ui.theme.AuroraSurfaceElevated
import com.example.ui.theme.GlacierCyan
import com.example.ui.theme.GlacierCyanSoft
import com.example.ui.theme.HyperCobalt
import com.example.ui.theme.IceWhite
import com.example.ui.theme.MonoFontFamily
import com.example.ui.viewmodel.FxUiState
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FxScreen(
    lang: AppLang,
    fxState: FxUiState,
    onFromChange: (String) -> Unit,
    onToChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onSwap: () -> Unit,
    onRefresh: () -> Unit,
    onApplyPreset: (from: String, to: String, amount: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val copy = MigraStrings.of(lang)
    val rateDigits = when {
        fxState.crossRate >= 100.0 -> 2
        fxState.crossRate >= 1.0 -> 4
        else -> 6
    }
    val formattedRate = String.format(Locale.US, "%.${rateDigits}f", fxState.crossRate)
    val convertedText = FxRepository.formatFxMoney(fxState.convertedAmount, fxState.toCode)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = copy.fxTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    color = IceWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = copy.fxSub,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArcticMist
                )
            }
        }

        // Main Converter Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.dp, AuroraPrismBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (fxState.isLive) AuroraMintSoft else GlacierCyanSoft,
                            border = BorderStroke(
                                1.dp,
                                (if (fxState.isLive) AuroraMint else GlacierCyan).copy(alpha = 0.38f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (fxState.isLive) AuroraMint else GlacierCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = buildString {
                                        append(if (fxState.isLive) copy.fxLive else copy.fxCached)
                                        if (fxState.updatedAtLabel.isNotEmpty()) {
                                            append(" · ${copy.fxUpdated} ${fxState.updatedAtLabel}")
                                        }
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (fxState.isLive) AuroraMint else GlacierCyan
                                )
                            }
                        }

                        IconButton(
                            onClick = onRefresh,
                            enabled = !fxState.isSyncing,
                            modifier = Modifier.testTag("fx_refresh_button")
                        ) {
                            if (fxState.isSyncing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = GlacierCyan
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = stringResource(R.string.cd_refresh_fx),
                                    tint = GlacierCyan
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CurrencyDropdownField(
                            label = copy.fxFrom,
                            selectedCode = fxState.fromCode,
                            lang = lang,
                            onSelect = onFromChange,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("fx_swap_button")
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(onClick = onSwap),
                            shape = RoundedCornerShape(12.dp),
                            color = GlacierCyanSoft,
                            border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.45f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.SwapHoriz,
                                    contentDescription = stringResource(R.string.cd_swap_currency),
                                    tint = GlacierCyan
                                )
                            }
                        }
                        CurrencyDropdownField(
                            label = copy.fxTo,
                            selectedCode = fxState.toCode,
                            lang = lang,
                            onSelect = onToChange,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = copy.fxAmount,
                        style = MaterialTheme.typography.labelSmall,
                        color = ArcticMist
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fxState.amountInput,
                        onValueChange = onAmountChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("fx_amount_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = IceWhite
                        ),
                        suffix = { Text(fxState.fromCode, fontWeight = FontWeight.Bold, color = GlacierCyan) },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = AuroraSurfaceElevated,
                            unfocusedContainerColor = AuroraSurfaceElevated,
                            focusedBorderColor = GlacierCyan,
                            unfocusedBorderColor = AuroraPrismBorder,
                            focusedTextColor = IceWhite,
                            unfocusedTextColor = IceWhite,
                            cursorColor = GlacierCyan
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Aurora Gradient Result Vault
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(AuroraHeroGradient)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = copy.fxConverted,
                                style = MaterialTheme.typography.labelSmall,
                                color = ArcticMist
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$convertedText ${fxState.toCode}",
                                style = MaterialTheme.typography.displaySmall.copy(
                                    fontFamily = MonoFontFamily,
                                    fontSize = 26.sp
                                ),
                                color = GlacierCyan,
                                modifier = Modifier.testTag("fx_result_value")
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "1 ${fxState.fromCode} = $formattedRate ${fxState.toCode}",
                                style = MaterialTheme.typography.labelMedium,
                                color = AuroraMint
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = copy.fxPopular,
                        style = MaterialTheme.typography.labelMedium,
                        color = ArcticMist
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MigraCatalogData.fxPresets.forEach { preset ->
                            val selected = fxState.fromCode == preset.from &&
                                fxState.toCode == preset.to &&
                                fxState.parsedAmount == preset.amount
                            FilterChip(
                                selected = selected,
                                onClick = { onApplyPreset(preset.from, preset.to, preset.amount) },
                                label = { Text(preset.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = HyperCobalt,
                                    selectedLabelColor = IceWhite,
                                    containerColor = AuroraSurfaceElevated,
                                    labelColor = ArcticMist
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selected,
                                    borderColor = AuroraPrismBorder,
                                    selectedBorderColor = GlacierCyan
                                )
                            )
                        }
                    }
                }
            }
        }

        // Benchmark Grid for Source Countries
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.dp, AuroraPrismBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == AppLang.RU) "Справочник курсов (за 1 000 RUB)" else "Quick Benchmark (per 1,000 RUB)",
                        style = MaterialTheme.typography.titleMedium,
                        color = IceWhite
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MigraCatalogData.fxCurrencies.filter { it.code != "RUB" }.forEach { curr ->
                        val v = (fxState.ratesPerRub[curr.code] ?: curr.fallbackPerRub) * 1000.0
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToChange(curr.code) }
                                .padding(vertical = 7.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(curr.flag, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(curr.code, style = MaterialTheme.typography.titleSmall, color = IceWhite)
                                    Text(
                                        text = if (lang == AppLang.RU) curr.nameRu else curr.nameEn,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ArcticMist
                                    )
                                }
                            }
                            Text(
                                text = FxRepository.formatFxMoney(v, curr.code),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontFamily = MonoFontFamily,
                                    color = AuroraMint
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyDropdownField(
    label: String,
    selectedCode: String,
    lang: AppLang,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCurrency = MigraCatalogData.fxCurrencies.find { it.code == selectedCode }

    Box(modifier = modifier) {
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = ArcticMist)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { expanded = true },
                shape = RoundedCornerShape(12.dp),
                color = AuroraSurfaceElevated,
                border = BorderStroke(1.dp, AuroraPrismBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${selectedCurrency?.flag.orEmpty()} $selectedCode",
                        style = MaterialTheme.typography.titleMedium,
                        color = IceWhite
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        tint = GlacierCyan
                    )
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = AuroraSurfaceElevated
        ) {
            MigraCatalogData.fxCurrencies.forEach { curr ->
                DropdownMenuItem(
                    text = {
                        Text(
                            "${curr.flag} ${curr.code} — ${if (lang == AppLang.RU) curr.nameRu else curr.nameEn}",
                            color = IceWhite
                        )
                    },
                    onClick = {
                        onSelect(curr.code)
                        expanded = false
                    }
                )
            }
        }
    }
}

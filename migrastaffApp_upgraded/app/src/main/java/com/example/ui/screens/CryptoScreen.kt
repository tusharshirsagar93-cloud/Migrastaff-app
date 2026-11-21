package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MigraCatalogData
import com.example.data.MigraStrings
import com.example.model.AppLang
import com.example.model.CryptoPaymentReceipt
import com.example.ui.components.AuroraGradientButton
import com.example.ui.components.QrMatrixCanvas
import com.example.ui.theme.ArcticMist
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.AuroraMintBorder
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.AuroraPrismBorder
import com.example.ui.theme.AuroraPrismBorderBrush
import com.example.ui.theme.AuroraSurface
import com.example.ui.theme.AuroraSurfaceElevated
import com.example.ui.theme.DeepNavyInk
import com.example.ui.theme.GlacierCyan
import com.example.ui.theme.GlacierCyanSoft
import com.example.ui.theme.HyperCobalt
import com.example.ui.theme.IceWhite
import com.example.ui.theme.MonoFontFamily
import com.example.ui.theme.RoyalAmethyst
import com.example.ui.theme.RoyalAmethystSoft
import com.example.ui.theme.SolarGold
import com.example.ui.theme.SolarGoldSoft
import com.example.ui.theme.SolarGoldText
import com.example.ui.theme.VaultSurfaceBottom
import com.example.ui.theme.VaultSurfaceTop
import com.example.ui.viewmodel.CryptoUiState
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CryptoScreen(
    lang: AppLang,
    cryptoState: CryptoUiState,
    rubPerUsd: Double,
    isSignedIn: Boolean,
    onSelectPlan: (String) -> Unit,
    onSelectCoin: (String) -> Unit,
    onCustomAmountChange: (String) -> Unit,
    onToggleCustomCurrency: () -> Unit,
    onContactChange: (String) -> Unit,
    onTxHashChange: (String) -> Unit,
    onCopyToast: (String) -> Unit,
    onSubmitReceipt: (CryptoPaymentReceipt) -> Unit,
    modifier: Modifier = Modifier
) {
    val copy = MigraStrings.of(lang)
    val context = LocalContext.current
    val selectedPlan = MigraCatalogData.cryptoPlans.find { it.key == cryptoState.selectedPlanKey }
        ?: MigraCatalogData.cryptoPlans.first()
    val selectedCoin = MigraCatalogData.cryptoCoins.find { it.key == cryptoState.selectedCoinKey }
        ?: MigraCatalogData.cryptoCoins.first()

    val (rubVal, usdVal) = if (selectedPlan.key == "custom") {
        val raw = cryptoState.customAmountInput.toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        if (cryptoState.customCurrency == "USD") {
            raw * rubPerUsd to raw
        } else {
            raw to (raw / rubPerUsd)
        }
    } else {
        selectedPlan.rubAmount to (selectedPlan.rubAmount / rubPerUsd)
    }

    val cryptoRaw = usdVal / selectedCoin.usdRate
    val cryptoFixed = String.format(Locale.US, "%.${selectedCoin.decimals}f", cryptoRaw)
    val cryptoFormatted = "$cryptoFixed ${selectedCoin.symbol}"

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = AuroraSurfaceElevated,
        unfocusedContainerColor = AuroraSurfaceElevated,
        focusedBorderColor = GlacierCyan,
        unfocusedBorderColor = AuroraPrismBorder,
        focusedTextColor = IceWhite,
        unfocusedTextColor = IceWhite,
        focusedLabelColor = GlacierCyan,
        unfocusedLabelColor = ArcticMist,
        cursorColor = GlacierCyan
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = RoyalAmethystSoft,
                    border = BorderStroke(1.dp, RoyalAmethyst.copy(alpha = 0.45f))
                ) {
                    Text(
                        text = copy.cryptoEyebrow,
                        style = MaterialTheme.typography.labelMedium,
                        color = RoyalAmethyst,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = copy.cryptoTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    color = IceWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = copy.cryptoSub,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArcticMist
                )
            }
        }

        // Step 1: Select Plan
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.dp, AuroraPrismBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(copy.cryptoStep1, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                    Spacer(modifier = Modifier.height(10.dp))
                    MigraCatalogData.cryptoPlans.forEach { plan ->
                        val selected = plan.key == selectedPlan.key
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("crypto_plan_${plan.key}")
                                .clickable { onSelectPlan(plan.key) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (selected) GlacierCyanSoft else AuroraSurfaceElevated,
                            border = BorderStroke(1.5.dp, if (selected) GlacierCyan else AuroraPrismBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (lang == AppLang.RU) plan.nameRu else plan.nameEn,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = IceWhite
                                    )
                                    Text(
                                        text = if (lang == AppLang.RU) plan.descRu else plan.descEn,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ArcticMist
                                    )
                                }
                                Text(
                                    text = if (plan.key == "custom") {
                                        if (lang == AppLang.RU) "Своя" else "Custom"
                                    } else {
                                        "₽${plan.rubAmount.roundToInt()}"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) GlacierCyan else AuroraMint
                                    )
                                )
                            }
                        }
                    }

                    if (selectedPlan.key == "custom") {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = cryptoState.customAmountInput,
                                onValueChange = onCustomAmountChange,
                                label = { Text(copy.cryptoCustomAmt) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                colors = fieldColors,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedButton(
                                onClick = onToggleCustomCurrency,
                                modifier = Modifier.height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.5f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GlacierCyan)
                            ) {
                                Text(cryptoState.customCurrency, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Step 2: Select Coin
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.dp, AuroraPrismBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(copy.cryptoStep2, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MigraCatalogData.cryptoCoins.forEach { coin ->
                            val selected = coin.key == selectedCoin.key
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectCoin(coin.key) },
                                label = { Text("${coin.symbol} · ${coin.network}") },
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
                                ),
                                modifier = Modifier.testTag("crypto_coin_${coin.key}")
                            )
                        }
                    }
                }
            }
        }

        // Terminal Checkout Card (Iridescent Aurora Titanium Vault + Canvas QR)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.2.dp, AuroraPrismBorderBrush)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(VaultSurfaceTop, VaultSurfaceBottom)
                                )
                            )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${selectedCoin.network} · ETA ${selectedCoin.eta}",
                                style = MaterialTheme.typography.labelMedium,
                                color = AuroraMint
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = copy.cryptoDue,
                                style = MaterialTheme.typography.labelSmall,
                                color = ArcticMist
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cryptoFormatted,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontFamily = MonoFontFamily,
                                        color = GlacierCyan
                                    ),
                                    modifier = Modifier.testTag("crypto_due_amount")
                                )
                                OutlinedButton(
                                    onClick = {
                                        copyToClipboard(context, cryptoFixed)
                                        onCopyToast("${copy.cryptoCopyAmt}: $cryptoFixed")
                                    },
                                    border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.55f)),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                                ) {
                                    Text(copy.cryptoCopyAmt, color = IceWhite, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "≈ ₽${rubVal.roundToInt()} (~$${String.format(Locale.US, "%.2f", usdVal)})",
                                style = MaterialTheme.typography.bodySmall,
                                color = ArcticMist
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // QR + Wallet Address
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = AuroraSurfaceElevated,
                        border = BorderStroke(1.dp, AuroraPrismBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            QrMatrixCanvas(
                                seedText = "${selectedCoin.address}:$cryptoFixed:${selectedCoin.network}"
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = copy.cryptoAddrLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ArcticMist
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = selectedCoin.address,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = MonoFontFamily,
                                        color = IceWhite
                                    )
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        copyToClipboard(context, selectedCoin.address)
                                        onCopyToast(copy.cryptoCopyAddr)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GlacierCyan,
                                        contentColor = DeepNavyInk
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                    modifier = Modifier.testTag("crypto_copy_wallet_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentCopy,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(copy.cryptoCopyAddr, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Warning Banner
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SolarGoldSoft,
                        border = BorderStroke(1.dp, SolarGold.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Filled.WarningAmber,
                                contentDescription = null,
                                tint = SolarGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${copy.cryptoWarnPrefix} ${selectedCoin.symbol} (${selectedCoin.network}).",
                                style = MaterialTheme.typography.bodySmall,
                                color = SolarGoldText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(copy.cryptoStep3, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cryptoState.payerContact,
                        onValueChange = onContactChange,
                        label = { Text(copy.cryptoContactLabel) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = cryptoState.txHash,
                        onValueChange = onTxHashChange,
                        label = { Text(copy.cryptoTxLabel) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("crypto_txid_input"),
                        colors = fieldColors
                    )

                    if (!isSignedIn) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = copy.cryptoSignInHint,
                            style = MaterialTheme.typography.bodySmall,
                            color = ArcticMist
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    AuroraGradientButton(
                        text = copy.cryptoSubmitBtn,
                        enabled = !cryptoState.isSubmitting,
                        onClick = {
                            val cleanTx = cryptoState.txHash.trim()
                            if (cleanTx.length >= 8) {
                                val ref = "MS-CR-${Random.nextInt(100000, 999999)}"
                                onSubmitReceipt(
                                    CryptoPaymentReceipt(
                                        refId = ref,
                                        planKey = selectedPlan.key,
                                        coinKey = selectedCoin.key,
                                        amountCrypto = cryptoFormatted,
                                        amountRub = rubVal,
                                        txHash = cleanTx,
                                        payerContact = cryptoState.payerContact.trim()
                                    )
                                )
                            } else {
                                onCopyToast(
                                    if (lang == AppLang.RU) "Введите корректный TXID (мин. 8 символов)"
                                    else "Enter a valid TXID (min 8 chars)"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("crypto_submit_button")
                    )

                    if (cryptoState.lastReceiptRef != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = AuroraMintSoft,
                            border = BorderStroke(1.dp, AuroraMintBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = AuroraMint
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = copy.cryptoReceiptTitle,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = AuroraMint
                                    )
                                    Text(
                                        text = "Ref: ${cryptoState.lastReceiptRef} · $cryptoFormatted",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = IceWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun copyToClipboard(context: Context, value: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
    cm.setPrimaryClip(ClipData.newPlainText("MigraStaff", value))
}

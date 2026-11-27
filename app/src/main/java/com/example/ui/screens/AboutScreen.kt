package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MigraCatalogData
import com.example.data.MigraStrings
import com.example.model.AppLang
import com.example.model.InfoTopic
import com.example.model.MainTab
import com.example.ui.components.StatusTagChip
import com.example.ui.theme.ArcticMist
import com.example.ui.theme.AuroraHeroGradient
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.AuroraPrismBorder
import com.example.ui.theme.AuroraPrismBorderBrush
import com.example.ui.theme.AuroraSurface
import com.example.ui.theme.AuroraSurfaceElevated
import com.example.ui.theme.DeepNavyInk
import com.example.ui.theme.GlacierCyan
import com.example.ui.theme.GlacierCyanSoft
import com.example.ui.theme.IceWhite
import com.example.ui.theme.RoyalAmethyst
import com.example.ui.theme.RoyalAmethystSoft
import com.example.ui.theme.SolarGoldSoft
import com.example.ui.theme.SolarGoldText

@Composable
fun AboutScreen(
    lang: AppLang,
    onOpenTopic: (InfoTopic) -> Unit,
    onSelectCryptoPlan: (String) -> Unit,
    onJumpTab: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val copy = MigraStrings.of(lang)
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Aurora Iridescent Hero Banner
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.2.dp, AuroraPrismBorderBrush)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AuroraHeroGradient)
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(74.dp)
                                .clip(CircleShape)
                                .border(2.dp, AuroraPrismBorderBrush, CircleShape)
                                .padding(3.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.migrastaff_logo),
                                contentDescription = "MigraStaff Official Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = copy.aboutHeroTitle,
                                style = MaterialTheme.typography.headlineMedium,
                                color = IceWhite
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = copy.aboutHeroSub,
                        style = MaterialTheme.typography.bodyMedium,
                        color = IceWhite.copy(alpha = 0.88f)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatusTagChip("12,400+ Workers", GlacierCyanSoft, GlacierCyan)
                        StatusTagChip("40+ Countries", AuroraMintSoft, AuroraMint)
                    }
                }
            }
        }

        // Outstaffing & 2027 Law Section
        item {
            var salaryRub by remember { mutableIntStateOf(50000) }
            val taxRub = (salaryRub * 0.43).toInt()
            val commRub = (salaryRub * 0.12).toInt()
            val adminRub = 2000
            val totalRub = salaryRub + taxRub + commRub + adminRub
            val isRu = lang == AppLang.RU

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.2.dp, AuroraMint.copy(alpha = 0.55f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        shape = RoundedCornerShape(99.dp),
                        color = AuroraMint.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = if (isRu) "🏢 АУТСТАФФИНГ · ООО «МИГРАСТАФФ»" else "🏢 OUTSTAFFING · ООО «МИГРАСТАФФ»",
                            style = MaterialTheme.typography.labelSmall,
                            color = AuroraMint,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isRu) "Аутстаффинг и защита от штрафов" else "Outstaffing & Legal Protection",
                        style = MaterialTheme.typography.titleLarge,
                        color = IceWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isRu) {
                            "Мы становимся официальным работодателем в РФ: договоры, уведомления в МВД и отчётность без рисков. Для граждан ЕАЭС (KG, KZ, AM), СНГ (UZ, TJ, AZ) и визовых стран (IN, BD, NP)."
                        } else {
                            "We become the legal employer in Russia — handling contracts, MVD notices & tax reporting. Covers EAEU (KG, KZ, AM), CIS Patent (UZ, TJ, AZ) & Visa countries (IN, BD, NP)."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = ArcticMist
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // 2026-2027 New Law Alert (FZ-241)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF59E0B).copy(alpha = 0.14f),
                        border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.45f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isRu) "⚡ Новый закон 2026–2027 о доходах (241-ФЗ)" else "⚡ 2026–2027 New Income Law (FZ-241)",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFFBBF24),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isRu) {
                                    "• 1 окт 2026: ФНС передаёт данные о доходах в МВД каждый квартал.\n• 1 янв / 1 мар 2027: Белая зарплата ≥ прожиточного минимума обязательна (иначе аннулирование).\n• Решение: 100% официальный аутстаффинг без штрафов."
                                } else {
                                    "• 1 Oct 2026: Tax Office (FNS) sends worker income data to MVD quarterly.\n• 1 Jan / 1 Mar 2027: Official white salary ≥ regional minimum mandatory.\n• Solution: 100% official Outstaffing — zero fines."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = IceWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Monthly Outstaffing Cost Calculator
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF0B1324).copy(alpha = 0.75f),
                        border = BorderStroke(1.dp, AuroraPrismBorder)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isRu) "🧮 Калькулятор аутстаффинга в месяц" else "🧮 Monthly Outstaffing Calculator",
                                style = MaterialTheme.typography.labelMedium,
                                color = GlacierCyan,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { if (salaryRub > 20000) salaryRub -= 5000 },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, AuroraPrismBorder),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                                ) { Text("−5k", fontSize = 11.5.sp) }
                                Text(
                                    text = "${salaryRub} ₽",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                OutlinedButton(
                                    onClick = { salaryRub += 5000 },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, AuroraPrismBorder),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                                ) { Text("+5k", fontSize = 11.5.sp) }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(if (isRu) "Налоги (43%):" else "Taxes (43%):", style = MaterialTheme.typography.bodySmall, color = ArcticMist)
                                Text("$taxRub ₽", style = MaterialTheme.typography.bodySmall, color = IceWhite, fontWeight = FontWeight.SemiBold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(if (isRu) "Комиссия (12%):" else "Commission (12%):", style = MaterialTheme.typography.bodySmall, color = ArcticMist)
                                Text("$commRub ₽", style = MaterialTheme.typography.bodySmall, color = IceWhite, fontWeight = FontWeight.SemiBold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(if (isRu) "МВД (1500) + Бух (500):" else "MVD (1500) + Acct (500):", style = MaterialTheme.typography.bodySmall, color = ArcticMist)
                                Text("$adminRub ₽", style = MaterialTheme.typography.bodySmall, color = IceWhite, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AuroraPrismBorder))
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(if (isRu) "Итого в месяц:" else "Total Monthly:", style = MaterialTheme.typography.titleSmall, color = AuroraMint, fontWeight = FontWeight.Bold)
                                Text("$totalRub ₽", style = MaterialTheme.typography.titleSmall, color = AuroraMint, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://migra.pro/outstaffing"))
                                runCatching { context.startActivity(intent) }
                            },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                        ) { Text(if (isRu) "📄 Аутстаффинг" else "📄 Outstaffing", fontSize = 12.sp, maxLines = 1) }
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.workmigrapro.com/guides"))
                                runCatching { context.startActivity(intent) }
                            },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFBBF24))
                        ) { Text(if (isRu) "📖 Закон 2027" else "📖 2027 Law Guide", fontSize = 12.sp, maxLines = 1) }
                    }
                }
            }
        }

        // Transparent Pricing Plans
        item {
            Text(copy.aboutPricingTitle, style = MaterialTheme.typography.titleLarge, color = IceWhite)
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PricingTierCard(
                    title = if (lang == AppLang.RU) "Рабочий — Базовый" else "Worker — Free",
                    price = "₽0",
                    desc = if (lang == AppLang.RU) {
                        "Полный профиль, до 5 фото работ, прямые отклики работодателей."
                    } else {
                        "Full candidate resume, up to 5 work photos, employer messages."
                    },
                    ctaLabel = if (lang == AppLang.RU) "Создать профиль" else "Register Free",
                    highlighted = false,
                    onClick = { onJumpTab(MainTab.PROFILE) }
                )
                PricingTierCard(
                    title = if (lang == AppLang.RU) "Рабочий — Про" else "Worker — Pro",
                    price = "₽499 / mo",
                    desc = if (lang == AppLang.RU) {
                        "Приоритет в топе поиска, значок Проверен Про, ускоренная верификация."
                    } else {
                        "Top search placement, Verified Pro badge, fast-track document screening."
                    },
                    ctaLabel = if (lang == AppLang.RU) "Оплатить крипто" else "Pay via Crypto",
                    highlighted = true,
                    onClick = {
                        onSelectCryptoPlan("pro")
                        onJumpTab(MainTab.CRYPTO)
                    }
                )
                PricingTierCard(
                    title = if (lang == AppLang.RU) "Работодатель — Бизнес" else "Employer — Business",
                    price = "₽2 490 / mo",
                    desc = if (lang == AppLang.RU) {
                        "Безлимитное открытие контактов, до 10 вакансий, 5 рекрутеров."
                    } else {
                        "Unlimited worker contact unlocks, 10 active jobs, 5 recruiter seats."
                    },
                    ctaLabel = if (lang == AppLang.RU) "Выбрать Бизнес" else "Pay Business via Crypto",
                    highlighted = false,
                    onClick = {
                        onSelectCryptoPlan("business")
                        onJumpTab(MainTab.CRYPTO)
                    }
                )
            }
        }

        // Moscow Headquarters & Contacts
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.dp, AuroraPrismBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Shield, contentDescription = null, tint = GlacierCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(copy.aboutOfficeTitle, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = AuroraMint,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == AppLang.RU) {
                                "ООО «МИГРАСТАФФ» · ИНН: 1800020150 · КПП: 772345001\nМосква, ул. Перерва, д. 16, пом. 16П"
                            } else {
                                "ООО \"МИГРАСТАФФ\" · INN: 1800020150 · KPP: 772345001\nMoscow, Pererva Street, House 16, Room 16P"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArcticMist
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/79128500333"))
                                runCatching { context.startActivity(intent) }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("about_whatsapp_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AuroraMint,
                                contentColor = DeepNavyInk
                            )
                        ) {
                            Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(copy.aboutWhatsApp, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:migrapro@imap.mail.ru"))
                                runCatching { context.startActivity(intent) }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                        ) {
                            Icon(Icons.Filled.Email, contentDescription = null, modifier = Modifier.size(16.dp), tint = GlacierCyan)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(copy.aboutEmail, fontSize = 12.5.sp)
                        }
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+79128500333"))
                                runCatching { context.startActivity(intent) }
                            },
                            modifier = Modifier.height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, AuroraPrismBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GlacierCyan)
                        ) {
                            Icon(Icons.Filled.Call, contentDescription = copy.aboutCall, modifier = Modifier.size(18.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.workmigrapro.com"))
                                runCatching { context.startActivity(intent) }
                            },
                            modifier = Modifier.weight(1f).height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                        ) {
                            Text("🌐 workmigrapro.com", fontSize = 12.sp, maxLines = 1)
                        }
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.workmigrapro.com/business-setup"))
                                runCatching { context.startActivity(intent) }
                            },
                            modifier = Modifier.weight(1f).height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, AuroraMint.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                        ) {
                            Text("💼 Business Setup", fontSize = 12.sp, maxLines = 1)
                        }
                    }
                }
            }
        }

        // Support Guides & Legal Sheets
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.dp, AuroraPrismBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(copy.aboutSupportTitle, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                    Spacer(modifier = Modifier.height(8.dp))
                    MigraCatalogData.infoTopics.forEach { topic ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenTopic(topic) }
                                .padding(vertical = 11.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (lang == AppLang.RU) topic.titleRu else topic.titleEn,
                                style = MaterialTheme.typography.bodyLarge,
                                color = GlacierCyan
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = ArcticMist
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PricingTierCard(
    title: String,
    price: String,
    desc: String,
    ctaLabel: String,
    highlighted: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (highlighted) AuroraSurfaceElevated else AuroraSurface
        ),
        border = BorderStroke(
            if (highlighted) 1.5.dp else 1.dp,
            if (highlighted) AuroraPrismBorderBrush else androidx.compose.ui.graphics.SolidColor(AuroraPrismBorder)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (highlighted) SolarGoldSoft else AuroraMintSoft,
                    border = BorderStroke(
                        1.dp,
                        if (highlighted) SolarGoldText.copy(alpha = 0.45f) else AuroraMint.copy(alpha = 0.45f)
                    )
                ) {
                    Text(
                        text = price,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (highlighted) SolarGoldText else AuroraMint,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(desc, style = MaterialTheme.typography.bodySmall, color = ArcticMist)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (highlighted) GlacierCyan else AuroraPrismBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (highlighted) GlacierCyan else IceWhite
                )
            ) {
                Text(ctaLabel, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

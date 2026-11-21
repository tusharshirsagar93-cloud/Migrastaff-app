package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.MigraCatalogData
import com.example.data.MigraStrings
import com.example.model.AppLang
import com.example.model.InfoTopic
import com.example.model.JobVacancy
import com.example.model.SortOrder
import com.example.ui.theme.ArcticMist
import com.example.ui.theme.AuroraActionGradient
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.AuroraMintBorder
import com.example.ui.theme.AuroraMintGradient
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.AuroraPrismBorder
import com.example.ui.theme.AuroraPrismBorderBrush
import com.example.ui.theme.AuroraPrismHairline
import com.example.ui.theme.AuroraSurface
import com.example.ui.theme.AuroraSurfaceElevated
import com.example.ui.theme.CoralRose
import com.example.ui.theme.DeepNavyInk
import com.example.ui.theme.GlacierCyan
import com.example.ui.theme.GlacierCyanSoft
import com.example.ui.theme.HyperCobalt
import com.example.ui.theme.IceWhite
import com.example.ui.theme.QrModuleNavy
import com.example.ui.theme.RoyalAmethyst
import com.example.ui.theme.RoyalAmethystSoft
import com.example.ui.theme.SolarGold
import com.example.ui.viewmodel.BrowseFilterState
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AuroraGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    useMintGradient: Boolean = false
) {
    val brush = when {
        !enabled -> Brush.horizontalGradient(listOf(AuroraSurfaceElevated, AuroraSurfaceElevated))
        useMintGradient -> AuroraMintGradient
        else -> AuroraActionGradient
    }
    val textColor = when {
        !enabled -> ArcticMist
        useMintGradient -> DeepNavyInk
        else -> IceWhite
    }
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = textColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(brush = brush, shape = RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 14.dp)
            )
        }
    }
}

@Composable
fun MigraBrandHeader(
    lang: AppLang,
    onToggleLang: () -> Unit,
    modifier: Modifier = Modifier
) {
    val copy = MigraStrings.of(lang)
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, AuroraPrismBorderBrush, CircleShape)
                        .padding(2.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.migrastaff_logo),
                        contentDescription = "MigraStaff Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "MIGRASTAFF",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp,
                            brush = Brush.horizontalGradient(listOf(IceWhite, GlacierCyan))
                        )
                    )
                    Text(
                        text = copy.brandTag,
                        style = MaterialTheme.typography.labelSmall,
                        color = AuroraMint
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .testTag("lang_toggle_button")
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(onClick = onToggleLang),
                shape = RoundedCornerShape(20.dp),
                color = GlacierCyanSoft,
                border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.45f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Public,
                        contentDescription = null,
                        tint = GlacierCyan,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = if (lang == AppLang.EN) "EN · RU" else "RU · EN",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = IceWhite
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            GlacierCyan.copy(alpha = 0.45f),
                            AuroraMint.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VacancyCard(
    vacancy: JobVacancy,
    lang: AppLang,
    isFavorite: Boolean,
    isApplied: Boolean,
    onToggleFavorite: () -> Unit,
    onOpenDetail: () -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val copy = MigraStrings.of(lang)
    val salaryRange = remember(vacancy.salaryMinRub, vacancy.salaryMaxRub) {
        val nf = NumberFormat.getNumberInstance(Locale.US)
        val minStr = nf.format(vacancy.salaryMinRub).replace(',', ' ')
        val maxStr = nf.format(vacancy.salaryMaxRub).replace(',', ' ')
        "$minStr – $maxStr"
    }
    val jobTitle = if (lang == AppLang.RU) vacancy.titleRu else vacancy.titleEn
    val employerTitle = if (lang == AppLang.RU) vacancy.employerRu else vacancy.employerEn
    val cityDisplay = if (lang == AppLang.RU) vacancy.cityRu else vacancy.cityEn
    val expDisplay = if (lang == AppLang.RU) vacancy.experienceRu else vacancy.experienceEn
    val payDisplay = if (lang == AppLang.RU) vacancy.payScheduleRu else vacancy.payScheduleEn
    val responseDisplay = if (lang == AppLang.RU) vacancy.responseSpeedRu else vacancy.responseSpeedEn
    val perkDisplay = if (lang == AppLang.RU) vacancy.perkRu else vacancy.perkEn
    val postedDisplay = if (lang == AppLang.RU) vacancy.postedRu else vacancy.postedEn
    val viewingText = if (lang == AppLang.RU) {
        "${vacancy.activeViewers} человек сейчас смотрят"
    } else {
        "${vacancy.activeViewers} people now viewing"
    }
    val reviewText = if (lang == AppLang.RU) {
        "${vacancy.rating} · ${vacancy.reviewCount} отзывов"
    } else {
        "${vacancy.rating} · ${vacancy.reviewCount} reviews"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("vacancy_card_${vacancy.id}")
            .clickable(onClick = onOpenDetail),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AuroraSurface),
        border = BorderStroke(
            1.dp,
            if (isApplied) AuroraMintBorder else AuroraPrismBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(AuroraMint)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = viewingText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = AuroraMint
                    )
                }
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("favorite_vacancy_${vacancy.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = stringResource(R.string.cd_favorites),
                        tint = if (isFavorite) CoralRose else ArcticMist
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = jobTitle,
                style = MaterialTheme.typography.titleLarge,
                color = IceWhite
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$salaryRange ${copy.perMonthSuffix}",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = GlacierCyan
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = employerTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = IceWhite,
                    modifier = Modifier.weight(1f, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (vacancy.verifiedEmployer) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = stringResource(R.string.cd_verified),
                        tint = GlacierCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = SolarGold,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$reviewText  ·  $cityDisplay",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArcticMist,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusTagChip(
                    label = expDisplay,
                    bg = AuroraSurfaceElevated,
                    fg = IceWhite
                )
                StatusTagChip(
                    label = payDisplay,
                    bg = AuroraSurfaceElevated,
                    fg = ArcticMist
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusTagChip(
                    label = responseDisplay,
                    bg = AuroraMintSoft,
                    fg = AuroraMint
                )
                StatusTagChip(
                    label = perkDisplay,
                    bg = RoyalAmethystSoft,
                    fg = RoyalAmethyst
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = postedDisplay,
                style = MaterialTheme.typography.labelSmall,
                color = ArcticMist.copy(alpha = 0.78f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuroraGradientButton(
                text = if (isApplied) copy.contactUnlockedCta else copy.viewResumeCta,
                onClick = if (isApplied) onOpenDetail else onApplyClick,
                useMintGradient = isApplied,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vacancy_cta_${vacancy.id}")
            )
        }
    }
}

@Composable
fun StatusTagChip(label: String, bg: Color, fg: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg,
        border = BorderStroke(0.5.dp, fg.copy(alpha = 0.22f))
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = fg,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VacancyDetailSheet(
    vacancy: JobVacancy,
    lang: AppLang,
    isSignedIn: Boolean,
    isApplied: Boolean,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    onGoToProfile: () -> Unit
) {
    val copy = MigraStrings.of(lang)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val nf = NumberFormat.getNumberInstance(Locale.US)
    val salaryRange = "${nf.format(vacancy.salaryMinRub).replace(',', ' ')} – ${
        nf.format(vacancy.salaryMaxRub).replace(',', ' ')
    }"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AuroraSurface,
        contentColor = IceWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (lang == AppLang.RU) vacancy.titleRu else vacancy.titleEn,
                        style = MaterialTheme.typography.headlineMedium,
                        color = IceWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${if (lang == AppLang.RU) vacancy.employerRu else vacancy.employerEn} · ${
                            if (lang == AppLang.RU) vacancy.cityRu else vacancy.cityEn
                        }",
                        style = MaterialTheme.typography.titleMedium,
                        color = ArcticMist
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = ArcticMist)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = GlacierCyanSoft,
                border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (lang == AppLang.RU) "Предлагаемая зарплата" else "Offered Salary",
                            style = MaterialTheme.typography.labelMedium,
                            color = ArcticMist
                        )
                        Text(
                            text = "$salaryRange ₽ / ${if (lang == AppLang.RU) "мес" else "mo"}",
                            style = MaterialTheme.typography.titleLarge,
                            color = GlacierCyan
                        )
                    }
                    StatusTagChip(
                        label = if (lang == AppLang.RU) vacancy.experienceRu else vacancy.experienceEn,
                        bg = AuroraSurface,
                        fg = IceWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = if (lang == AppLang.RU) "Условия и обязанности" else "Job Description & Conditions",
                style = MaterialTheme.typography.titleSmall,
                color = IceWhite
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (lang == AppLang.RU) vacancy.descRu else vacancy.descEn,
                style = MaterialTheme.typography.bodyLarge,
                color = ArcticMist
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = if (lang == AppLang.RU) "Оформление визы / патента для стран" else "Visa & Work Permit Sponsorship",
                style = MaterialTheme.typography.titleSmall,
                color = IceWhite
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                vacancy.visaCountries.forEach { code ->
                    val c = MigraCatalogData.countryOf(code)
                    StatusTagChip(
                        label = "${c?.flag.orEmpty()} ${MigraCatalogData.countryName(code, lang)}",
                        bg = AuroraSurfaceElevated,
                        fg = IceWhite
                    )
                }
                StatusTagChip(
                    label = if (lang == AppLang.RU) vacancy.perkRu else vacancy.perkEn,
                    bg = AuroraMintSoft,
                    fg = AuroraMint
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = AuroraPrismHairline)
            Spacer(modifier = Modifier.height(16.dp))

            if (isApplied) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = AuroraMintSoft,
                    border = BorderStroke(1.dp, AuroraMintBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = AuroraMint)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (lang == AppLang.RU) "Отклик отправлен · Контакты HR" else "Applied · Direct HR Contacts",
                                style = MaterialTheme.typography.titleSmall,
                                color = AuroraMint
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Phone, contentDescription = null, tint = GlacierCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(vacancy.hrPhone, style = MaterialTheme.typography.bodyMedium, color = IceWhite)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Email, contentDescription = null, tint = GlacierCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(vacancy.hrEmail, style = MaterialTheme.typography.bodyMedium, color = IceWhite)
                        }
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = AuroraSurfaceElevated,
                    border = BorderStroke(1.dp, AuroraPrismBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Lock, contentDescription = null, tint = GlacierCyan)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = copy.lockedStatus,
                                style = MaterialTheme.typography.titleSmall,
                                color = IceWhite
                            )
                            Text(
                                text = if (lang == AppLang.RU) {
                                    "Нажмите «Откликнуться», чтобы отправить заявку и открыть телефон и почту HR-отдела."
                                } else {
                                    "Tap Apply to submit your interest and unlock direct HR phone and email."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = ArcticMist
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            onGoToProfile()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AuroraPrismBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                    ) {
                        Text(
                            text = if (isSignedIn) {
                                if (lang == AppLang.RU) "Чат с директором" else "Ask Director"
                            } else {
                                if (lang == AppLang.RU) "Моё резюме" else "My Resume"
                            }
                        )
                    }
                    AuroraGradientButton(
                        text = copy.viewResumeCta,
                        onClick = onApply,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterModalSheet(
    lang: AppLang,
    current: BrowseFilterState,
    onDismiss: () -> Unit,
    onReset: () -> Unit,
    onApply: (country: String, profession: String, verifiedOnly: Boolean, sort: SortOrder) -> Unit
) {
    val copy = MigraStrings.of(lang)
    var country by remember(current) { mutableStateOf(current.selectedCountry) }
    var profession by remember(current) { mutableStateOf(current.selectedProfession) }
    var verifiedOnly by remember(current) { mutableStateOf(current.verifiedOnly) }
    var sortOrder by remember(current) { mutableStateOf(current.sortOrder) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val chipColors = FilterChipDefaults.filterChipColors(
        selectedContainerColor = HyperCobalt,
        selectedLabelColor = IceWhite,
        containerColor = AuroraSurfaceElevated,
        labelColor = ArcticMist
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AuroraSurface,
        contentColor = IceWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(copy.filtersTitle, style = MaterialTheme.typography.headlineSmall, color = IceWhite)
                TextButton(onClick = {
                    onReset()
                    onDismiss()
                }) {
                    Text(copy.filterReset, color = GlacierCyan)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(copy.filterVerifiedOnly, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                Switch(
                    checked = verifiedOnly,
                    onCheckedChange = { verifiedOnly = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = DeepNavyInk,
                        checkedTrackColor = AuroraMint,
                        uncheckedThumbColor = ArcticMist,
                        uncheckedTrackColor = AuroraSurfaceElevated
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(copy.filterSort, style = MaterialTheme.typography.titleSmall, color = IceWhite)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val sorts = listOf(
                    SortOrder.RELEVANCE to copy.sortRel,
                    SortOrder.SALARY_ASC to copy.sortLow,
                    SortOrder.SALARY_DESC to copy.sortHigh,
                    SortOrder.EXPERIENCE_DESC to copy.sortExp
                )
                sorts.forEach { (ord, label) ->
                    FilterChip(
                        selected = sortOrder == ord,
                        onClick = { sortOrder = ord },
                        label = { Text(label) },
                        colors = chipColors,
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = sortOrder == ord,
                            borderColor = AuroraPrismBorder,
                            selectedBorderColor = GlacierCyan
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(copy.filterCountry, style = MaterialTheme.typography.titleSmall, color = IceWhite)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = country.isEmpty(),
                    onClick = { country = "" },
                    label = { Text(copy.filterAllCountries) },
                    colors = chipColors,
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = country.isEmpty(),
                        borderColor = AuroraPrismBorder,
                        selectedBorderColor = GlacierCyan
                    )
                )
                MigraCatalogData.countries.forEach { c ->
                    val sel = country == c.code
                    FilterChip(
                        selected = sel,
                        onClick = { country = if (sel) "" else c.code },
                        label = {
                            Text("${c.flag} ${if (lang == AppLang.RU) c.nameRu else c.nameEn}")
                        },
                        colors = chipColors,
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = sel,
                            borderColor = AuroraPrismBorder,
                            selectedBorderColor = GlacierCyan
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(copy.filterProf, style = MaterialTheme.typography.titleSmall, color = IceWhite)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = profession.isEmpty(),
                    onClick = { profession = "" },
                    label = { Text(copy.allCategories) },
                    colors = chipColors,
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = profession.isEmpty(),
                        borderColor = AuroraPrismBorder,
                        selectedBorderColor = GlacierCyan
                    )
                )
                MigraCatalogData.categories.forEach { cat ->
                    val sel = profession == cat.profEn
                    FilterChip(
                        selected = sel,
                        onClick = { profession = if (sel) "" else cat.profEn },
                        label = { Text(MigraCatalogData.profName(cat.profEn, lang)) },
                        colors = chipColors,
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = sel,
                            borderColor = AuroraPrismBorder,
                            selectedBorderColor = GlacierCyan
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            AuroraGradientButton(
                text = copy.filterApply,
                onClick = {
                    onApply(country, profession, verifiedOnly, sortOrder)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun InfoTopicDialog(
    topic: InfoTopic,
    lang: AppLang,
    onDismiss: () -> Unit
) {
    val title = if (lang == AppLang.RU) topic.titleRu else topic.titleEn
    val bullets = if (lang == AppLang.RU) topic.bulletsRu else topic.bulletsEn

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AuroraSurface),
            border = BorderStroke(1.dp, AuroraPrismBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = IceWhite,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = ArcticMist)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                bullets.forEach { item ->
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = AuroraMint,
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArcticMist
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                AuroraGradientButton(
                    text = if (lang == AppLang.RU) "Понятно" else "Close",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun QrMatrixCanvas(
    seedText: String,
    modifier: Modifier = Modifier
) {
    val matrix = remember(seedText) { MigraCatalogData.generateQrMatrix(seedText) }
    val cd = stringResource(R.string.cd_qr_code)
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(IceWhite)
            .border(2.dp, GlacierCyan.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
            .padding(8.dp)
            .semantics { contentDescription = cd }
    ) {
        Canvas(modifier = Modifier.size(104.dp)) {
            val n = matrix.size
            val cellW = size.width / n
            val cellH = size.height / n
            for (r in 0 until n) {
                for (c in 0 until n) {
                    if (matrix[r][c]) {
                        drawRect(
                            color = QrModuleNavy,
                            topLeft = Offset(c * cellW, r * cellH),
                            size = Size(cellW + 0.4f, cellH + 0.4f)
                        )
                    }
                }
            }
        }
    }
}

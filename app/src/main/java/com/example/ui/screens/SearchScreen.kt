package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.MigraCatalogData
import com.example.data.MigraStrings
import com.example.model.AppLang
import com.example.model.JobVacancy
import com.example.model.MainTab
import com.example.ui.components.VacancyCard
import com.example.ui.theme.ArcticMist
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.AuroraPrismBorder
import com.example.ui.theme.AuroraSurface
import com.example.ui.theme.AuroraSurfaceElevated
import com.example.ui.theme.CoralRose
import com.example.ui.theme.CoralRoseSoft
import com.example.ui.theme.GlacierCyan
import com.example.ui.theme.GlacierCyanSoft
import com.example.ui.theme.HyperCobalt
import com.example.ui.theme.IceWhite
import com.example.ui.theme.RoyalAmethyst
import com.example.ui.theme.RoyalAmethystSoft
import com.example.ui.theme.SolarGoldSoft
import com.example.ui.theme.SolarGoldText
import com.example.ui.viewmodel.BrowseFilterState

@Composable
fun SearchScreen(
    lang: AppLang,
    filterState: BrowseFilterState,
    vacancies: List<JobVacancy>,
    favoriteIds: Set<Int>,
    appliedIds: Set<Int>,
    onQueryChange: (String) -> Unit,
    onOpenFilters: () -> Unit,
    onToggleSavedOnly: () -> Unit,
    onSelectCategory: (String) -> Unit,
    onResetFilters: () -> Unit,
    onToggleFavorite: (JobVacancy) -> Unit,
    onOpenVacancy: (JobVacancy) -> Unit,
    onApplyVacancy: (JobVacancy) -> Unit,
    onJumpTab: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val copy = MigraStrings.of(lang)
    val activeFilterCount = listOfNotNull(
        filterState.selectedCountry.takeIf { it.isNotEmpty() },
        filterState.selectedProfession.takeIf { it.isNotEmpty() },
        if (filterState.verifiedOnly) "v" else null
    ).size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(top = 6.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Aurora Glass Vacancy Search Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = filterState.query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("search_input"),
                    placeholder = {
                        Text(
                            text = copy.searchPlaceholder,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArcticMist.copy(alpha = 0.78f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.cd_search),
                            tint = GlacierCyan
                        )
                    },
                    trailingIcon = {
                        if (filterState.query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear", tint = ArcticMist)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = AuroraSurface,
                        unfocusedContainerColor = AuroraSurface,
                        focusedBorderColor = GlacierCyan,
                        unfocusedBorderColor = AuroraPrismBorder,
                        focusedTextColor = IceWhite,
                        unfocusedTextColor = IceWhite,
                        cursorColor = GlacierCyan
                    )
                )

                Surface(
                    modifier = Modifier
                        .size(52.dp)
                        .testTag("open_filters_button")
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = onOpenFilters),
                    shape = RoundedCornerShape(14.dp),
                    color = if (activeFilterCount > 0) GlacierCyanSoft else AuroraSurface,
                    border = BorderStroke(
                        1.dp,
                        if (activeFilterCount > 0) GlacierCyan else AuroraPrismBorder
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = stringResource(R.string.cd_filter),
                            tint = if (activeFilterCount > 0) GlacierCyan else IceWhite
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .size(52.dp)
                        .testTag("toggle_saved_only_button")
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = onToggleSavedOnly),
                    shape = RoundedCornerShape(14.dp),
                    color = if (filterState.savedOnly) CoralRoseSoft else AuroraSurface,
                    border = BorderStroke(
                        1.dp,
                        if (filterState.savedOnly) CoralRose else AuroraPrismBorder
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (filterState.savedOnly) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(R.string.cd_favorites),
                            tint = if (filterState.savedOnly) CoralRose else IceWhite
                        )
                    }
                }
            }
        }

        // Quick Action Carousel Cards
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    QuickActionCard(
                        title = copy.quickRegisterTitle,
                        subtitle = copy.quickRegisterSub,
                        icon = Icons.Filled.PersonAdd,
                        badgeBg = GlacierCyanSoft,
                        badgeTint = GlacierCyan,
                        accentBorder = GlacierCyan.copy(alpha = 0.35f),
                        testTag = "quick_action_register",
                        onClick = { onJumpTab(MainTab.PROFILE) }
                    )
                }
                item {
                    QuickActionCard(
                        title = copy.quickFxTitle,
                        subtitle = copy.quickFxSub,
                        icon = Icons.Filled.CurrencyExchange,
                        badgeBg = AuroraMintSoft,
                        badgeTint = AuroraMint,
                        accentBorder = AuroraMint.copy(alpha = 0.35f),
                        testTag = "quick_action_fx",
                        onClick = { onJumpTab(MainTab.FX) }
                    )
                }
                item {
                    QuickActionCard(
                        title = copy.quickCryptoTitle,
                        subtitle = copy.quickCryptoSub,
                        icon = Icons.Filled.QrCode2,
                        badgeBg = SolarGoldSoft,
                        badgeTint = SolarGoldText,
                        accentBorder = SolarGoldText.copy(alpha = 0.35f),
                        testTag = "quick_action_crypto",
                        onClick = { onJumpTab(MainTab.CRYPTO) }
                    )
                }
                item {
                    QuickActionCard(
                        title = copy.quickTeamTitle,
                        subtitle = copy.quickTeamSub,
                        icon = Icons.Filled.Groups,
                        badgeBg = RoyalAmethystSoft,
                        badgeTint = RoyalAmethyst,
                        accentBorder = RoyalAmethyst.copy(alpha = 0.35f),
                        testTag = "quick_action_about",
                        onClick = { onJumpTab(MainTab.ABOUT) }
                    )
                }
            }
        }

        // Job Category Pill Strip
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val allSelected = filterState.selectedProfession.isEmpty()
                    FilterChip(
                        selected = allSelected,
                        onClick = { onSelectCategory("") },
                        label = {
                            Text(
                                text = copy.allCategories,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HyperCobalt,
                            selectedLabelColor = IceWhite,
                            containerColor = AuroraSurface,
                            labelColor = ArcticMist
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = allSelected,
                            borderColor = AuroraPrismBorder,
                            selectedBorderColor = GlacierCyan
                        )
                    )
                }
                items(MigraCatalogData.categories, key = { it.key }) { cat ->
                    val selected = filterState.selectedProfession == cat.profEn
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectCategory(cat.profEn) },
                        label = {
                            Text(
                                text = "${MigraCatalogData.profName(cat.profEn, lang)} · ${cat.count}",
                                fontWeight = FontWeight.Medium
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HyperCobalt,
                            selectedLabelColor = IceWhite,
                            containerColor = AuroraSurface,
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

        // Open Vacancies Count & Filter Reset Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Work,
                        contentDescription = null,
                        tint = GlacierCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${vacancies.size} ${copy.workersFoundSuffix}",
                        style = MaterialTheme.typography.titleSmall,
                        color = IceWhite
                    )
                }
                if (filterState.query.isNotEmpty() || filterState.selectedCountry.isNotEmpty() ||
                    filterState.selectedProfession.isNotEmpty() || filterState.verifiedOnly || filterState.savedOnly
                ) {
                    TextButton(onClick = onResetFilters) {
                        Text(copy.filterReset, color = GlacierCyan)
                    }
                }
            }
        }

        if (vacancies.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.dp, AuroraPrismBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SearchOff,
                            contentDescription = null,
                            tint = GlacierCyan,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = copy.emptySearch,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArcticMist
                        )
                    }
                }
            }
        } else {
            items(vacancies, key = { it.id }) { vacancy ->
                VacancyCard(
                    vacancy = vacancy,
                    lang = lang,
                    isFavorite = favoriteIds.contains(vacancy.id),
                    isApplied = appliedIds.contains(vacancy.id),
                    onToggleFavorite = { onToggleFavorite(vacancy) },
                    onOpenDetail = { onOpenVacancy(vacancy) },
                    onApplyClick = { onApplyVacancy(vacancy) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeBg: Color,
    badgeTint: Color,
    accentBorder: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(156.dp)
            .testTag(testTag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AuroraSurfaceElevated),
        border = BorderStroke(1.dp, accentBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = badgeTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = IceWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = ArcticMist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

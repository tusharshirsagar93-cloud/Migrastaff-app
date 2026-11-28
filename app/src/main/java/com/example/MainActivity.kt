package com.example

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.Surface
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import android.net.Uri
import android.content.Intent
import com.example.model.AppLang
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.MigraStrings
import com.example.model.AuthUiState
import com.example.model.MainTab
import com.example.ui.components.FilterModalSheet
import com.example.ui.components.InfoTopicDialog
import com.example.ui.components.MigraBrandHeader
import com.example.ui.components.VacancyDetailSheet
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.CryptoScreen
import com.example.ui.screens.MyApplicationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.ArcticMist
import com.example.ui.theme.AuroraCanvasGradient
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.AuroraPrismBorder
import com.example.ui.theme.AuroraPrismBorderBrush
import com.example.ui.theme.AuroraSurface
import com.example.ui.theme.AuroraSurfaceElevated
import com.example.ui.theme.AuroraVoid
import com.example.ui.theme.GlacierCyan
import com.example.ui.theme.GlacierCyanSoft
import com.example.ui.theme.HyperCobalt
import com.example.ui.theme.IceWhite
import com.example.ui.theme.MigraStaffTheme
import com.example.ui.theme.RoyalAmethyst
import com.example.ui.viewmodel.MigraMainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            MigraStaffTheme {
                MigraStaffApp(activity = this)
            }
        }
    }
}

private data class NavEntry(
    val tab: MainTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MigraStaffApp(
    activity: Activity? = null,
    vm: MigraMainViewModel = viewModel()
) {
    val context = LocalContext.current
    val lang by vm.language.collectAsStateWithLifecycle()
    val activeTab by vm.activeTab.collectAsStateWithLifecycle()
    val filterState by vm.filterState.collectAsStateWithLifecycle()
    val vacancies by vm.filteredVacancies.collectAsStateWithLifecycle()
    val favoriteIds by vm.localFavorites.collectAsStateWithLifecycle()
    val appliedIds by vm.appliedVacancyIds.collectAsStateWithLifecycle()
    val fxState by vm.fxState.collectAsStateWithLifecycle()
    val myApplications by vm.myApplications.collectAsStateWithLifecycle()
    val employerProfile by vm.employerProfile.collectAsStateWithLifecycle()
    val allApplications by vm.allApplications.collectAsStateWithLifecycle()
    val allEmployers by vm.allEmployers.collectAsStateWithLifecycle()
    val allCandidates by vm.allCandidates.collectAsStateWithLifecycle()
    val liveJobs by vm.liveJobsFlow.collectAsStateWithLifecycle()
    val cryptoState by vm.cryptoState.collectAsStateWithLifecycle()
    val selectedVacancy by vm.selectedVacancy.collectAsStateWithLifecycle()
    val selectedTopic by vm.selectedInfoTopic.collectAsStateWithLifecycle()
    val showFilterSheet by vm.showFilterSheet.collectAsStateWithLifecycle()
    val authState by vm.authState.collectAsStateWithLifecycle()
    val authError by vm.authErrorMessage.collectAsStateWithLifecycle()
    val snackbarMsg by vm.snackbarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val copy = MigraStrings.of(lang)

    LaunchedEffect(activity) {
        if (activity != null) {
            vm.tryAutoSignIn(activity)
        }
    }

    LaunchedEffect(snackbarMsg) {
        val msg = snackbarMsg
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            vm.clearSnackbar()
        }
    }

    BackHandler(
        enabled = selectedVacancy != null || selectedTopic != null || showFilterSheet || activeTab != MainTab.SEARCH
    ) {
        when {
            selectedVacancy != null -> vm.closeVacancyDetail()
            selectedTopic != null -> vm.closeInfoTopic()
            showFilterSheet -> vm.setFilterSheetVisible(false)
            activeTab != MainTab.SEARCH -> vm.selectTab(MainTab.SEARCH)
        }
    }

    val safeWorkLbl = if (lang == AppLang.RU) "Услуги и\nзащита" else "Services &\nSafe Work"
    var showProfileMenu by remember { mutableStateOf(false) }
    val navItems = listOf(
        NavEntry(MainTab.SEARCH, copy.tabSearch, Icons.Filled.Search, Icons.Outlined.Search),
        NavEntry(MainTab.ABOUT, safeWorkLbl, Icons.Filled.Verified, Icons.Outlined.Verified),
        NavEntry(MainTab.FX, copy.tabFx, Icons.Filled.Work, Icons.Outlined.Work),
        NavEntry(MainTab.CRYPTO, copy.tabCrypto, Icons.Filled.QrCode2, Icons.Outlined.QrCode2),
        NavEntry(MainTab.PROFILE, copy.tabProfile, Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AuroraCanvasGradient)
    ) {
        val isWide = maxWidth >= 600.dp

        // Subtle Northern Lights Ambient Glow Mesh behind entire app
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(HyperCobalt.copy(alpha = 0.22f), Color.Transparent),
                    center = Offset(size.width * 0.12f, size.height * 0.08f),
                    radius = size.width * 0.78f
                ),
                center = Offset(size.width * 0.12f, size.height * 0.08f),
                radius = size.width * 0.78f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GlacierCyan.copy(alpha = 0.14f), Color.Transparent),
                    center = Offset(size.width * 0.92f, size.height * 0.26f),
                    radius = size.width * 0.65f
                ),
                center = Offset(size.width * 0.92f, size.height * 0.26f),
                radius = size.width * 0.65f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(RoyalAmethyst.copy(alpha = 0.13f), Color.Transparent),
                    center = Offset(size.width * 0.85f, size.height * 0.82f),
                    radius = size.width * 0.72f
                ),
                center = Offset(size.width * 0.85f, size.height * 0.82f),
                radius = size.width * 0.72f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AuroraMint.copy(alpha = 0.10f), Color.Transparent),
                    center = Offset(size.width * 0.10f, size.height * 0.72f),
                    radius = size.width * 0.60f
                ),
                center = Offset(size.width * 0.10f, size.height * 0.72f),
                radius = size.width * 0.60f
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = AuroraSurfaceElevated,
                        contentColor = IceWhite,
                        actionColor = GlacierCyan
                    )
                }
            },
            bottomBar = {
                if (!isWide) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .background(AuroraPrismBorderBrush)
                        )
                        NavigationBar(containerColor = AuroraVoid.copy(alpha = 0.96f)) {
                            navItems.forEach { item ->
                                val selected = activeTab == item.tab
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { vm.selectTab(item.tab) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.label
                                        )
                                    },
                                    label = { Text(item.label, maxLines = 2, fontSize = 10.sp, lineHeight = 11.sp, textAlign = TextAlign.Center) },
                                    modifier = Modifier.testTag("nav_${item.tab.route}"),
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GlacierCyan,
                                        selectedTextColor = GlacierCyan,
                                        indicatorColor = GlacierCyanSoft,
                                        unselectedIconColor = ArcticMist.copy(alpha = 0.78f),
                                        unselectedTextColor = ArcticMist.copy(alpha = 0.78f)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWide) {
                    NavigationRail(
                        containerColor = AuroraSurface.copy(alpha = 0.95f),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        navItems.forEach { item ->
                            val selected = activeTab == item.tab
                            NavigationRailItem(
                                selected = selected,
                                onClick = { vm.selectTab(item.tab) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) },
                                modifier = Modifier.testTag("rail_${item.tab.route}"),
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = GlacierCyan,
                                    selectedTextColor = GlacierCyan,
                                    indicatorColor = GlacierCyanSoft,
                                    unselectedIconColor = ArcticMist.copy(alpha = 0.78f),
                                    unselectedTextColor = ArcticMist.copy(alpha = 0.78f)
                                )
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = 8.dp)
                            .background(AuroraPrismBorder.copy(alpha = 0.5f))
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = 760.dp)
                    ) {
                        if (showProfileMenu) {
                            val mCtx = LocalContext.current
                            val isRuM = lang == AppLang.RU
                            val g1 = listOf(
                                Triple("🛂", if (isRuM) "Гайды по визе и патенту" else "Visa & Patent Guides", "https://www.workmigrapro.com/guides"),
                                Triple("🧳", if (isRuM) "Чек-лист переезда" else "Relocation Checklist", "https://www.workmigrapro.com/jobseekers/relocation-checklist"),
                                Triple("💼", if (isRuM) "Бизнес в РФ (Business Setup)" else "Business Setup in Russia", "https://www.workmigrapro.com/business-setup")
                            )
                            val g2 = listOf(
                                Triple("ℹ️", if (isRuM) "О MigraStaff и правовая инфо (152-ФЗ)" else "About MigraStaff & Legal (152-FZ)", "https://www.workmigrapro.com/legal"),
                                Triple("💬", if (isRuM) "Помощь и WhatsApp (+7 912 850-03-33)" else "Help & WhatsApp (+7 912 850-03-33)", "https://wa.me/79128500333"),
                                Triple("🌐", if (isRuM) "Открыть workmigrapro.com" else "Open workmigrapro.com", "https://www.workmigrapro.com")
                            )
                            Column(
                                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        onClick = { showProfileMenu = false },
                                        shape = RoundedCornerShape(10.dp),
                                        color = AuroraSurface,
                                        border = BorderStroke(1.dp, AuroraPrismBorder)
                                    ) {
                                        Text("‹", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = IceWhite, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = if (isRuM) "Дополнительно" else "Additional info", style = MaterialTheme.typography.titleLarge, color = IceWhite, fontWeight = FontWeight.Bold)
                                }
                                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = AuroraSurface), border = BorderStroke(1.dp, AuroraPrismBorder)) {
                                    Column {
                                        g1.forEachIndexed { i, (ico, t, u) ->
                                            Row(modifier = Modifier.fillMaxWidth().clickable { runCatching { mCtx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(u))) } }.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Text(ico, fontSize = 18.sp)
                                                Text("   $t", color = IceWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                                Text("›", color = ArcticMist, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                            }
                                            if (i < g1.lastIndex) Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AuroraPrismBorder))
                                        }
                                    }
                                }
                                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = AuroraSurface), border = BorderStroke(1.dp, AuroraPrismBorder)) {
                                    Column {
                                        g2.forEachIndexed { i, (ico, t, u) ->
                                            Row(modifier = Modifier.fillMaxWidth().clickable { runCatching { mCtx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(u))) } }.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Text(ico, fontSize = 18.sp)
                                                Text("   $t", color = IceWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                                Text("›", color = ArcticMist, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                            }
                                            if (i < g2.lastIndex) Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AuroraPrismBorder))
                                        }
                                    }
                                }
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                                    border = BorderStroke(1.dp, AuroraMint.copy(alpha = 0.5f)),
                                    modifier = Modifier.clickable { runCatching { mCtx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.workmigrapro.com/employers"))) } }
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text("🏢", fontSize = 22.sp)
                                        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                            Text(if (isRuM) "Найти сотрудников" else "Find Employees", color = IceWhite, fontWeight = FontWeight.Bold, fontSize = 14.5.sp)
                                            Text(if (isRuM) "Работодателям и агентствам · Аутстаффинг" else "Employers & Agencies · Outstaffing", color = AuroraMint, fontSize = 12.sp)
                                        }
                                        Text("›", color = AuroraMint, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            return@Column
                        }
                        MigraBrandHeader(
                            lang = lang,
                            onToggleLang = vm::toggleLanguage,
                            showMenu = (activeTab == MainTab.PROFILE),
                            onOpenMenu = { showProfileMenu = true }
                        )

                        when (activeTab) {
                            MainTab.SEARCH -> SearchScreen(
                                lang = lang,
                                filterState = filterState,
                                vacancies = vacancies,
                                favoriteIds = favoriteIds,
                                appliedIds = appliedIds,
                                onQueryChange = vm::updateSearchQuery,
                                onOpenFilters = { vm.setFilterSheetVisible(true) },
                                onToggleSavedOnly = vm::toggleSavedOnlyFilter,
                                onSelectCategory = vm::selectCategory,
                                onResetFilters = vm::resetFilters,
                                onToggleFavorite = { job -> vm.toggleLocalFavorite(job.id) },
                                onOpenVacancy = vm::openVacancyDetail,
                                onApplyVacancy = { job -> vm.applyToVacancy(job) },
                                onJumpTab = vm::selectTab
                            )

                            MainTab.FX -> MyApplicationsScreen(
                                lang = lang,
                                isSignedIn = authState is AuthUiState.SignedIn,
                                applications = myApplications,
                                onBrowseJobs = { vm.selectTab(MainTab.SEARCH) },
                                onGoToProfile = { vm.selectTab(MainTab.PROFILE) }
                            )

                            MainTab.CRYPTO -> CryptoScreen(
                                lang = lang,
                                cryptoState = cryptoState,
                                rubPerUsd = fxState.rubPerUsd,
                                isSignedIn = authState is AuthUiState.SignedIn,
                                onSelectPlan = vm::selectCryptoPlan,
                                onSelectCoin = vm::selectCryptoCoin,
                                onCustomAmountChange = vm::updateCustomCryptoAmount,
                                onToggleCustomCurrency = vm::toggleCustomCryptoCurrency,
                                onContactChange = vm::updateCryptoContact,
                                onTxHashChange = vm::updateCryptoTxHash,
                                onCopyToast = vm::showMessage,
                                onSubmitReceipt = vm::submitCryptoReceipt
                            )

                            MainTab.ABOUT -> AboutScreen(
                                lang = lang,
                                onOpenTopic = vm::openInfoTopic,
                                onSelectCryptoPlan = vm::selectCryptoPlan,
                                onJumpTab = vm::selectTab
                            )

                            MainTab.PROFILE -> ProfileScreen(
                                lang = lang,
                                authState = authState,
                                authErrorMessage = authError,
                                employerProfile = employerProfile,
                                allApplications = allApplications,
                                allEmployers = allEmployers,
                                allCandidates = allCandidates,
                                liveJobs = liveJobs,
                                onGoogleSignIn = {
                                    if (activity != null) {
                                        vm.signInWithGoogle(activity)
                                    }
                                },
                                onEmailSignIn = vm::signInWithEmail,
                                onEmailSignUp = vm::signUpWithEmail,
                                onEmployerRegister = vm::registerEmployerAccount,
                                onResetPassword = vm::resetPassword,
                                onUpdateAppStatus = vm::updateApplicationStatus,
                                onUpdateEmployerStatus = vm::updateEmployerStatus,
                                onPublishJob = vm::publishJob,
                                onDeleteJob = vm::deleteJob,
                                onVerifyCandidate = vm::setCandidateVerified,
                                onBanCandidate = vm::setCandidateBanned,
                                onDeleteCandidate = vm::deleteCandidate,
                                onSignOut = { vm.signOut(context) },
                                onShowMessage = vm::showMessage
                            )
                        }
                    }
                }
            }
        }
    }

    selectedVacancy?.let { vacancy ->
        VacancyDetailSheet(
            vacancy = vacancy,
            lang = lang,
            isSignedIn = authState is AuthUiState.SignedIn,
            isApplied = appliedIds.contains(vacancy.id),
            onDismiss = vm::closeVacancyDetail,
            onApply = { vm.applyToVacancy(vacancy) },
            onGoToProfile = { vm.selectTab(MainTab.PROFILE) }
        )
    }

    selectedTopic?.let { topic ->
        InfoTopicDialog(
            topic = topic,
            lang = lang,
            onDismiss = vm::closeInfoTopic
        )
    }

    if (showFilterSheet) {
        FilterModalSheet(
            lang = lang,
            current = filterState,
            onDismiss = { vm.setFilterSheetVisible(false) },
            onReset = vm::resetFilters,
            onApply = vm::updateFilters
        )
    }
}

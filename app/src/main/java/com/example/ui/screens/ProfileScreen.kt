package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.MigraCatalogData
import com.example.data.MigraStrings
import com.example.data.firebase.StreamResult
import com.example.model.AppLang
import com.example.model.AuthUiState
import com.example.model.CandidateProfile
import com.example.model.EmployerAccount
import com.example.model.JobApplication
import com.example.model.JobVacancy
import com.example.ui.components.AuroraGradientButton
import com.example.ui.components.StatusTagChip
import com.example.ui.theme.ArcticMist
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.DeepNavyInk
import com.example.ui.theme.AuroraMintBorder
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.DeepNavyInk
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.DeepNavyInk
import com.example.ui.theme.AuroraMintBorder
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.AuroraMintBorder
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.DeepNavyInk
import com.example.ui.theme.AuroraMintBorder
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.AuroraPrismBorder
import com.example.ui.theme.AuroraPrismBorderBrush
import com.example.ui.theme.AuroraSurface
import com.example.ui.theme.AuroraSurfaceElevated
import com.example.ui.theme.CoralRose
import com.example.ui.theme.GlacierCyan
import com.example.ui.theme.GlacierCyanSoft
import com.example.ui.theme.HyperCobalt
import com.example.ui.theme.IceWhite
import com.example.ui.viewmodel.AccountViewModel

@Composable
fun ProfileScreen(
    lang: AppLang,
    authState: AuthUiState,
    authErrorMessage: String?,
    employerProfile: EmployerAccount? = null,
    allApplications: List<JobApplication> = emptyList(),
    allEmployers: List<EmployerAccount> = emptyList(),
    allCandidates: List<CandidateProfile> = emptyList(),
    liveJobs: List<JobVacancy> = emptyList(),
    onGoogleSignIn: () -> Unit,
    onEmailSignIn: (String, String) -> Unit = { _, _ -> },
    onEmailSignUp: (String, String) -> Unit = { _, _ -> },
    onEmployerRegister: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onResetPassword: (String) -> Unit = {},
    onUpdateAppStatus: (String, String) -> Unit = { _, _ -> },
    onUpdateEmployerStatus: (String, String) -> Unit = { _, _ -> },
    onPublishJob: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> },
    onDeleteJob: (String) -> Unit = {},
    onVerifyCandidate: (String, Boolean) -> Unit = { _, _ -> },
    onBanCandidate: (String, Boolean) -> Unit = { _, _ -> },
    onDeleteCandidate: (String, String) -> Unit = { _, _ -> },
    onSignOut: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val copy = MigraStrings.of(lang)

    when (authState) {
        AuthUiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GlacierCyan)
            }
        }

        AuthUiState.SignedOut -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.2.dp, AuroraPrismBorderBrush),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = GlacierCyanSoft,
                            border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.Lock,
                                    contentDescription = null,
                                    tint = GlacierCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = copy.profileGateBadge,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GlacierCyan
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .size(94.dp)
                                .clip(CircleShape)
                                .border(2.dp, AuroraPrismBorderBrush, CircleShape)
                                .padding(3.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.migrastaff_logo),
                                contentDescription = "MigraStaff Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = copy.profileGateTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            color = IceWhite
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = copy.profileGateSub,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArcticMist
                        )
                        if (!authErrorMessage.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = authErrorMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = CoralRose
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        var isEmpRegMode by remember { mutableStateOf(false) }
                        var companyName by remember { mutableStateOf("") }
                        var companyCity by remember { mutableStateOf("Moscow") }
                        var emailOrUser by remember { mutableStateOf("") }
                        var passInput by remember { mutableStateOf("") }
                        var showPass by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isEmpRegMode = false },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, if (!isEmpRegMode) GlacierCyan else AuroraPrismBorder)
                            ) {
                                Text("Candidate / Director", color = if (!isEmpRegMode) GlacierCyan else ArcticMist, maxLines = 1)
                            }
                            OutlinedButton(
                                onClick = { isEmpRegMode = true },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, if (isEmpRegMode) GlacierCyan else AuroraPrismBorder)
                            ) {
                                Text("Employer Signup", color = if (isEmpRegMode) GlacierCyan else ArcticMist, maxLines = 1)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        if (isEmpRegMode) {
                            OutlinedTextField(
                                value = companyName,
                                onValueChange = { companyName = it },
                                label = { Text("Company Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = companyCity,
                                onValueChange = { companyCity = it },
                                label = { Text("City (Russia)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        OutlinedTextField(
                            value = emailOrUser,
                            onValueChange = { emailOrUser = it },
                            label = { Text(if (isEmpRegMode) "Company Email" else if (lang == AppLang.RU) "Email или @username" else "Email or @username") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = passInput,
                            onValueChange = { passInput = it },
                            label = { Text(if (lang == AppLang.RU) "Пароль" else "Password") },
                            singleLine = true,
                            visualTransformation = if (showPass) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
                            trailingIcon = {
                                IconButton(onClick = { showPass = !showPass }) {
                                    Icon(
                                        imageVector = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (showPass) "Hide password" else "Show password",
                                        tint = ArcticMist
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        if (isEmpRegMode) {
                            Button(
                                onClick = { onEmployerRegister(companyName, companyCity, emailOrUser, passInput) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = AuroraMint, contentColor = DeepNavyInk)
                            ) {
                                Text("Register Employer Account", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onEmailSignIn(emailOrUser, passInput) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = AuroraMint, contentColor = DeepNavyInk)
                                ) {
                                    Text(if (lang == AppLang.RU) "Войти" else "Sign In", fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { onEmailSignUp(emailOrUser, passInput) },
                                    modifier = Modifier.weight(1f),
                                    border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.6f))
                                ) {
                                    Text(if (lang == AppLang.RU) "Регистрация" else "Create Account", color = GlacierCyan)
                                }
                            }
                        }
                        TextButton(onClick = { onResetPassword(emailOrUser) }) {
                            Text(if (lang == AppLang.RU) "Забыли пароль?" else "Forgot Password?", color = ArcticMist)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AuroraGradientButton(
                            text = copy.profileGoogleBtn,
                            onClick = onGoogleSignIn,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("google_sign_in_button")
                        )
                    }
                }
            }
        }

        is AuthUiState.SignedIn -> {
            val isDirector = authState.email.equals("rajgaikwad9891@gmail.com", ignoreCase = true)
            if (isDirector) {
                DirectorPanelContent(
                    lang = lang,
                    authState = authState,
                    allApplications = allApplications,
                    allEmployers = allEmployers,
                    allCandidates = allCandidates,
                    liveJobs = liveJobs,
                    onUpdateAppStatus = onUpdateAppStatus,
                    onUpdateEmployerStatus = onUpdateEmployerStatus,
                    onPublishJob = { t, l, sal -> onPublishJob(t, l, sal, "MigraStaff Direct", authState.uid) },
                    onDeleteJob = onDeleteJob,
                    onVerifyCandidate = onVerifyCandidate,
                    onBanCandidate = onBanCandidate,
                    onDeleteCandidate = onDeleteCandidate,
                    onSignOut = onSignOut,
                    modifier = modifier
                )
            } else if (employerProfile != null) {
                EmployerPortalContent(
                    authState = authState,
                    employer = employerProfile,
                    liveJobs = liveJobs,
                    onPublishJob = { t, l, sal ->
                        onPublishJob(t, l, sal, employerProfile.companyName.ifEmpty { "Employer" }, authState.uid)
                    },
                    onSignOut = onSignOut,
                    modifier = modifier
                )
            } else {
                val context = LocalContext.current
                val accountVm: AccountViewModel = viewModel(
                    key = "account_${authState.uid}",
                    factory = AccountViewModel.Factory(authState.uid, context)
                )
                SignedInProfileContent(
                    lang = lang,
                    authState = authState,
                    accountVm = accountVm,
                    onSignOut = onSignOut,
                    onShowMessage = onShowMessage,
                    modifier = modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignedInProfileContent(
    lang: AppLang,
    authState: AuthUiState.SignedIn,
    accountVm: AccountViewModel,
    onSignOut: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val copy = MigraStrings.of(lang)
    val profileStream by accountVm.profileStream.collectAsStateWithLifecycle()
    val favoritesStream by accountVm.favoritesStream.collectAsStateWithLifecycle()
    val messagesStream by accountVm.messagesStream.collectAsStateWithLifecycle()
    val receiptsStream by accountVm.receiptsStream.collectAsStateWithLifecycle()
    val isSaving by accountVm.isSavingProfile.collectAsStateWithLifecycle()
    val isSending by accountVm.isSendingMessage.collectAsStateWithLifecycle()

    var subTab by rememberSaveable { mutableIntStateOf(0) }
    val subTabs = listOf(
        copy.profileRegTitle,
        copy.profileChatTitle,
        copy.profileFavsTitle,
        copy.profileReceiptsTitle
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Surface(
            color = AuroraSurface,
            border = BorderStroke(1.dp, AuroraPrismBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(authState.displayName, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                    if (authState.email.isNotEmpty()) {
                        Text(authState.email, style = MaterialTheme.typography.bodySmall, color = ArcticMist)
                    }
                }
                TextButton(
                    onClick = onSignOut,
                    modifier = Modifier.testTag("sign_out_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = CoralRose,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(copy.profileSignOut, color = CoralRose)
                }
            }
        }

        SecondaryTabRow(
            selectedTabIndex = subTab,
            containerColor = AuroraSurface,
            contentColor = GlacierCyan,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(subTab),
                    color = GlacierCyan
                )
            }
        ) {
            subTabs.forEachIndexed { idx, title ->
                Tab(
                    selected = subTab == idx,
                    onClick = { subTab = idx },
                    text = {
                        Text(
                            title,
                            maxLines = 1,
                            color = if (subTab == idx) GlacierCyan else ArcticMist
                        )
                    }
                )
            }
        }

        when (subTab) {
            0 -> RegistrationTab(
                lang = lang,
                profileStream = profileStream,
                isSaving = isSaving,
                onSave = { prof ->
                    accountVm.saveCandidateProfile(prof) { res ->
                        res.onSuccess {
                            onShowMessage(
                                if (lang == AppLang.RU) "Анкета сохранена в облаке" else "Candidate profile locked & saved"
                            )
                        }.onFailure { err ->
                            onShowMessage(err.localizedMessage ?: "Failed to save")
                        }
                    }
                }
            )

            1 -> DirectorChatTab(
                lang = lang,
                messagesStream = messagesStream,
                isSending = isSending,
                onSend = { text ->
                    accountVm.sendDirectorMessage(text, lang.code) { res ->
                        res.onFailure {
                            onShowMessage(it.localizedMessage ?: "Failed to send")
                        }
                    }
                }
            )

            2 -> CloudFavoritesTab(lang = lang, favoritesStream = favoritesStream)
            3 -> CloudReceiptsTab(lang = lang, receiptsStream = receiptsStream)
        }
    }
}

@Composable
private fun RegistrationTab(
    lang: AppLang,
    profileStream: StreamResult<CandidateProfile?>,
    isSaving: Boolean,
    onSave: (CandidateProfile) -> Unit
) {
    val copy = MigraStrings.of(lang)
    val loaded = (profileStream as? StreamResult.Success)?.data

    var username by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("UZ") }
    var profession by remember { mutableStateOf("Welder") }
    var expYears by remember { mutableStateOf("3") }
    var expectedSalary by remember { mutableStateOf("80000") }
    var languages by remember { mutableStateOf("Russian, English") }
    var skills by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+7") }
    var dob by remember { mutableStateOf("1995-01-15") }
    var about by remember { mutableStateOf("") }
    var pickedPhotos by remember { mutableStateOf<List<Uri>>(emptyList()) }

    LaunchedEffect(loaded) {
        if (loaded != null) {
            username = loaded.username
            surname = loaded.surname
            firstName = loaded.firstName
            countryCode = loaded.countryCode
            profession = loaded.profession
            expYears = loaded.experienceYears.toString()
            expectedSalary = loaded.expectedSalary.toString()
            languages = loaded.languages
            skills = loaded.skills
            phone = loaded.phone
            dob = loaded.dateOfBirth
            about = loaded.about
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5)
    ) { uris ->
        pickedPhotos = uris
    }

    var countryMenu by remember { mutableStateOf(false) }
    var profMenu by remember { mutableStateOf(false) }

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
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (loaded != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = AuroraMintSoft,
                    border = BorderStroke(1.dp, AuroraMintBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = AuroraMint)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == AppLang.RU) {
                                "Статус анкеты: ${loaded.verStatus} · @${loaded.username}"
                            } else {
                                "Profile status: ${loaded.verStatus} · @${loaded.username}"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = AuroraMint
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.dp, AuroraPrismBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text(if (lang == AppLang.RU) "Логин (min 3)" else "Username (min 3)") },
                        singleLine = true,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = surname,
                            onValueChange = { surname = it },
                            label = { Text(if (lang == AppLang.RU) "Фамилия*" else "Surname*") },
                            singleLine = true,
                            colors = fieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text(if (lang == AppLang.RU) "Имя*" else "First Name*") },
                            singleLine = true,
                            colors = fieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { countryMenu = true },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, AuroraPrismBorder),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                            ) {
                                Text(MigraCatalogData.countryName(countryCode, lang))
                            }
                            DropdownMenu(
                                expanded = countryMenu,
                                onDismissRequest = { countryMenu = false },
                                containerColor = AuroraSurfaceElevated
                            ) {
                                MigraCatalogData.countries.forEach { c ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "${c.flag} ${if (lang == AppLang.RU) c.nameRu else c.nameEn}",
                                                color = IceWhite
                                            )
                                        },
                                        onClick = {
                                            countryCode = c.code
                                            countryMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { profMenu = true },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, AuroraPrismBorder),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                            ) {
                                Text(MigraCatalogData.profName(profession, lang))
                            }
                            DropdownMenu(
                                expanded = profMenu,
                                onDismissRequest = { profMenu = false },
                                containerColor = AuroraSurfaceElevated
                            ) {
                                MigraCatalogData.categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(MigraCatalogData.profName(cat.profEn, lang), color = IceWhite) },
                                        onClick = {
                                            profession = cat.profEn
                                            profMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = expYears,
                            onValueChange = { expYears = it.filter(Char::isDigit) },
                            label = { Text(if (lang == AppLang.RU) "Стаж (лет)" else "Years Exp") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = fieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = expectedSalary,
                            onValueChange = { expectedSalary = it.filter(Char::isDigit) },
                            label = { Text(if (lang == AppLang.RU) "Зарплата (₽)" else "Salary (₽/mo)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = fieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("WhatsApp / Phone") },
                        singleLine = true,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = skills,
                        onValueChange = { skills = it },
                        label = { Text(if (lang == AppLang.RU) "Навыки" else "Key Skills") },
                        singleLine = true,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = about,
                        onValueChange = { about = it },
                        label = { Text(if (lang == AppLang.RU) "О себе и опыте" else "About & Work History") },
                        minLines = 3,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedButton(
                        onClick = {
                            photoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GlacierCyan)
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (pickedPhotos.isEmpty()) copy.addPhotosBtn
                            else "${pickedPhotos.size} photo(s) selected"
                        )
                    }

                    AuroraGradientButton(
                        text = copy.saveProfileBtn,
                        enabled = !isSaving,
                        onClick = {
                            onSave(
                                CandidateProfile(
                                    username = username.trim().ifEmpty { "candidate_01" },
                                    surname = surname.trim().ifEmpty { "Worker" },
                                    firstName = firstName.trim().ifEmpty { "Candidate" },
                                    countryCode = countryCode,
                                    profession = profession,
                                    experienceYears = expYears.toIntOrNull()?.coerceIn(0, 70) ?: 1,
                                    expectedSalary = expectedSalary.toIntOrNull()?.coerceIn(0, 10_000_000) ?: 60000,
                                    languages = languages.trim(),
                                    skills = skills.trim(),
                                    about = about.trim(),
                                    phone = phone.trim(),
                                    dateOfBirth = dob.trim(),
                                    photoCount = maxOf(pickedPhotos.size, loaded?.photoCount ?: 0)
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_profile_button")
                    )
                }
            }
        }
    }
}

@Composable
private fun DirectorChatTab(
    lang: AppLang,
    messagesStream: StreamResult<List<com.example.model.SupportMessage>>,
    isSending: Boolean,
    onSend: (String) -> Unit
) {
    val copy = MigraStrings.of(lang)
    var draft by remember { mutableStateOf("") }
    val messages = (messagesStream as? StreamResult.Success)?.data.orEmpty()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                        border = BorderStroke(1.dp, AuroraPrismBorder)
                    ) {
                        Text(
                            text = copy.chatEmpty,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArcticMist,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(messages, key = { it.id }) { msg ->
                    val isCandidate = msg.senderRole == "candidate"
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isCandidate) GlacierCyanSoft else AuroraSurface,
                        border = BorderStroke(
                            1.dp,
                            if (isCandidate) GlacierCyan.copy(alpha = 0.45f) else AuroraPrismBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.textOriginal,
                                style = MaterialTheme.typography.bodyLarge,
                                color = IceWhite
                            )
                            if (msg.textTranslated.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = msg.textTranslated,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuroraMint
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                placeholder = { Text(copy.chatPlaceholder, color = ArcticMist) },
                modifier = Modifier.weight(1f),
                singleLine = true,
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
            Button(
                onClick = {
                    val toSend = draft
                    draft = ""
                    onSend(toSend)
                },
                enabled = !isSending && draft.isNotBlank(),
                modifier = Modifier.height(52.dp).testTag("chat_send_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HyperCobalt,
                    contentColor = IceWhite
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = copy.chatSend)
            }
        }
    }
}

@Composable
private fun CloudFavoritesTab(
    lang: AppLang,
    favoritesStream: StreamResult<List<com.example.model.FavoriteWorker>>
) {
    val list = (favoritesStream as? StreamResult.Success)?.data.orEmpty()
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (list.isEmpty()) {
            item {
                Text(
                    text = if (lang == AppLang.RU) "Нет сохранённых специалистов" else "No cloud-saved workers yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArcticMist
                )
            }
        } else {
            items(list, key = { it.id }) { fav ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.dp, AuroraPrismBorder)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(fav.workerName, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                            Text(
                                text = "${MigraCatalogData.profName(fav.profession, lang)} · ${fav.countryCode}",
                                style = MaterialTheme.typography.bodySmall,
                                color = ArcticMist
                            )
                        }
                        StatusTagChip("₽${fav.salaryRub}", GlacierCyanSoft, GlacierCyan)
                    }
                }
            }
        }
    }
}

@Composable
private fun CloudReceiptsTab(
    lang: AppLang,
    receiptsStream: StreamResult<List<com.example.model.CryptoPaymentReceipt>>
) {
    val list = (receiptsStream as? StreamResult.Success)?.data.orEmpty()
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (list.isEmpty()) {
            item {
                Text(
                    text = if (lang == AppLang.RU) "Крипто-чеки пока не отправлены" else "No submitted crypto TXIDs yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArcticMist
                )
            }
        } else {
            items(list, key = { it.id }) { r ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.dp, AuroraPrismBorder)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(r.refId, style = MaterialTheme.typography.titleSmall, color = IceWhite)
                            StatusTagChip(r.status, AuroraMintSoft, AuroraMint)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${r.amountCrypto} (${r.coinKey})", style = MaterialTheme.typography.bodyMedium, color = GlacierCyan)
                        Text("TXID: ${r.txHash}", style = MaterialTheme.typography.bodySmall, color = ArcticMist)
                    }
                }
            }
        }
    }
}

@Composable
private fun DirectorPanelContent(
    lang: AppLang,
    authState: AuthUiState.SignedIn,
    allApplications: List<JobApplication>,
    allEmployers: List<EmployerAccount>,
    allCandidates: List<CandidateProfile>,
    liveJobs: List<JobVacancy>,
    onUpdateAppStatus: (String, String) -> Unit,
    onUpdateEmployerStatus: (String, String) -> Unit,
    onPublishJob: (String, String, String) -> Unit,
    onDeleteJob: (String) -> Unit,
    onVerifyCandidate: (String, Boolean) -> Unit,
    onBanCandidate: (String, Boolean) -> Unit,
    onDeleteCandidate: (String, String) -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var jTitle by remember { mutableStateOf("") }
    var jLoc by remember { mutableStateOf("") }
    var jSal by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Color.Transparent),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.2.dp, AuroraPrismBorderBrush)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Director Control Panel", style = MaterialTheme.typography.titleLarge, color = IceWhite)
                        Text(
                            "Jobs: ${liveJobs.size} · Apps: ${allApplications.size} · Employers: ${allEmployers.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArcticMist
                        )
                    }
                    TextButton(onClick = onSignOut) {
                        Text("Logout", color = CoralRose, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    "Apps (${allApplications.size})",
                    "Emps (${allEmployers.size})",
                    "Jobs (${liveJobs.size})",
                    "Cands (${allCandidates.size})"
                ).forEachIndexed { idx, label ->
                    Button(
                        onClick = { tab = idx },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tab == idx) HyperCobalt else AuroraSurfaceElevated,
                            contentColor = IceWhite
                        )
                    ) {
                        Text(label, maxLines = 1)
                    }
                }
            }
        }

        if (tab == 0) {
            if (allApplications.isEmpty()) {
                item { Text("No job applications yet.", color = ArcticMist) }
            } else {
                items(allApplications, key = { it.id }, contentType = { "dir_app" }) { app ->
                    var menuOpen by remember { mutableStateOf(false) }
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                        border = BorderStroke(1.dp, AuroraPrismBorder)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(app.candidateName.ifEmpty { app.candidateEmail }, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                                Text("${app.jobTitle} · ${app.location}", style = MaterialTheme.typography.bodySmall, color = ArcticMist)
                            }
                            Box {
                                OutlinedButton(onClick = { menuOpen = true }) {
                                    Text(app.status.replaceFirstChar { it.uppercase() }, color = GlacierCyan)
                                }
                                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                                    listOf("pending", "shortlisted", "hired", "rejected").forEach { st ->
                                        DropdownMenuItem(
                                            text = { Text(st.replaceFirstChar { it.uppercase() }) },
                                            onClick = {
                                                menuOpen = false
                                                onUpdateAppStatus(app.id, st)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tab == 1) {
            if (allEmployers.isEmpty()) {
                item { Text("No registered employers yet.", color = ArcticMist) }
            } else {
                items(allEmployers, key = { it.uid }, contentType = { "dir_emp" }) { emp ->
                    val isApproved = emp.status.equals("approved", true)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                        border = BorderStroke(1.dp, AuroraPrismBorder)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(emp.companyName.ifEmpty { emp.email }, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                                Text("${emp.city} · ${emp.status}", style = MaterialTheme.typography.bodySmall, color = if (isApproved) AuroraMint else ArcticMist)
                            }
                            Button(
                                onClick = { onUpdateEmployerStatus(emp.uid, if (isApproved) "suspended" else "approved") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isApproved) CoralRose else HyperCobalt,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(if (isApproved) "Suspend" else "Approve")
                            }
                        }
                    }
                }
            }
        } else if (tab == 2) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.dp, AuroraPrismBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("+ Post New Job Vacancy", style = MaterialTheme.typography.titleMedium, color = IceWhite)
                        OutlinedTextField(value = jTitle, onValueChange = { jTitle = it }, label = { Text("Job Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = jLoc, onValueChange = { jLoc = it }, label = { Text("City / Location") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = jSal, onValueChange = { jSal = it }, label = { Text("Monthly Salary") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        AuroraGradientButton(
                            text = "Publish Job Live",
                            onClick = {
                                onPublishJob(jTitle, jLoc, jSal)
                                jTitle = ""; jLoc = ""; jSal = ""
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            items(liveJobs, key = { it.id }, contentType = { "dir_job" }) { job ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.dp, AuroraPrismBorder)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(job.titleEn, style = MaterialTheme.typography.titleSmall, color = IceWhite)
                            Text("${job.cityEn} · ${job.salaryMinRub} ₽", style = MaterialTheme.typography.bodySmall, color = ArcticMist)
                        }
                        if (job.docId.isNotBlank()) {
                            TextButton(onClick = { onDeleteJob(job.docId) }) {
                                Text("Delete", color = CoralRose)
                            }
                        }
                    }
                }
            }
        } else {
            if (allCandidates.isEmpty()) {
                item { Text("No candidates yet.", color = ArcticMist) }
            } else {
                items(allCandidates, key = { it.id }, contentType = { "dir_cand" }) { c ->
                    val isVer = c.verStatus == "verified"
                    val cName = "${c.surname} ${c.firstName}".trim().ifEmpty { c.username.ifEmpty { "Candidate" } }
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                        border = BorderStroke(1.dp, if (c.banned) CoralRose else AuroraPrismBorder)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(cName, style = MaterialTheme.typography.titleMedium, color = IceWhite)
                                    Text("${c.profession} · ${c.countryCode} · @${c.username}", style = MaterialTheme.typography.bodySmall, color = ArcticMist)
                                }
                                Text(
                                    if (c.banned) "BANNED" else if (isVer) "Verified" else "Pending",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (c.banned) CoralRose else if (isVer) AuroraMint else GlacierCyan
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { onVerifyCandidate(c.userId, !isVer) }, modifier = Modifier.weight(1f)) {
                                    Text(if (isVer) "Unverify" else "Verify", color = GlacierCyan, maxLines = 1)
                                }
                                Button(
                                    onClick = { onBanCandidate(c.userId, !c.banned) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = if (c.banned) AuroraMint else HyperCobalt, contentColor = Color.White)
                                ) { Text(if (c.banned) "Unban" else "Ban", maxLines = 1) }
                                Button(
                                    onClick = { onDeleteCandidate(c.userId, c.username) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = CoralRose, contentColor = Color.White)
                                ) { Text("Delete", maxLines = 1) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmployerPortalContent(
    authState: AuthUiState.SignedIn,
    employer: EmployerAccount,
    liveJobs: List<JobVacancy>,
    onPublishJob: (String, String, String) -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isApproved = employer.status.equals("approved", true)
    var jTitle by remember { mutableStateOf("") }
    var jLoc by remember { mutableStateOf("") }
    var jSal by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Color.Transparent),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                border = BorderStroke(1.dp, AuroraPrismBorder)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(employer.companyName.ifEmpty { authState.displayName }, style = MaterialTheme.typography.titleLarge, color = IceWhite)
                        Text(
                            if (isApproved) "✓ Approved Employer (${employer.city})" else "Pending Director Approval (${employer.status})",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isApproved) AuroraMint else GlacierCyan
                        )
                    }
                    TextButton(onClick = onSignOut) {
                        Text("Log Out", color = CoralRose)
                    }
                }
            }
        }

        if (isApproved) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.dp, AuroraPrismBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Post a Job Live", style = MaterialTheme.typography.titleMedium, color = IceWhite)
                        OutlinedTextField(value = jTitle, onValueChange = { jTitle = it }, label = { Text("Job Title (e.g. Welder)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = jLoc, onValueChange = { jLoc = it }, label = { Text("City (e.g. Moscow)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = jSal, onValueChange = { jSal = it }, label = { Text("Monthly Salary (e.g. 90,000 RUB)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        AuroraGradientButton(
                            text = "Publish Job Live →",
                            onClick = {
                                onPublishJob(jTitle, jLoc, jSal)
                                jTitle = ""; jLoc = ""; jSal = ""
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

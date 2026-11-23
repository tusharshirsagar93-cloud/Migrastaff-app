package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppLang
import com.example.model.JobApplication
import com.example.ui.components.AuroraGradientButton
import com.example.ui.components.StatusTagChip
import com.example.ui.theme.ArcticMist
import com.example.ui.theme.AuroraMint
import com.example.ui.theme.AuroraMintSoft
import com.example.ui.theme.AuroraPrismBorder
import com.example.ui.theme.AuroraSurface
import com.example.ui.theme.CoralRose
import com.example.ui.theme.CoralRoseSoft
import com.example.ui.theme.GlacierCyan
import com.example.ui.theme.GlacierCyanSoft
import com.example.ui.theme.IceWhite
import com.example.ui.theme.SolarGoldSoft
import com.example.ui.theme.SolarGoldText

@Composable
fun MyApplicationsScreen(
    lang: AppLang,
    isSignedIn: Boolean,
    applications: List<JobApplication>,
    onBrowseJobs: () -> Unit,
    onGoToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shortlistedCount = applications.count { it.status.equals("shortlisted", true) || it.status.equals("hired", true) }
    val pendingCount = applications.count { it.status.equals("pending", true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = if (lang == AppLang.RU) "Мои отклики" else "My Job Applications",
                    style = MaterialTheme.typography.headlineMedium,
                    color = IceWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (lang == AppLang.RU) "Статус обновляется в реальном времени" else "Live status synced with website",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArcticMist
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusTagChip("${applications.size} Applied", GlacierCyanSoft, GlacierCyan)
                    StatusTagChip("$shortlistedCount Shortlisted", AuroraMintSoft, AuroraMint)
                    StatusTagChip("$pendingCount Pending", SolarGoldSoft, SolarGoldText)
                }
            }
        }

        if (!isSignedIn) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.dp, AuroraPrismBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = if (lang == AppLang.RU) "Войдите, чтобы видеть статус откликов" else "Sign in to track your applications",
                            style = MaterialTheme.typography.titleMedium,
                            color = IceWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (lang == AppLang.RU) "Все ваши отклики с сайта и из приложения синхронизируются в профиле." else "All your job applications from the website and app sync live here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArcticMist
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AuroraGradientButton(
                            text = if (lang == AppLang.RU) "Войти в Профиль" else "Sign In on Profile",
                            onClick = onGoToProfile,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        } else if (applications.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.dp, AuroraPrismBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = if (lang == AppLang.RU) "Вы ещё не откликались на вакансии" else "No job applications yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = IceWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (lang == AppLang.RU) "Откройте вкладку Поиск и нажмите «Откликнуться» на любой вакансии." else "Open Search and tap Apply on any live job vacancy.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArcticMist
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AuroraGradientButton(
                            text = if (lang == AppLang.RU) "Смотреть вакансии →" else "Browse Jobs →",
                            onClick = onBrowseJobs,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        } else {
            items(applications, key = { it.id }, contentType = { "application" }) { app ->
                val st = app.status.lowercase()
                val (bg, fg) = when (st) {
                    "shortlisted" -> GlacierCyanSoft to GlacierCyan
                    "hired" -> AuroraMintSoft to AuroraMint
                    "rejected" -> CoralRoseSoft to CoralRose
                    else -> SolarGoldSoft to SolarGoldText
                }
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AuroraSurface),
                    border = BorderStroke(1.dp, AuroraPrismBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = app.jobTitle.ifEmpty { "Job Vacancy" },
                                style = MaterialTheme.typography.titleMedium,
                                color = IceWhite,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(shape = RoundedCornerShape(20.dp), color = bg) {
                                Text(
                                    text = st.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = fg,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = listOf(app.location, app.salary).filter { it.isNotBlank() }.joinToString(" · "),
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArcticMist
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = onGoToProfile,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, GlacierCyan.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GlacierCyan)
                        ) {
                            Text(if (lang == AppLang.RU) "Написать директору →" else "Message Director →", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            item {
                OutlinedButton(
                    onClick = onBrowseJobs,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, AuroraPrismBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = IceWhite)
                ) {
                    Text(if (lang == AppLang.RU) "Смотреть ещё вакансии →" else "Browse More Jobs →")
                }
            }
        }
    }
}

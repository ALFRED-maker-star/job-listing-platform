package com.example.ui.employer

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ApplicationStatus
import com.example.model.EmployerAccount
import com.example.model.Job
import com.example.model.JobApplication
import com.example.ui.components.ApplicationStatusBadge
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.AppliedGreen
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandBlueDark
import com.example.ui.theme.BrandBluePrimary
import com.example.ui.theme.BrandBlueSubtle
import com.example.ui.theme.InterviewPurple
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.UnderReviewAmber

@Composable
fun EmployerDashboardScreen(
    employerAccount: EmployerAccount,
    jobs: List<Job>,
    applications: List<JobApplication>,
    onPostNewJobClick: () -> Unit,
    onViewAllPostingsClick: () -> Unit,
    onViewAllApplicantsClick: () -> Unit,
    onApplicantClick: (JobApplication) -> Unit,
    isDesktopLayout: Boolean = false,
    modifier: Modifier = Modifier
) {
    val activePostings = jobs.count { it.isActive }
    val totalApplicants = applications.size
    val newThisWeek = applications.count { System.currentTimeMillis() - it.appliedTimestamp < 7 * 86400000L }

    val underReviewCount = applications.count { it.status == ApplicationStatus.UNDER_REVIEW }
    val interviewingCount = applications.count { it.status == ApplicationStatus.INTERVIEW }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .testTag("employer_dashboard_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Welcome Header & Quick Actions
        item {
            Surface(
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Welcome, ${employerAccount.name}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "${employerAccount.companyName} Hiring Portal • ${employerAccount.role}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Button(
                            onClick = onPostNewJobClick,
                            colors = ButtonDefaults.buttonColors(containerColor = BrandBluePrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("dashboard_post_job_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Post Job", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3 Primary Required Stat Cards (Active Postings, Total Applicants, New This Week)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "Key Metrics",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    ),
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                if (isDesktopLayout) {
                    // Desktop 4-column horizontal grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            title = "Active Postings",
                            value = "$activePostings",
                            subtitle = "${jobs.size} Total created",
                            icon = Icons.Default.Work,
                            iconTint = BrandBluePrimary,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            title = "Total Applicants",
                            value = "$totalApplicants",
                            subtitle = "Across all roles",
                            icon = Icons.Default.Group,
                            iconTint = InterviewPurple,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            title = "New This Week",
                            value = "+$newThisWeek",
                            subtitle = "Recent candidates",
                            icon = Icons.Default.TrendingUp,
                            iconTint = AppliedGreen,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            title = "In Pipeline",
                            value = "${underReviewCount + interviewingCount}",
                            subtitle = "$interviewingCount in interview",
                            icon = Icons.Default.CheckCircle,
                            iconTint = UnderReviewAmber,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    // Mobile stacked grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            title = "Active Postings",
                            value = "$activePostings",
                            subtitle = "${jobs.size} Total roles",
                            icon = Icons.Default.Work,
                            iconTint = BrandBluePrimary,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            title = "Total Applicants",
                            value = "$totalApplicants",
                            subtitle = "All candidates",
                            icon = Icons.Default.Group,
                            iconTint = InterviewPurple,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            title = "New This Week",
                            value = "+$newThisWeek",
                            subtitle = "Needs review",
                            icon = Icons.Default.TrendingUp,
                            iconTint = AppliedGreen,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            title = "Active Pipeline",
                            value = "${underReviewCount + interviewingCount}",
                            subtitle = "$interviewingCount interviewing",
                            icon = Icons.Default.CheckCircle,
                            iconTint = UnderReviewAmber,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Quick Navigation Tiles
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewAllPostingsClick,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.ListAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("My Postings")
                }

                OutlinedButton(
                    onClick = onViewAllApplicantsClick,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Group, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("All Applicants")
                }
            }
        }

        // Recent Candidates Activity Feed
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recent Candidate Applications",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                TextButton(onClick = onViewAllApplicantsClick) {
                    Text("View all (${applications.size})", color = BrandBluePrimary)
                }
            }
        }

        items(applications.take(5), key = { it.id }) { app ->
            val matchingJob = jobs.find { it.id == app.jobId }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .clickable { onApplicantClick(app) }
                    .testTag("dashboard_applicant_${app.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BrandBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = app.applicantName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                color = BrandBlueDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = app.applicantName,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AppliedGreen.copy(alpha = 0.12f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${app.matchScore}% Match",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF15803D),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                            Text(
                                text = "Applied for ${matchingJob?.title ?: "Position"}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                maxLines = 1
                            )
                            Text(
                                text = "${app.appliedDate} • ${app.cvFileName}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                            )
                        }
                    }

                    ApplicationStatusBadge(status = app.status)
                }
            }
        }
    }
}

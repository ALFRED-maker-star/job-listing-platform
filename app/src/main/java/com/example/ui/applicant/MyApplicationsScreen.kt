package com.example.ui.applicant

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ApplicationStatus
import com.example.model.Job
import com.example.model.JobApplication
import com.example.ui.components.ApplicationStatusBadge
import com.example.ui.components.CompanyLogoBadge
import com.example.ui.theme.AppliedGreen
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandBluePrimary
import com.example.ui.theme.BrandBlueSubtle
import com.example.ui.theme.InterviewPurple
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.UnderReviewAmber

@Composable
fun MyApplicationsScreen(
    applications: List<JobApplication>,
    jobs: List<Job>,
    userEmail: String,
    onJobClick: (Job) -> Unit,
    onExploreJobsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("All Applications", "In Progress", "Interviews")

    val userApps = applications.filter { it.applicantEmail == userEmail }

    val filteredApps = when (selectedFilterIndex) {
        1 -> userApps.filter { it.status == ApplicationStatus.APPLIED || it.status == ApplicationStatus.UNDER_REVIEW }
        2 -> userApps.filter { it.status == ApplicationStatus.INTERVIEW }
        else -> userApps
    }

    val totalApplied = userApps.size
    val totalUnderReview = userApps.count { it.status == ApplicationStatus.UNDER_REVIEW }
    val totalInterviewing = userApps.count { it.status == ApplicationStatus.INTERVIEW }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .testTag("my_applications_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            // Header Stats Strip
            Surface(
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Application Tracker",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Track your submissions, interview invitations, and status updates",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TrackerStatBox(
                            label = "Submitted",
                            count = totalApplied,
                            color = BrandBluePrimary,
                            modifier = Modifier.weight(1f)
                        )
                        TrackerStatBox(
                            label = "In Review",
                            count = totalUnderReview,
                            color = UnderReviewAmber,
                            modifier = Modifier.weight(1f)
                        )
                        TrackerStatBox(
                            label = "Interviews",
                            count = totalInterviewing,
                            color = InterviewPurple,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tab Row
                    TabRow(
                        selectedTabIndex = selectedFilterIndex,
                        containerColor = Color.White,
                        contentColor = BrandBluePrimary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedFilterIndex]),
                                color = BrandBluePrimary
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedFilterIndex == index,
                                onClick = { selectedFilterIndex = index },
                                text = {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (selectedFilterIndex == index) FontWeight.Bold else FontWeight.Medium
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        if (filteredApps.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No applications in this view",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Browse open positions and apply to track them here.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }
        } else {
            items(filteredApps, key = { it.id }) { app ->
                val matchingJob = jobs.find { it.id == app.jobId }

                ApplicationListItemCard(
                    application = app,
                    job = matchingJob,
                    onViewJob = {
                        matchingJob?.let { onJobClick(it) }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("application_card_${app.id}")
                )
            }
        }
    }
}

@Composable
private fun TrackerStatBox(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = color.copy(alpha = 0.08f),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
            )
        }
    }
}

@Composable
fun ApplicationListItemCard(
    application: JobApplication,
    job: Job?,
    onViewJob: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Company Logo, Job Title, Company, and Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CompanyLogoBadge(
                        text = job?.companyLogoText ?: "J",
                        colorHex = job?.companyColorHex ?: 0xFF2563EB
                    )
                    Column {
                        Text(
                            text = job?.title ?: "Position Applied",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "${job?.companyName ?: "Company"} • ${job?.location ?: "Location"}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }

                ApplicationStatusBadge(status = application.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Step Bar (4 steps: Applied -> Under Review -> Interview -> Hired/Decision)
            ApplicationProgressStepper(status = application.status)

            Spacer(modifier = Modifier.height(12.dp))

            // Metadata row: Applied Date & Submitted CV
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Applied on ${application.appliedDate}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { isExpanded = !isExpanded }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isExpanded) "Hide Details" else "View Details",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BrandBluePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = BrandBluePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Expandable application details
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceSubtle)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = BrandBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Resume attached: ${application.cvFileName} (${application.cvFileSize})",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        )
                    }

                    if (application.coverLetter.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cover letter submitted:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = application.coverLetter,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        )
                    }

                    if (application.notes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Updates from hiring team:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = BrandBluePrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        application.notes.forEach { note ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• ${note.text} (${note.timestamp})",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "View original job posting →",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BrandBluePrimary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable { onViewJob() }
                    )
                }
            }
        }
    }
}

@Composable
fun ApplicationProgressStepper(status: ApplicationStatus) {
    val currentStep = when (status) {
        ApplicationStatus.APPLIED -> 1
        ApplicationStatus.UNDER_REVIEW -> 2
        ApplicationStatus.INTERVIEW -> 3
        ApplicationStatus.HIRED -> 4
        ApplicationStatus.REJECTED -> 1
    }

    val steps = listOf("Applied", "Review", "Interview", if (status == ApplicationStatus.REJECTED) "Closed" else "Hired")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, stepName ->
            val stepNumber = index + 1
            val isPassed = stepNumber <= currentStep
            val isCurrent = stepNumber == currentStep

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isPassed && status == ApplicationStatus.REJECTED && index == 3 -> Color(0xFFEF4444)
                                isPassed -> BrandBluePrimary
                                else -> BorderLight
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(TextMuted)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stepName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (isPassed) TextPrimary else TextMuted,
                        fontSize = 10.sp
                    )
                )
            }

            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .weight(1f)
                        .background(if (stepNumber < currentStep) BrandBluePrimary else BorderLight)
                )
            }
        }
    }
}

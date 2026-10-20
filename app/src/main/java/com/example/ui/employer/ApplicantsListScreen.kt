package com.example.ui.employer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ApplicationStatus
import com.example.model.Job
import com.example.model.JobApplication
import com.example.ui.components.ApplicationStatusBadge
import com.example.ui.components.FilterChipRow
import com.example.ui.theme.AppliedGreen
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandBlueDark
import com.example.ui.theme.BrandBluePrimary
import com.example.ui.theme.BrandBlueSubtle
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ApplicantsListScreen(
    applications: List<JobApplication>,
    jobs: List<Job>,
    selectedJobTitle: String?,
    onSelectJobTitle: (String?) -> Unit,
    selectedStatus: ApplicationStatus?,
    onSelectStatus: (ApplicationStatus?) -> Unit,
    sortNewestFirst: Boolean,
    onToggleSort: () -> Unit,
    onApplicantClick: (JobApplication) -> Unit,
    isDesktopLayout: Boolean = false,
    modifier: Modifier = Modifier
) {
    var jobDropdownExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("All Statuses") + ApplicationStatus.values().map { it.label }
    val currentStatusLabel = selectedStatus?.label ?: "All Statuses"

    val jobOptions = listOf("All Jobs") + jobs.map { it.title }
    val activeJobSelection = selectedJobTitle ?: "All Jobs"

    // Filter pipeline
    val filtered = applications
        .filter { app ->
            if (activeJobSelection == "All Jobs") true
            else {
                val matchingJob = jobs.find { it.id == app.jobId }
                matchingJob?.title == activeJobSelection
            }
        }
        .filter { app ->
            selectedStatus == null || app.status == selectedStatus
        }
        .sortedByDescending { if (sortNewestFirst) it.appliedTimestamp else -it.appliedTimestamp }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .testTag("applicants_list_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Controls Header (Job selector, Status chips, Sort toggle)
        item {
            Surface(
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Candidate Applications",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Filter by job posting and hiring stage",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Job selector dropdown trigger
                    Box {
                        Surface(
                            color = SurfaceSubtle,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { jobDropdownExpanded = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.FilterList, contentDescription = null, tint = BrandBluePrimary, modifier = Modifier.size(18.dp))
                                    Text(
                                        text = activeJobSelection,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                    )
                                }
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = TextMuted)
                            }
                        }

                        DropdownMenu(
                            expanded = jobDropdownExpanded,
                            onDismissRequest = { jobDropdownExpanded = false }
                        ) {
                            jobOptions.forEach { jobName ->
                                DropdownMenuItem(
                                    text = { Text(jobName) },
                                    onClick = {
                                        onSelectJobTitle(if (jobName == "All Jobs") null else jobName)
                                        jobDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status chips
                    FilterChipRow(
                        title = "Status",
                        options = statusOptions,
                        selectedOption = currentStatusLabel,
                        onSelect = { selected ->
                            val found = ApplicationStatus.values().find { it.label == selected }
                            onSelectStatus(found)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sort row & count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${filtered.size} Applicants found",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onToggleSort() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Sort, contentDescription = null, tint = BrandBluePrimary, modifier = Modifier.size(14.dp))
                            Text(
                                text = if (sortNewestFirst) "Sort: Newest First" else "Sort: Oldest First",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandBluePrimary
                                )
                            )
                        }
                    }
                }
            }
        }

        // Table / Card presentation
        if (filtered.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No applicants match this criteria", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Try resetting status or job filters.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                }
            }
        } else {
            items(filtered, key = { it.id }) { app ->
                val matchingJob = jobs.find { it.id == app.jobId }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                        .clickable { onApplicantClick(app) }
                        .testTag("applicant_row_${app.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
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
                                        text = "${app.applicantHeadline} • ${app.applicantLocation}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                        maxLines = 1
                                    )
                                }
                            }

                            ApplicationStatusBadge(status = app.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Role & CV Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceSubtle)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Role: ${matchingJob?.title ?: "Position"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                ),
                                maxLines = 1
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Applied ${app.appliedDate}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

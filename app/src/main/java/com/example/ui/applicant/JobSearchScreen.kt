package com.example.ui.applicant

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExperienceLevel
import com.example.model.Job
import com.example.model.JobType
import com.example.ui.components.CompanyLogoBadge
import com.example.ui.components.FilterChipRow
import com.example.ui.components.WorkFindSearchBar
import com.example.ui.theme.AppliedGreen
import com.example.ui.theme.AppliedGreenLight
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandBluePrimary
import com.example.ui.theme.BrandBlueSubtle
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun JobSearchScreen(
    jobs: List<Job>,
    savedJobIds: Set<String>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedLocation: String,
    onLocationChange: (String) -> Unit,
    selectedJobType: JobType?,
    onJobTypeChange: (JobType?) -> Unit,
    selectedExperience: ExperienceLevel?,
    onExperienceChange: (ExperienceLevel?) -> Unit,
    isJobApplied: (String) -> Boolean,
    onJobClick: (Job) -> Unit,
    onToggleSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val locations = listOf("All", "Remote", "San Francisco", "New York", "Austin", "Boston")
    val jobTypes = listOf("All Types") + JobType.values().map { it.label }
    val currentTypeLabel = selectedJobType?.label ?: "All Types"

    val expLevels = listOf("All Levels") + ExperienceLevel.values().map { it.label }
    val currentExpLabel = selectedExperience?.label ?: "All Levels"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .testTag("job_search_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            // Header Search & Filters Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, BorderLight)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                WorkFindSearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Location Filter Chips
                Text(
                    text = "Location",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                FilterChipRow(
                    title = "Location",
                    options = locations,
                    selectedOption = selectedLocation,
                    onSelect = onLocationChange
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Job Type & Experience filters in rows
                Text(
                    text = "Job Type",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                FilterChipRow(
                    title = "Type",
                    options = jobTypes,
                    selectedOption = currentTypeLabel,
                    onSelect = { selected ->
                        val found = JobType.values().find { it.label == selected }
                        onJobTypeChange(found)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Experience Level",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                FilterChipRow(
                    title = "Experience",
                    options = expLevels,
                    selectedOption = currentExpLabel,
                    onSelect = { selected ->
                        val found = ExperienceLevel.values().find { it.label == selected }
                        onExperienceChange(found)
                    }
                )
            }
        }

        item {
            // Results count banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${jobs.size} Open Positions",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                if (searchQuery.isNotEmpty() || selectedLocation != "All" || selectedJobType != null || selectedExperience != null) {
                    Text(
                        text = "Filtered",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BrandBluePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        if (jobs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No matching jobs found",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your search terms or filters.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                    }
                }
            }
        } else {
            items(jobs, key = { it.id }) { job ->
                val applied = isJobApplied(job.id)
                val saved = savedJobIds.contains(job.id)

                JobCardItem(
                    job = job,
                    isApplied = applied,
                    isSaved = saved,
                    onClick = { onJobClick(job) },
                    onToggleSave = { onToggleSave(job.id) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("job_card_${job.id}")
                )
            }
        }
    }
}

@Composable
fun JobCardItem(
    job: Job,
    isApplied: Boolean,
    isSaved: Boolean,
    onClick: () -> Unit,
    onToggleSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top Row: Company Logo, Title, Company Name, and Bookmark
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
                        text = job.companyLogoText,
                        colorHex = job.companyColorHex
                    )
                    Column {
                        Text(
                            text = job.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            ),
                            maxLines = 2
                        )
                        Text(
                            text = "${job.companyName} • ${job.department}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("save_job_${job.id}")
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save job",
                        tint = if (isSaved) BrandBluePrimary else TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chips Row: Location, Job Type, Experience
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Location chip
                Surface(
                    color = SurfaceSubtle,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (job.isRemote) "${job.location} (Remote)" else job.location,
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                    }
                }

                // Type chip
                Surface(
                    color = BrandBlueSubtle,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = job.jobType.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BrandBluePrimary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Experience chip
                Surface(
                    color = SurfaceSubtle,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = job.experienceLevel.label,
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Row: Salary Range, Posted Date, and Applied Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Salary Range
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = BrandBluePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = job.salaryRangeText,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrandBluePrimary
                        )
                    )
                }

                // Status or Date
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isApplied) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AppliedGreenLight)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = AppliedGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Applied",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = AppliedGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = job.postedDate,
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }
                }
            }
        }
    }
}

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EmployerAccount
import com.example.model.ExperienceLevel
import com.example.model.Job
import com.example.model.JobType
import com.example.ui.components.FilterChipRow
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandBluePrimary
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.UUID

@Composable
fun PostEditJobScreen(
    existingJob: Job? = null,
    employerAccount: EmployerAccount,
    onBack: () -> Unit,
    onSaveJob: (Job, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isEdit = existingJob != null

    var title by remember { mutableStateOf(existingJob?.title ?: "") }
    var department by remember { mutableStateOf(existingJob?.department ?: "Engineering") }
    var location by remember { mutableStateOf(existingJob?.location ?: "San Francisco, CA") }
    var isRemote by remember { mutableStateOf(existingJob?.isRemote ?: true) }
    var jobType by remember { mutableStateOf(existingJob?.jobType ?: JobType.FULL_TIME) }
    var experienceLevel by remember { mutableStateOf(existingJob?.experienceLevel ?: ExperienceLevel.MID) }
    var salaryMin by remember { mutableStateOf(existingJob?.salaryMin?.toString() ?: "140000") }
    var salaryMax by remember { mutableStateOf(existingJob?.salaryMax?.toString() ?: "180000") }
    var description by remember {
        mutableStateOf(
            existingJob?.description ?: "We are looking for a talented engineer to join our high-impact team. You will build user-facing products and collaborate closely with product design."
        )
    }

    val requirements = remember {
        mutableStateListOf<String>().apply {
            if (existingJob != null) {
                addAll(existingJob.requirements)
            } else {
                addAll(
                    listOf(
                        "3+ years of experience with relevant technologies",
                        "Experience building scalable and resilient production software",
                        "Excellent communication and problem-solving skills"
                    )
                )
            }
        }
    }

    var newRequirementText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("post_job_back_button")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Text(
                        text = if (isEdit) "Edit Job Posting" else "Post a New Job",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                errorMessage = "Please enter a job title"
                                return@Button
                            }
                            val minVal = salaryMin.toIntOrNull() ?: 100000
                            val maxVal = salaryMax.toIntOrNull() ?: 150000

                            val saved = Job(
                                id = existingJob?.id ?: "job_${UUID.randomUUID().toString().take(8)}",
                                title = title.trim(),
                                companyName = employerAccount.companyName,
                                companyLogoText = employerAccount.companyLogoText,
                                companyColorHex = employerAccount.companyColorHex,
                                location = location.trim(),
                                isRemote = isRemote,
                                jobType = jobType,
                                experienceLevel = experienceLevel,
                                salaryMin = minVal,
                                salaryMax = maxVal,
                                salaryPeriod = "yr",
                                postedDate = existingJob?.postedDate ?: "Today",
                                postedTimestamp = existingJob?.postedTimestamp ?: System.currentTimeMillis(),
                                department = department.trim(),
                                description = description.trim(),
                                requirements = requirements.toList(),
                                benefits = existingJob?.benefits ?: listOf("Health Insurance", "401(k) Matching", "Remote Setup", "Learning Budget"),
                                applicantCount = existingJob?.applicantCount ?: 0,
                                isActive = existingJob?.isActive ?: true,
                                employerId = employerAccount.id
                            )
                            onSaveJob(saved, isEdit)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBluePrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("publish_job_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(
                                text = if (isEdit) "Save Changes" else "Publish Job Posting",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundSlate)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (errorMessage != null) {
                item {
                    Surface(
                        color = Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = Color(0xFFDC2626),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            // Basic Info Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Job Details",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it; errorMessage = null },
                            label = { Text("Job Title *") },
                            placeholder = { Text("e.g. Senior Android Engineer") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("job_title_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = department,
                            onValueChange = { department = it },
                            label = { Text("Department / Team") },
                            placeholder = { Text("e.g. Mobile Engineering") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            placeholder = { Text("e.g. San Francisco, CA") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isRemote = !isRemote }
                        ) {
                            Checkbox(
                                checked = isRemote,
                                onCheckedChange = { isRemote = it },
                                colors = CheckboxDefaults.colors(checkedColor = BrandBluePrimary)
                            )
                            Text(
                                text = "Remote work allowed for this position",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                            )
                        }
                    }
                }
            }

            // Classification & Compensation Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Employment Type & Experience",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Type selector chips
                        Text("Job Type", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextMuted))
                        Spacer(modifier = Modifier.height(6.dp))
                        FilterChipRow(
                            title = "Type",
                            options = JobType.values().map { it.label },
                            selectedOption = jobType.label,
                            onSelect = { selected ->
                                jobType = JobType.values().find { it.label == selected } ?: JobType.FULL_TIME
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Experience level chips
                        Text("Experience Level", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextMuted))
                        Spacer(modifier = Modifier.height(6.dp))
                        FilterChipRow(
                            title = "Experience",
                            options = ExperienceLevel.values().map { it.label },
                            selectedOption = experienceLevel.label,
                            onSelect = { selected ->
                                experienceLevel = ExperienceLevel.values().find { it.label == selected } ?: ExperienceLevel.MID
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Annual Salary Range ($ USD)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = salaryMin,
                                onValueChange = { salaryMin = it },
                                label = { Text("Min Salary ($)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = salaryMax,
                                onValueChange = { salaryMax = it },
                                label = { Text("Max Salary ($)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Description Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Full Job Description",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("Describe the role, responsibilities, and team culture...") },
                            minLines = 5,
                            maxLines = 10,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("job_description_input")
                        )
                    }
                }
            }

            // Requirements List Builder Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Requirements (${requirements.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        requirements.forEachIndexed { index, req ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "• $req",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { requirements.removeAt(index) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", tint = TextMuted, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = newRequirementText,
                                onValueChange = { newRequirementText = it },
                                placeholder = { Text("Add a requirement...") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    if (newRequirementText.isNotBlank()) {
                                        requirements.add(newRequirementText.trim())
                                        newRequirementText = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandBluePrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Add")
                            }
                        }
                    }
                }
            }
        }
    }
}

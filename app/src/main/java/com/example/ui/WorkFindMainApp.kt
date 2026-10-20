package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.applicant.ApplyScreen
import com.example.ui.applicant.JobDetailScreen
import com.example.ui.applicant.JobSearchScreen
import com.example.ui.applicant.MyApplicationsScreen
import com.example.ui.applicant.ProfileScreen
import com.example.ui.components.WorkFindHeader
import com.example.ui.employer.ApplicantDetailScreen
import com.example.ui.employer.ApplicantsListScreen
import com.example.ui.employer.EmployerDashboardScreen
import com.example.ui.employer.EmployerLoginScreen
import com.example.ui.employer.MyJobPostingsScreen
import com.example.ui.employer.PostEditJobScreen
import com.example.ui.theme.AppliedGreen
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandBlueDark
import com.example.ui.theme.BrandBluePrimary
import com.example.ui.theme.BrandBlueSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WorkFindMainApp(
    viewModel: WorkFindViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val currentRole by viewModel.currentRole.collectAsState()
    val applicantTab by viewModel.applicantTab.collectAsState()
    val employerTab by viewModel.employerTab.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    val jobs by viewModel.jobs.collectAsState()
    val filteredJobs by viewModel.filteredJobs.collectAsState()
    val applications by viewModel.applications.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val employerAccount by viewModel.employerAccount.collectAsState()
    val savedJobIds by viewModel.savedJobIds.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val selectedJobType by viewModel.selectedJobType.collectAsState()
    val selectedExperience by viewModel.selectedExperience.collectAsState()

    val employerJobFilter by viewModel.employerApplicantJobFilter.collectAsState()
    val employerStatusFilter by viewModel.employerApplicantStatusFilter.collectAsState()
    val sortNewestFirst by viewModel.employerApplicantSortNewest.collectAsState()
    val simulateDesktop by viewModel.simulateDesktopLayout.collectAsState()

    // Handle Android system back button
    val canGoBack = currentScreen is Screen.JobDetail ||
        currentScreen is Screen.Apply ||
        currentScreen is Screen.EmployerApplicantDetail ||
        currentScreen is Screen.EmployerPostOrEditJob

    BackHandler(enabled = canGoBack) {
        viewModel.navigateBack()
    }

    val userAppCount = applications.count { it.applicantEmail == userProfile.email }
    val totalCandidateCount = applications.size

    val showBottomBar = currentScreen !is Screen.JobDetail &&
        currentScreen !is Screen.Apply &&
        currentScreen !is Screen.EmployerLogin &&
        currentScreen !is Screen.EmployerApplicantDetail &&
        currentScreen !is Screen.EmployerPostOrEditJob &&
        (!simulateDesktop || currentRole != UserRole.EMPLOYER)

    Scaffold(
        topBar = {
            WorkFindHeader(
                currentRole = currentRole,
                onRoleChange = { viewModel.setRole(it) },
                showDesktopToggle = currentRole == UserRole.EMPLOYER,
                isDesktopSimulated = simulateDesktop,
                onToggleDesktop = { viewModel.toggleSimulateDesktop() }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                Surface(
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    shadowElevation = 8.dp
                ) {
                    NavigationBar(
                        containerColor = Color.White,
                        contentColor = BrandBluePrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .testTag("main_navigation_bar")
                    ) {
                        if (currentRole == UserRole.APPLICANT) {
                            // 1. Search Tab
                            NavigationBarItem(
                                selected = applicantTab == ApplicantTab.SEARCH,
                                onClick = { viewModel.setApplicantTab(ApplicantTab.SEARCH) },
                                icon = {
                                    Icon(
                                        imageVector = if (applicantTab == ApplicantTab.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                                        contentDescription = "Search Jobs"
                                    )
                                },
                                label = { Text("Jobs", fontSize = 12.sp, fontWeight = if (applicantTab == ApplicantTab.SEARCH) FontWeight.Bold else FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandBluePrimary,
                                    selectedTextColor = BrandBluePrimary,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted,
                                    indicatorColor = BrandBlueSubtle
                                ),
                                modifier = Modifier.testTag("nav_jobs_tab")
                            )

                            // 2. My Applications Tab with badge
                            NavigationBarItem(
                                selected = applicantTab == ApplicantTab.APPLICATIONS,
                                onClick = { viewModel.setApplicantTab(ApplicantTab.APPLICATIONS) },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            if (userAppCount > 0) {
                                                Badge(
                                                    containerColor = AppliedGreen,
                                                    contentColor = Color.White
                                                ) {
                                                    Text("$userAppCount")
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (applicantTab == ApplicantTab.APPLICATIONS) Icons.Filled.Assignment else Icons.Outlined.Assignment,
                                            contentDescription = "My Applications"
                                        )
                                    }
                                },
                                label = { Text("Applications", fontSize = 12.sp, fontWeight = if (applicantTab == ApplicantTab.APPLICATIONS) FontWeight.Bold else FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandBluePrimary,
                                    selectedTextColor = BrandBluePrimary,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted,
                                    indicatorColor = BrandBlueSubtle
                                ),
                                modifier = Modifier.testTag("nav_applications_tab")
                            )

                            // 3. Profile Tab
                            NavigationBarItem(
                                selected = applicantTab == ApplicantTab.PROFILE,
                                onClick = { viewModel.setApplicantTab(ApplicantTab.PROFILE) },
                                icon = {
                                    Icon(
                                        imageVector = if (applicantTab == ApplicantTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                                        contentDescription = "Profile & CV"
                                    )
                                },
                                label = { Text("Profile", fontSize = 12.sp, fontWeight = if (applicantTab == ApplicantTab.PROFILE) FontWeight.Bold else FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandBluePrimary,
                                    selectedTextColor = BrandBluePrimary,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted,
                                    indicatorColor = BrandBlueSubtle
                                ),
                                modifier = Modifier.testTag("nav_profile_tab")
                            )
                        } else {
                            // Employer Bottom Navigation
                            NavigationBarItem(
                                selected = employerTab == EmployerTab.DASHBOARD,
                                onClick = { viewModel.setEmployerTab(EmployerTab.DASHBOARD) },
                                icon = {
                                    Icon(
                                        imageVector = if (employerTab == EmployerTab.DASHBOARD) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                                        contentDescription = "Dashboard"
                                    )
                                },
                                label = { Text("Dashboard", fontSize = 11.sp, fontWeight = if (employerTab == EmployerTab.DASHBOARD) FontWeight.Bold else FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandBluePrimary,
                                    selectedTextColor = BrandBluePrimary,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted,
                                    indicatorColor = BrandBlueSubtle
                                ),
                                modifier = Modifier.testTag("nav_employer_dashboard")
                            )

                            NavigationBarItem(
                                selected = employerTab == EmployerTab.POSTINGS,
                                onClick = { viewModel.setEmployerTab(EmployerTab.POSTINGS) },
                                icon = {
                                    Icon(
                                        imageVector = if (employerTab == EmployerTab.POSTINGS) Icons.Filled.ListAlt else Icons.Outlined.ListAlt,
                                        contentDescription = "Postings"
                                    )
                                },
                                label = { Text("Postings", fontSize = 11.sp, fontWeight = if (employerTab == EmployerTab.POSTINGS) FontWeight.Bold else FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandBluePrimary,
                                    selectedTextColor = BrandBluePrimary,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted,
                                    indicatorColor = BrandBlueSubtle
                                ),
                                modifier = Modifier.testTag("nav_employer_postings")
                            )

                            NavigationBarItem(
                                selected = employerTab == EmployerTab.POST_JOB,
                                onClick = { viewModel.setEmployerTab(EmployerTab.POST_JOB) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.AddCircle,
                                        contentDescription = "Post Job"
                                    )
                                },
                                label = { Text("Post Job", fontSize = 11.sp, fontWeight = if (employerTab == EmployerTab.POST_JOB) FontWeight.Bold else FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandBluePrimary,
                                    selectedTextColor = BrandBluePrimary,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted,
                                    indicatorColor = BrandBlueSubtle
                                ),
                                modifier = Modifier.testTag("nav_employer_post_job")
                            )

                            NavigationBarItem(
                                selected = employerTab == EmployerTab.APPLICANTS,
                                onClick = { viewModel.setEmployerTab(EmployerTab.APPLICANTS) },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            if (totalCandidateCount > 0) {
                                                Badge(
                                                    containerColor = BrandBluePrimary,
                                                    contentColor = Color.White
                                                ) {
                                                    Text("$totalCandidateCount")
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (employerTab == EmployerTab.APPLICANTS) Icons.Filled.Group else Icons.Outlined.Group,
                                            contentDescription = "Applicants"
                                        )
                                    }
                                },
                                label = { Text("Applicants", fontSize = 11.sp, fontWeight = if (employerTab == EmployerTab.APPLICANTS) FontWeight.Bold else FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandBluePrimary,
                                    selectedTextColor = BrandBluePrimary,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted,
                                    indicatorColor = BrandBlueSubtle
                                ),
                                modifier = Modifier.testTag("nav_employer_applicants")
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        // When Desktop simulated mode is active for Employer, show Desktop Layout with side Rail!
        if (simulateDesktop && currentRole == UserRole.EMPLOYER && employerAccount != null) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavigationRail(
                    containerColor = Color.White,
                    contentColor = BrandBluePrimary,
                    modifier = Modifier
                        .fillMaxHeight()
                        .border(1.dp, BorderLight)
                ) {
                    NavigationRailItem(
                        selected = employerTab == EmployerTab.DASHBOARD,
                        onClick = { viewModel.setEmployerTab(EmployerTab.DASHBOARD) },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                        label = { Text("Dashboard", fontSize = 11.sp) }
                    )
                    NavigationRailItem(
                        selected = employerTab == EmployerTab.POSTINGS,
                        onClick = { viewModel.setEmployerTab(EmployerTab.POSTINGS) },
                        icon = { Icon(Icons.Default.ListAlt, contentDescription = "Postings") },
                        label = { Text("Postings", fontSize = 11.sp) }
                    )
                    NavigationRailItem(
                        selected = employerTab == EmployerTab.POST_JOB,
                        onClick = { viewModel.setEmployerTab(EmployerTab.POST_JOB) },
                        icon = { Icon(Icons.Default.AddCircle, contentDescription = "Post Job") },
                        label = { Text("Post Job", fontSize = 11.sp) }
                    )
                    NavigationRailItem(
                        selected = employerTab == EmployerTab.APPLICANTS,
                        onClick = { viewModel.setEmployerTab(EmployerTab.APPLICANTS) },
                        icon = { Icon(Icons.Default.Group, contentDescription = "Applicants") },
                        label = { Text("Applicants", fontSize = 11.sp) }
                    )
                }

                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    RenderScreenContent(
                        currentScreen = currentScreen,
                        viewModel = viewModel,
                        jobs = jobs,
                        filteredJobs = filteredJobs,
                        applications = applications,
                        userProfile = userProfile,
                        employerAccount = employerAccount,
                        savedJobIds = savedJobIds,
                        searchQuery = searchQuery,
                        selectedLocation = selectedLocation,
                        selectedJobType = selectedJobType,
                        selectedExperience = selectedExperience,
                        employerJobFilter = employerJobFilter,
                        employerStatusFilter = employerStatusFilter,
                        sortNewestFirst = sortNewestFirst,
                        isDesktopLayout = true
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                RenderScreenContent(
                    currentScreen = currentScreen,
                    viewModel = viewModel,
                    jobs = jobs,
                    filteredJobs = filteredJobs,
                    applications = applications,
                    userProfile = userProfile,
                    employerAccount = employerAccount,
                    savedJobIds = savedJobIds,
                    searchQuery = searchQuery,
                    selectedLocation = selectedLocation,
                    selectedJobType = selectedJobType,
                    selectedExperience = selectedExperience,
                    employerJobFilter = employerJobFilter,
                    employerStatusFilter = employerStatusFilter,
                    sortNewestFirst = sortNewestFirst,
                    isDesktopLayout = false
                )
            }
        }
    }
}

@Composable
private fun RenderScreenContent(
    currentScreen: Screen,
    viewModel: WorkFindViewModel,
    jobs: List<com.example.model.Job>,
    filteredJobs: List<com.example.model.Job>,
    applications: List<com.example.model.JobApplication>,
    userProfile: com.example.model.UserProfile,
    employerAccount: com.example.model.EmployerAccount?,
    savedJobIds: Set<String>,
    searchQuery: String,
    selectedLocation: String,
    selectedJobType: com.example.model.JobType?,
    selectedExperience: com.example.model.ExperienceLevel?,
    employerJobFilter: String?,
    employerStatusFilter: com.example.model.ApplicationStatus?,
    sortNewestFirst: Boolean,
    isDesktopLayout: Boolean
) {
    when (currentScreen) {
        // 1. Home / Job Search
        is Screen.ApplicantHome -> {
            JobSearchScreen(
                jobs = filteredJobs,
                savedJobIds = savedJobIds,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                selectedLocation = selectedLocation,
                onLocationChange = { viewModel.setSelectedLocation(it) },
                selectedJobType = selectedJobType,
                onJobTypeChange = { viewModel.setSelectedJobType(it) },
                selectedExperience = selectedExperience,
                onExperienceChange = { viewModel.setSelectedExperience(it) },
                isJobApplied = { viewModel.isJobApplied(it) },
                onJobClick = { job -> viewModel.navigateTo(Screen.JobDetail(job.id)) },
                onToggleSave = { jobId -> viewModel.toggleSaveJob(jobId) }
            )
        }

        // 2. Job Detail
        is Screen.JobDetail -> {
            val job = jobs.find { it.id == currentScreen.jobId } ?: jobs.first()
            val isApplied = viewModel.isJobApplied(job.id)
            val isSaved = savedJobIds.contains(job.id)

            JobDetailScreen(
                job = job,
                isApplied = isApplied,
                isSaved = isSaved,
                onBack = { viewModel.navigateBack() },
                onToggleSave = { viewModel.toggleSaveJob(job.id) },
                onApplyNow = { viewModel.navigateTo(Screen.Apply(job.id)) },
                onViewApplication = {
                    viewModel.setApplicantTab(ApplicantTab.APPLICATIONS)
                }
            )
        }

        // 3. Apply
        is Screen.Apply -> {
            val job = jobs.find { it.id == currentScreen.jobId } ?: jobs.first()
            ApplyScreen(
                job = job,
                userProfile = userProfile,
                onBack = { viewModel.navigateBack() },
                onSubmit = { coverLetter, cvFileName, cvFileSize ->
                    viewModel.submitApplication(job.id, coverLetter, cvFileName, cvFileSize)
                }
            )
        }

        // 4. My Applications
        is Screen.MyApplications -> {
            MyApplicationsScreen(
                applications = applications,
                jobs = jobs,
                userEmail = userProfile.email,
                onJobClick = { job -> viewModel.navigateTo(Screen.JobDetail(job.id)) },
                onExploreJobsClick = { viewModel.setApplicantTab(ApplicantTab.SEARCH) }
            )
        }

        // 5. Profile/CV
        is Screen.ApplicantProfile -> {
            ProfileScreen(
                userProfile = userProfile,
                onUpdateProfile = { updated -> viewModel.updateUserProfile(updated) }
            )
        }

        // 6. Employer Login
        is Screen.EmployerLogin -> {
            EmployerLoginScreen(
                onLoginSuccess = { account -> viewModel.loginEmployer(account) },
                onSwitchToApplicant = { viewModel.setRole(UserRole.APPLICANT) }
            )
        }

        // 7. Employer Dashboard
        is Screen.EmployerDashboard -> {
            val activeAccount = employerAccount ?: com.example.data.SampleData.initialEmployer
            EmployerDashboardScreen(
                employerAccount = activeAccount,
                jobs = jobs,
                applications = applications,
                onPostNewJobClick = { viewModel.navigateTo(Screen.EmployerPostOrEditJob()) },
                onViewAllPostingsClick = { viewModel.setEmployerTab(EmployerTab.POSTINGS) },
                onViewAllApplicantsClick = { viewModel.setEmployerTab(EmployerTab.APPLICANTS) },
                onApplicantClick = { app -> viewModel.navigateTo(Screen.EmployerApplicantDetail(app.id)) },
                isDesktopLayout = isDesktopLayout
            )
        }

        // 8. My Job Postings
        is Screen.EmployerJobPostings -> {
            MyJobPostingsScreen(
                jobs = jobs,
                applications = applications,
                onPostNewJob = { viewModel.navigateTo(Screen.EmployerPostOrEditJob()) },
                onEditJob = { job -> viewModel.navigateTo(Screen.EmployerPostOrEditJob(job.id)) },
                onToggleActive = { jobId -> viewModel.toggleJobActive(jobId) },
                onViewApplicantsForJob = { job ->
                    viewModel.setEmployerJobFilter(job.title)
                    viewModel.navigateTo(Screen.EmployerApplicantsList(job.id))
                }
            )
        }

        // 9. Post / Edit Job
        is Screen.EmployerPostOrEditJob -> {
            val activeAccount = employerAccount ?: com.example.data.SampleData.initialEmployer
            val existingJob = currentScreen.jobId?.let { jid -> jobs.find { it.id == jid } }
            PostEditJobScreen(
                existingJob = existingJob,
                employerAccount = activeAccount,
                onBack = { viewModel.navigateBack() },
                onSaveJob = { job, isEdit -> viewModel.postOrUpdateJob(job, isEdit) }
            )
        }

        // 10. Applicants List
        is Screen.EmployerApplicantsList -> {
            ApplicantsListScreen(
                applications = applications,
                jobs = jobs,
                selectedJobTitle = employerJobFilter,
                onSelectJobTitle = { viewModel.setEmployerJobFilter(it) },
                selectedStatus = employerStatusFilter,
                onSelectStatus = { viewModel.setEmployerStatusFilter(it) },
                sortNewestFirst = sortNewestFirst,
                onToggleSort = { viewModel.toggleApplicantSort() },
                onApplicantClick = { app -> viewModel.navigateTo(Screen.EmployerApplicantDetail(app.id)) },
                isDesktopLayout = isDesktopLayout
            )
        }

        // 11. Applicant Detail
        is Screen.EmployerApplicantDetail -> {
            val app = applications.find { it.id == currentScreen.applicationId } ?: applications.first()
            val job = jobs.find { it.id == app.jobId }
            ApplicantDetailScreen(
                application = app,
                job = job,
                onBack = { viewModel.navigateBack() },
                onStatusChange = { newStatus -> viewModel.updateApplicationStatus(app.id, newStatus) },
                onAddNote = { note -> viewModel.addEmployerNote(app.id, note) }
            )
        }
    }
}

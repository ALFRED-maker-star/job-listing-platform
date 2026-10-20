package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.data.WorkFindRepository
import com.example.model.ApplicationStatus
import com.example.model.EmployerAccount
import com.example.model.ExperienceLevel
import com.example.model.Job
import com.example.model.JobApplication
import com.example.model.JobType
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class UserRole {
    APPLICANT,
    EMPLOYER
}

enum class ApplicantTab {
    SEARCH,
    APPLICATIONS,
    PROFILE
}

enum class EmployerTab {
    DASHBOARD,
    POSTINGS,
    POST_JOB,
    APPLICANTS
}

sealed class Screen {
    data object ApplicantHome : Screen()
    data class JobDetail(val jobId: String) : Screen()
    data class Apply(val jobId: String) : Screen()
    data object MyApplications : Screen()
    data object ApplicantProfile : Screen()

    data object EmployerLogin : Screen()
    data object EmployerDashboard : Screen()
    data object EmployerJobPostings : Screen()
    data class EmployerPostOrEditJob(val jobId: String? = null) : Screen()
    data class EmployerApplicantsList(val filterJobId: String? = null) : Screen()
    data class EmployerApplicantDetail(val applicationId: String) : Screen()
}

class WorkFindViewModel(
    private val repository: WorkFindRepository = WorkFindRepository()
) : ViewModel() {

    val jobs: StateFlow<List<Job>> = repository.jobs
    val applications: StateFlow<List<JobApplication>> = repository.applications
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val employerAccount: StateFlow<EmployerAccount?> = repository.employerAccount
    val savedJobIds: StateFlow<Set<String>> = repository.savedJobIds

    private val _currentRole = MutableStateFlow(UserRole.APPLICANT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _applicantTab = MutableStateFlow(ApplicantTab.SEARCH)
    val applicantTab: StateFlow<ApplicantTab> = _applicantTab.asStateFlow()

    private val _employerTab = MutableStateFlow(EmployerTab.DASHBOARD)
    val employerTab: StateFlow<EmployerTab> = _employerTab.asStateFlow()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.ApplicantHome)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Search and Filters for Job Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLocation = MutableStateFlow("All")
    val selectedLocation: StateFlow<String> = _selectedLocation.asStateFlow()

    private val _selectedJobType = MutableStateFlow<JobType?>(null)
    val selectedJobType: StateFlow<JobType?> = _selectedJobType.asStateFlow()

    private val _selectedExperience = MutableStateFlow<ExperienceLevel?>(null)
    val selectedExperience: StateFlow<ExperienceLevel?> = _selectedExperience.asStateFlow()

    // Applicants List filter in Employer mode
    private val _employerApplicantJobFilter = MutableStateFlow<String?>("All Jobs")
    val employerApplicantJobFilter: StateFlow<String?> = _employerApplicantJobFilter.asStateFlow()

    private val _employerApplicantStatusFilter = MutableStateFlow<ApplicationStatus?>(null)
    val employerApplicantStatusFilter: StateFlow<ApplicationStatus?> = _employerApplicantStatusFilter.asStateFlow()

    private val _employerApplicantSortNewest = MutableStateFlow(true)
    val employerApplicantSortNewest: StateFlow<Boolean> = _employerApplicantSortNewest.asStateFlow()

    // Desktop mode toggle for testing employer dashboard in desktop layout
    private val _simulateDesktopLayout = MutableStateFlow(false)
    val simulateDesktopLayout: StateFlow<Boolean> = _simulateDesktopLayout.asStateFlow()

    // Filtered jobs
    val filteredJobs: StateFlow<List<Job>> = combine(
        jobs,
        _searchQuery,
        _selectedLocation,
        _selectedJobType,
        _selectedExperience
    ) { jobList, query, location, type, exp ->
        jobList.filter { job ->
            val matchesQuery = query.isBlank() ||
                job.title.contains(query, ignoreCase = true) ||
                job.companyName.contains(query, ignoreCase = true) ||
                job.department.contains(query, ignoreCase = true) ||
                job.requirements.any { it.contains(query, ignoreCase = true) }

            val matchesLocation = location == "All" ||
                (location == "Remote" && job.isRemote) ||
                job.location.contains(location, ignoreCase = true)

            val matchesType = type == null || job.jobType == type
            val matchesExp = exp == null || job.experienceLevel == exp

            matchesQuery && matchesLocation && matchesType && matchesExp
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setRole(role: UserRole) {
        _currentRole.value = role
        if (role == UserRole.APPLICANT) {
            _currentScreen.value = when (_applicantTab.value) {
                ApplicantTab.SEARCH -> Screen.ApplicantHome
                ApplicantTab.APPLICATIONS -> Screen.MyApplications
                ApplicantTab.PROFILE -> Screen.ApplicantProfile
            }
        } else {
            if (employerAccount.value == null) {
                _currentScreen.value = Screen.EmployerLogin
            } else {
                _currentScreen.value = when (_employerTab.value) {
                    EmployerTab.DASHBOARD -> Screen.EmployerDashboard
                    EmployerTab.POSTINGS -> Screen.EmployerJobPostings
                    EmployerTab.POST_JOB -> Screen.EmployerPostOrEditJob()
                    EmployerTab.APPLICANTS -> Screen.EmployerApplicantsList()
                }
            }
        }
    }

    fun setApplicantTab(tab: ApplicantTab) {
        _applicantTab.value = tab
        _currentScreen.value = when (tab) {
            ApplicantTab.SEARCH -> Screen.ApplicantHome
            ApplicantTab.APPLICATIONS -> Screen.MyApplications
            ApplicantTab.PROFILE -> Screen.ApplicantProfile
        }
    }

    fun setEmployerTab(tab: EmployerTab) {
        _employerTab.value = tab
        if (employerAccount.value == null) {
            _currentScreen.value = Screen.EmployerLogin
            return
        }
        _currentScreen.value = when (tab) {
            EmployerTab.DASHBOARD -> Screen.EmployerDashboard
            EmployerTab.POSTINGS -> Screen.EmployerJobPostings
            EmployerTab.POST_JOB -> Screen.EmployerPostOrEditJob()
            EmployerTab.APPLICANTS -> Screen.EmployerApplicantsList()
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun navigateBack() {
        when (val current = _currentScreen.value) {
            is Screen.JobDetail -> _currentScreen.value = Screen.ApplicantHome
            is Screen.Apply -> _currentScreen.value = Screen.JobDetail(current.jobId)
            is Screen.EmployerApplicantDetail -> _currentScreen.value = Screen.EmployerApplicantsList()
            is Screen.EmployerPostOrEditJob -> _currentScreen.value = Screen.EmployerJobPostings
            else -> {
                if (_currentRole.value == UserRole.EMPLOYER) {
                    _currentScreen.value = Screen.EmployerDashboard
                } else {
                    _currentScreen.value = Screen.ApplicantHome
                }
            }
        }
    }

    // Filters
    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setSelectedLocation(location: String) { _selectedLocation.value = location }
    fun setSelectedJobType(type: JobType?) { _selectedJobType.value = type }
    fun setSelectedExperience(exp: ExperienceLevel?) { _selectedExperience.value = exp }

    fun setEmployerJobFilter(jobTitle: String?) { _employerApplicantJobFilter.value = jobTitle }
    fun setEmployerStatusFilter(status: ApplicationStatus?) { _employerApplicantStatusFilter.value = status }
    fun toggleApplicantSort() { _employerApplicantSortNewest.value = !_employerApplicantSortNewest.value }
    fun toggleSimulateDesktop() { _simulateDesktopLayout.value = !_simulateDesktopLayout.value }

    fun isJobApplied(jobId: String): Boolean = repository.isJobApplied(jobId)

    fun submitApplication(jobId: String, coverLetter: String, cvFileName: String, cvFileSize: String) {
        repository.submitApplication(jobId, coverLetter, cvFileName, cvFileSize)
        _currentScreen.value = Screen.MyApplications
        _applicantTab.value = ApplicantTab.APPLICATIONS
    }

    fun updateApplicationStatus(applicationId: String, status: ApplicationStatus) {
        repository.updateApplicationStatus(applicationId, status)
    }

    fun addEmployerNote(applicationId: String, note: String) {
        val author = employerAccount.value?.name ?: "Hiring Lead"
        repository.addEmployerNote(applicationId, note, author)
    }

    fun postOrUpdateJob(job: Job, isEdit: Boolean) {
        if (isEdit) {
            repository.updateJob(job)
        } else {
            repository.postJob(job)
        }
        _currentScreen.value = Screen.EmployerJobPostings
        _employerTab.value = EmployerTab.POSTINGS
    }

    fun toggleJobActive(jobId: String) {
        repository.toggleJobActive(jobId)
    }

    fun toggleSaveJob(jobId: String) {
        repository.toggleSaveJob(jobId)
    }

    fun updateUserProfile(profile: UserProfile) {
        repository.updateProfile(profile)
    }

    fun loginEmployer(account: EmployerAccount = SampleData.initialEmployer) {
        repository.loginEmployer(account)
        _currentScreen.value = Screen.EmployerDashboard
        _employerTab.value = EmployerTab.DASHBOARD
    }

    fun logoutEmployer() {
        repository.logoutEmployer()
        _currentScreen.value = Screen.EmployerLogin
    }
}

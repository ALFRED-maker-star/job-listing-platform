package com.example.data

import com.example.model.ApplicationStatus
import com.example.model.EmployerAccount
import com.example.model.EmployerNote
import com.example.model.Job
import com.example.model.JobApplication
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class WorkFindRepository {
    private val _jobs = MutableStateFlow<List<Job>>(SampleData.initialJobs)
    val jobs: StateFlow<List<Job>> = _jobs.asStateFlow()

    private val _applications = MutableStateFlow<List<JobApplication>>(SampleData.initialApplications)
    val applications: StateFlow<List<JobApplication>> = _applications.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile>(SampleData.initialUserProfile)
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _employerAccount = MutableStateFlow<EmployerAccount?>(SampleData.initialEmployer)
    val employerAccount: StateFlow<EmployerAccount?> = _employerAccount.asStateFlow()

    private val _savedJobIds = MutableStateFlow<Set<String>>(setOf("job_1", "job_3"))
    val savedJobIds: StateFlow<Set<String>> = _savedJobIds.asStateFlow()

    fun isJobApplied(jobId: String): Boolean {
        return _applications.value.any { it.jobId == jobId && it.applicantEmail == _userProfile.value.email }
    }

    fun getApplicationForJob(jobId: String): JobApplication? {
        return _applications.value.find { it.jobId == jobId && it.applicantEmail == _userProfile.value.email }
    }

    fun submitApplication(
        jobId: String,
        coverLetter: String,
        cvFileName: String,
        cvFileSize: String
    ): JobApplication {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
        val formattedDate = dateFormat.format(Date())
        val profile = _userProfile.value

        val newApp = JobApplication(
            id = "app_${UUID.randomUUID().toString().take(8)}",
            jobId = jobId,
            applicantName = profile.name,
            applicantEmail = profile.email,
            applicantPhone = profile.phone,
            applicantHeadline = profile.headline,
            applicantLocation = profile.location,
            cvFileName = cvFileName,
            cvFileSize = cvFileSize,
            coverLetter = coverLetter,
            appliedDate = formattedDate,
            appliedTimestamp = System.currentTimeMillis(),
            status = ApplicationStatus.APPLIED,
            skills = profile.skills,
            experienceYears = 6,
            matchScore = 95
        )

        _applications.update { listOf(newApp) + it }
        _jobs.update { list ->
            list.map { job ->
                if (job.id == jobId) job.copy(applicantCount = job.applicantCount + 1) else job
            }
        }
        return newApp
    }

    fun updateApplicationStatus(applicationId: String, newStatus: ApplicationStatus) {
        _applications.update { list ->
            list.map { app ->
                if (app.id == applicationId) app.copy(status = newStatus) else app
            }
        }
    }

    fun addEmployerNote(applicationId: String, noteText: String, author: String = "Sarah Chen") {
        val timeFormat = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.US)
        val newNote = EmployerNote(
            id = "note_${UUID.randomUUID().toString().take(6)}",
            author = author,
            text = noteText,
            timestamp = timeFormat.format(Date())
        )
        _applications.update { list ->
            list.map { app ->
                if (app.id == applicationId) {
                    app.copy(notes = app.notes + newNote)
                } else app
            }
        }
    }

    fun postJob(job: Job) {
        _jobs.update { listOf(job) + it }
    }

    fun updateJob(updatedJob: Job) {
        _jobs.update { list ->
            list.map { if (it.id == updatedJob.id) updatedJob else it }
        }
    }

    fun toggleJobActive(jobId: String) {
        _jobs.update { list ->
            list.map {
                if (it.id == jobId) it.copy(isActive = !it.isActive) else it
            }
        }
    }

    fun toggleSaveJob(jobId: String) {
        _savedJobIds.update { set ->
            if (set.contains(jobId)) set - jobId else set + jobId
        }
    }

    fun updateProfile(profile: UserProfile) {
        _userProfile.value = profile
    }

    fun loginEmployer(account: EmployerAccount) {
        _employerAccount.value = account
    }

    fun logoutEmployer() {
        _employerAccount.value = null
    }
}

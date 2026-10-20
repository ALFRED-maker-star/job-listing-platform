package com.example.model

enum class JobType(val label: String) {
    FULL_TIME("Full-time"),
    PART_TIME("Part-time"),
    CONTRACT("Contract"),
    INTERNSHIP("Internship")
}

enum class ExperienceLevel(val label: String) {
    ENTRY("Entry Level"),
    MID("Mid-Level"),
    SENIOR("Senior"),
    LEAD("Lead / Principal")
}

enum class ApplicationStatus(val label: String) {
    APPLIED("Applied"),
    UNDER_REVIEW("Under Review"),
    INTERVIEW("Interview"),
    REJECTED("Rejected"),
    HIRED("Hired")
}

data class EmployerNote(
    val id: String,
    val author: String,
    val text: String,
    val timestamp: String
)

data class Job(
    val id: String,
    val title: String,
    val companyName: String,
    val companyLogoText: String,
    val companyColorHex: Long,
    val location: String,
    val isRemote: Boolean = false,
    val jobType: JobType = JobType.FULL_TIME,
    val experienceLevel: ExperienceLevel = ExperienceLevel.MID,
    val salaryMin: Int,
    val salaryMax: Int,
    val salaryPeriod: String = "yr",
    val postedDate: String,
    val postedTimestamp: Long,
    val department: String,
    val description: String,
    val requirements: List<String>,
    val benefits: List<String>,
    val applicantCount: Int = 0,
    val isActive: Boolean = true,
    val employerId: String = "emp_1"
) {
    val salaryRangeText: String
        get() = "$${salaryMin / 1000}k - $${salaryMax / 1000}k / $salaryPeriod"
}

data class JobApplication(
    val id: String,
    val jobId: String,
    val applicantName: String,
    val applicantEmail: String,
    val applicantPhone: String,
    val applicantHeadline: String,
    val applicantLocation: String,
    val cvFileName: String,
    val cvFileSize: String,
    val coverLetter: String,
    val appliedDate: String,
    val appliedTimestamp: Long,
    val status: ApplicationStatus = ApplicationStatus.APPLIED,
    val notes: List<EmployerNote> = emptyList(),
    val skills: List<String> = emptyList(),
    val experienceYears: Int = 4,
    val matchScore: Int = 92
)

data class WorkExperience(
    val id: String,
    val role: String,
    val company: String,
    val duration: String,
    val description: String
)

data class Education(
    val id: String,
    val degree: String,
    val institution: String,
    val year: String
)

data class UserProfile(
    val name: String,
    val headline: String,
    val email: String,
    val phone: String,
    val location: String,
    val bio: String,
    val cvFileName: String,
    val cvFileSize: String,
    val cvLastUpdated: String,
    val experiences: List<WorkExperience>,
    val education: List<Education>,
    val skills: List<String>
)

data class EmployerAccount(
    val id: String,
    val name: String,
    val email: String,
    val companyName: String,
    val companyLogoText: String,
    val companyColorHex: Long,
    val role: String,
    val industry: String,
    val companySize: String,
    val location: String
)

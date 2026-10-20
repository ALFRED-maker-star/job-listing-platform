package com.example.data

import com.example.model.ApplicationStatus
import com.example.model.Education
import com.example.model.EmployerAccount
import com.example.model.EmployerNote
import com.example.model.ExperienceLevel
import com.example.model.Job
import com.example.model.JobApplication
import com.example.model.JobType
import com.example.model.UserProfile
import com.example.model.WorkExperience

object SampleData {
    val initialEmployer = EmployerAccount(
        id = "emp_stripe",
        name = "Sarah Chen",
        email = "sarah.chen@stripe.com",
        companyName = "Stripe",
        companyLogoText = "S",
        companyColorHex = 0xFF6366F1,
        role = "Lead Talent Partner",
        industry = "FinTech & Payments",
        companySize = "8,000+ employees",
        location = "San Francisco, CA"
    )

    val initialUserProfile = UserProfile(
        name = "Alex Morgan",
        headline = "Senior Mobile Engineer • Kotlin & Compose Specialist",
        email = "alex.morgan@email.com",
        phone = "+1 (555) 234-5678",
        location = "San Francisco, CA (Open to Remote)",
        bio = "Passionate Android engineer with 6+ years designing scalable, fluid, and battery-efficient apps. Experienced in Jetpack Compose, Clean Architecture, Kotlin Coroutines, and modern design systems.",
        cvFileName = "Alex_Morgan_Senior_Android_Resume_2026.pdf",
        cvFileSize = "2.4 MB",
        cvLastUpdated = "Oct 12, 2026",
        experiences = listOf(
            WorkExperience(
                id = "exp_1",
                role = "Senior Android Engineer",
                company = "ScaleGrid Technologies",
                duration = "2023 - Present (3 yrs)",
                description = "Led migration of core app to 100% Jetpack Compose. Reduced app cold start time by 32% and increased crash-free sessions to 99.9% across 2M active users."
            ),
            WorkExperience(
                id = "exp_2",
                role = "Mobile Software Engineer",
                company = "Finova Labs",
                duration = "2020 - 2023 (3 yrs)",
                description = "Built offline-first financial dashboard with Room database and Coroutines Flow. Implemented biometric auth and tokenized payments."
            )
        ),
        education = listOf(
            Education(
                id = "edu_1",
                degree = "B.S. in Computer Science",
                institution = "University of California, Berkeley",
                year = "2016 - 2020"
            )
        ),
        skills = listOf(
            "Kotlin",
            "Jetpack Compose",
            "Coroutines & Flow",
            "M3 Design Systems",
            "Clean Architecture",
            "Room DB",
            "CI/CD Pipelines",
            "REST & GraphQL APIs",
            "Robolectric Testing"
        )
    )

    val initialJobs = listOf(
        Job(
            id = "job_1",
            title = "Senior Android Engineer",
            companyName = "Stripe",
            companyLogoText = "S",
            companyColorHex = 0xFF6366F1,
            location = "San Francisco, CA",
            isRemote = true,
            jobType = JobType.FULL_TIME,
            experienceLevel = ExperienceLevel.SENIOR,
            salaryMin = 185000,
            salaryMax = 230000,
            salaryPeriod = "yr",
            postedDate = "2 days ago",
            postedTimestamp = System.currentTimeMillis() - 2 * 86400000L,
            department = "Mobile Platform",
            description = "At Stripe, mobile engineers build the checkout and developer dashboard experiences that power global internet commerce. You will architect robust, modular Android SDKs and modern Compose interfaces that thousands of companies rely on daily.",
            requirements = listOf(
                "5+ years of software engineering with 3+ years dedicated to modern Android (Kotlin)",
                "Expertise with Jetpack Compose, state handling, and coroutines",
                "Deep understanding of Android SDK performance, memory tuning, and network resilience",
                "Experience shipping production apps used by millions of users",
                "Strong collaboration with product managers, UX researchers, and backend engineers"
            ),
            benefits = listOf(
                "Competitive base + equity package",
                "Comprehensive healthcare & wellness coverage",
                "Remote-first flexible work setup with $1,500 home office stipend",
                "Annual learning & conference allowance"
            ),
            applicantCount = 28,
            isActive = true,
            employerId = "emp_stripe"
        ),
        Job(
            id = "job_2",
            title = "Lead Product Designer",
            companyName = "Figma",
            companyLogoText = "F",
            companyColorHex = 0xFFEC4899,
            location = "New York, NY",
            isRemote = true,
            jobType = JobType.FULL_TIME,
            experienceLevel = ExperienceLevel.LEAD,
            salaryMin = 190000,
            salaryMax = 245000,
            salaryPeriod = "yr",
            postedDate = "3 days ago",
            postedTimestamp = System.currentTimeMillis() - 3 * 86400000L,
            department = "Core Product",
            description = "Figma is revolutionizing how teams design and build digital products. We are seeking a Lead Product Designer to guide our design systems tooling, developer handoff experience, and canvas performance aesthetics.",
            requirements = listOf(
                "7+ years of product design experience in high-growth SaaS tools",
                "Mastery of prototyping, component architectures, and systems thinking",
                "Proven track record mentoring senior designers and driving product vision",
                "Strong portfolio showcasing complex workflows made intuitive"
            ),
            benefits = listOf(
                "Generous equity and 401(k) matching",
                "Unlimited paid time off (minimum 4 weeks encouraged)",
                "Full medical, dental, and vision premium coverage",
                "Twice-yearly global company offsites"
            ),
            applicantCount = 42,
            isActive = true,
            employerId = "emp_stripe"
        ),
        Job(
            id = "job_3",
            title = "Staff Backend Engineer (Distributed Systems)",
            companyName = "Linear",
            companyLogoText = "L",
            companyColorHex = 0xFF5E6AD2,
            location = "San Francisco, CA",
            isRemote = true,
            jobType = JobType.FULL_TIME,
            experienceLevel = ExperienceLevel.SENIOR,
            salaryMin = 200000,
            salaryMax = 260000,
            salaryPeriod = "yr",
            postedDate = "5 days ago",
            postedTimestamp = System.currentTimeMillis() - 5 * 86400000L,
            department = "Core Infrastructure",
            description = "Linear is the issue tracking tool software teams actually love. We obsess over milliseconds of latency and instantaneous real-time sync. You will scale our real-time synchronization engine and low-latency database layer.",
            requirements = listOf(
                "6+ years building high-throughput distributed systems",
                "Mastery of PostgreSQL, conflict-free replicated data types (CRDTs), or sync protocols",
                "Obsessive focus on software speed, developer craft, and simplicity",
                "B.S./M.S. in Computer Science or equivalent practical experience"
            ),
            benefits = listOf(
                "Work directly with founders and world-class engineers",
                "High equity grant and top-tier base salary",
                "Top-spec MacBook Pro and gear of your choice",
                "Flexible async work culture with minimal meetings"
            ),
            applicantCount = 19,
            isActive = true,
            employerId = "emp_linear"
        ),
        Job(
            id = "job_4",
            title = "Mobile Growth Engineer (Kotlin / Multiplatform)",
            companyName = "Notion",
            companyLogoText = "N",
            companyColorHex = 0xFF1E293B,
            location = "New York, NY",
            isRemote = false,
            jobType = JobType.FULL_TIME,
            experienceLevel = ExperienceLevel.MID,
            salaryMin = 150000,
            salaryMax = 185000,
            salaryPeriod = "yr",
            postedDate = "1 week ago",
            postedTimestamp = System.currentTimeMillis() - 7 * 86400000L,
            department = "Growth & Monetization",
            description = "Help millions of knowledge workers organize their work and life. On the Growth team, you will experiment rapidly with onboarding flows, viral sharing hooks, and AI-assisted workspace activation across mobile.",
            requirements = listOf(
                "3+ years of native Android software development",
                "A/B testing and telemetry experiment implementation experience",
                "High empathy for user journey friction and typography aesthetics",
                "Solid foundation in automated testing and CI/CD"
            ),
            benefits = listOf(
                "Comprehensive health and wellness benefits",
                "Daily catered lunches in modern downtown office",
                "Commuter transit subsidy and wellness stipend",
                "Generous parental leave policies"
            ),
            applicantCount = 35,
            isActive = true,
            employerId = "emp_stripe"
        ),
        Job(
            id = "job_5",
            title = "Senior Frontend Engineer (Design Systems)",
            companyName = "Airbnb",
            companyLogoText = "A",
            companyColorHex = 0xFFFF5A5F,
            location = "Austin, TX",
            isRemote = true,
            jobType = JobType.FULL_TIME,
            experienceLevel = ExperienceLevel.SENIOR,
            salaryMin = 175000,
            salaryMax = 220000,
            salaryPeriod = "yr",
            postedDate = "4 days ago",
            postedTimestamp = System.currentTimeMillis() - 4 * 86400000L,
            department = "Design Technology",
            description = "Join Airbnb’s Design Technology team to pioneer accessible, fluid UI components that define the benchmark for hospitality and travel design around the globe.",
            requirements = listOf(
                "5+ years building accessible component libraries and design tokens",
                "Expert knowledge of web performance, animations, and micro-interactions",
                "Collaborative mindset bridging design and engineering teams",
                "Experience supporting multi-brand or globalized localization"
            ),
            benefits = listOf(
                "$2,000 annual Airbnb travel credit",
                "Live and work anywhere program",
                "Health, dental, vision, and mental wellness benefits",
                "401(k) retirement plan with immediate matching"
            ),
            applicantCount = 51,
            isActive = true,
            employerId = "emp_airbnb"
        ),
        Job(
            id = "job_6",
            title = "Contract Mobile QA Automation Engineer",
            companyName = "Spotify",
            companyLogoText = "S",
            companyColorHex = 0xFF1DB954,
            location = "Boston, MA",
            isRemote = true,
            jobType = JobType.CONTRACT,
            experienceLevel = ExperienceLevel.MID,
            salaryMin = 90000,
            salaryMax = 120000,
            salaryPeriod = "yr",
            postedDate = "1 day ago",
            postedTimestamp = System.currentTimeMillis() - 86400000L,
            department = "Client Quality",
            description = "Build robust end-to-end automated UI and playback performance test suites for Spotify's Android streaming experience. 12-month renewable contract.",
            requirements = listOf(
                "3+ years with Android test frameworks (Robolectric, UIAutomator, Compose Test)",
                "Experience setting up device farm testing in CI pipelines",
                "Familiarity with audio streaming metrics and network throttling tests"
            ),
            benefits = listOf(
                "Competitive hourly/contract rate",
                "Flexible hours across US timezones",
                "Spotify Premium family account included"
            ),
            applicantCount = 14,
            isActive = true,
            employerId = "emp_spotify"
        )
    )

    val initialApplications = listOf(
        JobApplication(
            id = "app_1",
            jobId = "job_1", // Stripe Senior Android
            applicantName = "Alex Morgan",
            applicantEmail = "alex.morgan@email.com",
            applicantPhone = "+1 (555) 234-5678",
            applicantHeadline = "Senior Mobile Engineer • Kotlin & Compose Specialist",
            applicantLocation = "San Francisco, CA",
            cvFileName = "Alex_Morgan_Senior_Android_Resume_2026.pdf",
            cvFileSize = "2.4 MB",
            coverLetter = "Dear Stripe Talent Team,\n\nI have followed Stripe's mobile SDK evolution with great admiration, particularly your adoption of Jetpack Compose and clean developer-facing APIs. In my current role at ScaleGrid, I spearheaded our migration to 100% Compose, improving app startup times by 32% and architecting robust offline-resilient payments. I would love to bring this expertise to Stripe's mobile platform.",
            appliedDate = "Oct 18, 2026",
            appliedTimestamp = System.currentTimeMillis() - 2 * 86400000L,
            status = ApplicationStatus.INTERVIEW,
            notes = listOf(
                EmployerNote(
                    id = "note_1",
                    author = "Sarah Chen",
                    text = "Strong mobile architectural background. Technical screen scheduled with Engineering Manager for Thursday.",
                    timestamp = "Oct 19, 2026 • 2:15 PM"
                )
            ),
            skills = listOf("Kotlin", "Jetpack Compose", "Coroutines", "Clean Architecture", "Payments SDK"),
            experienceYears = 6,
            matchScore = 96
        ),
        JobApplication(
            id = "app_2",
            jobId = "job_4", // Notion Mobile Growth
            applicantName = "Alex Morgan",
            applicantEmail = "alex.morgan@email.com",
            applicantPhone = "+1 (555) 234-5678",
            applicantHeadline = "Senior Mobile Engineer",
            applicantLocation = "San Francisco, CA",
            cvFileName = "Alex_Morgan_Senior_Android_Resume_2026.pdf",
            cvFileSize = "2.4 MB",
            coverLetter = "Notion is an indispensable tool in my everyday engineering workflow. I am thrilled by the opportunity to apply experimental growth frameworks and craft delightful mobile onboarding moments for millions of users.",
            appliedDate = "Oct 15, 2026",
            appliedTimestamp = System.currentTimeMillis() - 5 * 86400000L,
            status = ApplicationStatus.UNDER_REVIEW,
            notes = listOf(
                EmployerNote(
                    id = "note_2",
                    author = "Recruiter",
                    text = "Resume reviewed. Matches core growth experiment requirements well.",
                    timestamp = "Oct 16, 2026 • 10:45 AM"
                )
            ),
            skills = listOf("Android", "Growth Experiments", "A/B Testing", "Compose"),
            experienceYears = 6,
            matchScore = 88
        ),
        // Other candidates in the employer's pipeline for Stripe jobs
        JobApplication(
            id = "app_3",
            jobId = "job_1", // Stripe Senior Android
            applicantName = "David Kim",
            applicantEmail = "david.kim@techpulse.io",
            applicantPhone = "+1 (555) 876-1234",
            applicantHeadline = "Staff Mobile Architect @ FinTech Corp",
            applicantLocation = "Seattle, WA",
            cvFileName = "David_Kim_Staff_Mobile_CV.pdf",
            cvFileSize = "1.8 MB",
            coverLetter = "Excited to apply for Stripe's Senior Android role. I have over 8 years of native Android experience building payment terminals and cryptographic security modules.",
            appliedDate = "Oct 19, 2026",
            appliedTimestamp = System.currentTimeMillis() - 86400000L,
            status = ApplicationStatus.APPLIED,
            notes = emptyList(),
            skills = listOf("Kotlin", "Security", "NDK", "Compose", "Architecture"),
            experienceYears = 8,
            matchScore = 93
        ),
        JobApplication(
            id = "app_4",
            jobId = "job_1", // Stripe Senior Android
            applicantName = "Elena Rostova",
            applicantEmail = "elena.r@quantumcode.dev",
            applicantPhone = "+1 (555) 432-8899",
            applicantHeadline = "Senior Software Engineer (Android/C++)",
            applicantLocation = "Austin, TX",
            cvFileName = "Elena_Rostova_Resume.pdf",
            cvFileSize = "2.1 MB",
            coverLetter = "I specialize in audio/video pipeline and low-level Android performance tuning. Excited about Stripe's developer platform and high reliability standards.",
            appliedDate = "Oct 17, 2026",
            appliedTimestamp = System.currentTimeMillis() - 3 * 86400000L,
            status = ApplicationStatus.UNDER_REVIEW,
            notes = listOf(
                EmployerNote(
                    id = "note_4",
                    author = "Sarah Chen",
                    text = "Resume screening passed. Candidate requested remote setup.",
                    timestamp = "Oct 18, 2026 • 4:20 PM"
                )
            ),
            skills = listOf("Kotlin", "C++", "Graphics", "High Performance"),
            experienceYears = 5,
            matchScore = 86
        ),
        JobApplication(
            id = "app_5",
            jobId = "job_2", // Figma Lead Designer
            applicantName = "Marcus Vance",
            applicantEmail = "marcus@vancedesign.co",
            applicantPhone = "+1 (555) 345-9988",
            applicantHeadline = "Principal Design Systems Architect",
            applicantLocation = "New York, NY",
            cvFileName = "Marcus_Vance_Portfolio_CV.pdf",
            cvFileSize = "4.2 MB",
            coverLetter = "I have spent the last 9 years architecting design systems at scale for enterprise collaboration software. Figma has been my canvas throughout.",
            appliedDate = "Oct 16, 2026",
            appliedTimestamp = System.currentTimeMillis() - 4 * 86400000L,
            status = ApplicationStatus.INTERVIEW,
            notes = listOf(
                EmployerNote(
                    id = "note_5",
                    author = "Sarah Chen",
                    text = "Portfolio review was exceptional. Onsite presentation scheduled.",
                    timestamp = "Oct 17, 2026 • 11:00 AM"
                )
            ),
            skills = listOf("Design Systems", "Figma Plugins", "Prototyping", "Leadership"),
            experienceYears = 9,
            matchScore = 98
        ),
        JobApplication(
            id = "app_6",
            jobId = "job_2", // Figma Lead Designer
            applicantName = "Chloe Bennett",
            applicantEmail = "chloe.b@creativemind.io",
            applicantPhone = "+1 (555) 678-2233",
            applicantHeadline = "Senior Product Designer",
            applicantLocation = "Brooklyn, NY",
            cvFileName = "Chloe_Bennett_Resume.pdf",
            cvFileSize = "1.5 MB",
            coverLetter = "Passionate about creating fluid, inclusive interfaces for creative professionals.",
            appliedDate = "Oct 14, 2026",
            appliedTimestamp = System.currentTimeMillis() - 6 * 86400000L,
            status = ApplicationStatus.HIRED,
            notes = listOf(
                EmployerNote(
                    id = "note_6",
                    author = "Sarah Chen",
                    text = "Offer accepted! Start date confirmed for Nov 15th.",
                    timestamp = "Oct 19, 2026 • 9:30 AM"
                )
            ),
            skills = listOf("Figma", "Design Research", "Tokens", "UI/UX"),
            experienceYears = 7,
            matchScore = 95
        )
    )
}

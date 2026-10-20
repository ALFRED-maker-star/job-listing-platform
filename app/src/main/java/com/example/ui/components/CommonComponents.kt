package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ApplicationStatus
import com.example.ui.UserRole
import com.example.ui.theme.AppliedGreen
import com.example.ui.theme.AppliedGreenLight
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrandBluePrimary
import com.example.ui.theme.HiredEmerald
import com.example.ui.theme.HiredEmeraldLight
import com.example.ui.theme.InterviewPurple
import com.example.ui.theme.InterviewPurpleLight
import com.example.ui.theme.RejectedRose
import com.example.ui.theme.RejectedRoseLight
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.UnderReviewAmber
import com.example.ui.theme.UnderReviewAmberLight

@Composable
fun WorkFindHeader(
    currentRole: UserRole,
    onRoleChange: (UserRole) -> Unit,
    showDesktopToggle: Boolean = false,
    isDesktopSimulated: Boolean = false,
    onToggleDesktop: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderLight),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BrandBluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "W",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Work",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandBluePrimary
                            )
                        )
                        Text(
                            text = "Find",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }
                    Text(
                        text = if (currentRole == UserRole.APPLICANT) "Job Seeker" else "Employer Portal",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }
            }

            // Role Switcher & Optional Desktop View Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showDesktopToggle && currentRole == UserRole.EMPLOYER) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
                            .background(if (isDesktopSimulated) BrandBluePrimary.copy(alpha = 0.1f) else SurfaceSubtle)
                            .clickable { onToggleDesktop() }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isDesktopSimulated) Icons.Default.PhoneAndroid else Icons.Default.DesktopWindows,
                                contentDescription = "Toggle Desktop Layout",
                                tint = if (isDesktopSimulated) BrandBluePrimary else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isDesktopSimulated) "Mobile View" else "Desktop View",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDesktopSimulated) BrandBluePrimary else TextSecondary
                                )
                            )
                        }
                    }
                }

                // Switch Role Pill
                Box(
                    modifier = Modifier
                        .testTag("switch_role_button")
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (currentRole == UserRole.APPLICANT) BrandBluePrimary.copy(alpha = 0.12f) else AppliedGreenLight)
                        .clickable {
                            val nextRole = if (currentRole == UserRole.APPLICANT) UserRole.EMPLOYER else UserRole.APPLICANT
                            onRoleChange(nextRole)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (currentRole == UserRole.APPLICANT) Icons.Default.Business else Icons.Default.Person,
                            contentDescription = "Switch to ${if (currentRole == UserRole.APPLICANT) "Employer" else "Applicant"}",
                            tint = if (currentRole == UserRole.APPLICANT) BrandBluePrimary else AppliedGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (currentRole == UserRole.APPLICANT) "Switch to Employer" else "Switch to Applicant",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (currentRole == UserRole.APPLICANT) BrandBluePrimary else Color(0xFF15803D)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationStatusBadge(
    status: ApplicationStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        ApplicationStatus.APPLIED -> Triple(AppliedGreenLight, AppliedGreen, "Applied")
        ApplicationStatus.UNDER_REVIEW -> Triple(UnderReviewAmberLight, Color(0xFFB45309), "Under Review")
        ApplicationStatus.INTERVIEW -> Triple(InterviewPurpleLight, InterviewPurple, "Interview")
        ApplicationStatus.REJECTED -> Triple(RejectedRoseLight, RejectedRose, "Rejected")
        ApplicationStatus.HIRED -> Triple(HiredEmeraldLight, Color(0xFF047857), "Hired")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}

@Composable
fun CompanyLogoBadge(
    text: String,
    colorHex: Long,
    sizeDp: Int = 44,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(colorHex).copy(alpha = 0.15f))
            .border(1.dp, Color(colorHex).copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color(colorHex)
            )
        )
    }
}

@Composable
fun WorkFindSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search job title, skill, or keyword...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = BrandBluePrimary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.testTag("clear_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = TextMuted
                    )
                }
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = BrandBluePrimary,
            unfocusedBorderColor = BorderLight
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("search_bar_input")
    )
}

@Composable
fun FilterChipRow(
    title: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                val chipBg by animateColorAsState(
                    targetValue = if (isSelected) BrandBluePrimary else Color.White,
                    label = "chipBg"
                )
                val chipText by animateColorAsState(
                    targetValue = if (isSelected) Color.White else TextSecondary,
                    label = "chipText"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) BrandBluePrimary else BorderLight,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(chipBg)
                        .clickable { onSelect(option) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = chipText
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StatMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextMuted,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary
                )
            )
        }
    }
}

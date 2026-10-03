package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.example.data.model.AssignmentEntity
import com.example.data.model.AssignmentStatus
import com.example.data.model.GradeLevel
import com.example.data.model.StudentEntity
import com.example.ui.theme.BloomPink
import com.example.ui.theme.GoldStar
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.MeadowTeal
import com.example.ui.theme.PreschoolAmber
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.WarmCard
import com.example.ui.theme.WarmCream

@Composable
fun TeacherDashboardScreen(
    students: List<StudentEntity>,
    assignments: List<AssignmentEntity>,
    onBack: () -> Unit,
    onRunAutomatedGrading: () -> Unit,
    onSyncNow: () -> Unit,
    onShareWhatsApp: () -> Unit,
    isSyncing: Boolean
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Class Analytics", "Automated Grading", "Student Roster", "School Info")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F9))
            .testTag("teacher_dashboard_screen")
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("teacher_back_button")) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MeadowTeal
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Principal & Teacher Portal",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "Blooming Buds Preschool • Patoda",
                                fontSize = 12.sp,
                                color = MeadowTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = onShareWhatsApp,
                            colors = ButtonDefaults.buttonColors(containerColor = LeafGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("teacher_whatsapp_share_button")
                        ) {
                            Text(text = "Share 💬", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        // Sync Button
                        Button(
                            onClick = onSyncNow,
                            colors = ButtonDefaults.buttonColors(containerColor = if (isSyncing) Color.Gray else MeadowTeal),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("teacher_sync_button")
                        ) {
                            Icon(imageVector = Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (isSyncing) "Syncing..." else "Sync", fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Principal Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MeadowTeal.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.School, contentDescription = null, tint = MeadowTeal)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Principal: Vasundhara Ekbote",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MeadowTeal
                            )
                            Text(
                                text = "Full Administrative & Course Access • Manjarsumbha Road, Patoda",
                                fontSize = 11.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = MeadowTeal
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }
        }

        // Tab Body Content
        when (selectedTab) {
            0 -> ClassAnalyticsTab(students, assignments)
            1 -> AutomatedGradingTab(assignments, onRunAutomatedGrading)
            2 -> StudentRosterTab(students)
            3 -> SchoolInfoTab()
        }
    }
}

@Composable
fun ClassAnalyticsTab(students: List<StudentEntity>, assignments: List<AssignmentEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("Total Learners", "${students.size}", "Nursery, LKG, UKG", PreschoolAmber, Modifier.weight(1f))
                MetricCard("Active Assignments", "${assignments.size}", "Worksheets & Quizzes", SkyBlue, Modifier.weight(1f))
                MetricCard("Average Score", "93%", "Automated Mastery", LeafGreen, Modifier.weight(1f))
            }
        }

        item {
            Text(
                text = "Preschool Grade Performance",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.DarkGray
            )
        }

        item {
            GradeProgressCard("Nursery (Ages 2.5 - 3.5)", "Phonics A-E, Counting 1-5, Shapes", 92, PreschoolAmber)
        }
        item {
            GradeProgressCard("LKG (Ages 3.5 - 4.5)", "Letter Tracing Whiteboard, Short Vowels, 1-10", 88, MeadowTeal)
        }
        item {
            GradeProgressCard("UKG (Ages 4.5 - 5.5)", "Sight Words, Picture Math Addition, CVC Words", 95, SkyBlue)
        }

        item {
            Text(
                text = "Recent Real-Time Submissions",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.DarkGray
            )
        }

        items(assignments.take(3)) { asg ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Assignment, contentDescription = null, tint = MeadowTeal)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = asg.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "Student: ${asg.studentName} (${asg.gradeLevel.displayName})", fontSize = 11.sp, color = Color.Gray)
                    }
                    Text(
                        text = if (asg.status == AssignmentStatus.GRADED) "${asg.autoGradedScore}%" else "Pending",
                        fontWeight = FontWeight.Bold,
                        color = if (asg.status == AssignmentStatus.GRADED) LeafGreen else PreschoolAmber,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AutomatedGradingTab(
    assignments: List<AssignmentEntity>,
    onRunAutomatedGrading: () -> Unit
) {
    val pendingCount = assignments.count { it.status != AssignmentStatus.GRADED }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = MeadowTeal)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Automated Grading Engine",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MeadowTeal
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Analyzes student handwriting strokes on the whiteboard, phonics bubble responses, and counting accuracy to auto-assign scores and constructive teacher feedback.",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onRunAutomatedGrading,
                        colors = ButtonDefaults.buttonColors(containerColor = MeadowTeal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("run_auto_grading_button")
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Grade $pendingCount Pending Assignments Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                text = "Student Assignment Submissions (${assignments.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.DarkGray
            )
        }

        items(assignments) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "${item.studentName} • ${item.gradeLevel.displayName} • ${item.submissionType}",
                                fontSize = 11.sp,
                                color = MeadowTeal
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (item.status == AssignmentStatus.GRADED) LeafGreen.copy(alpha = 0.15f) else PreschoolAmber.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (item.status == AssignmentStatus.GRADED) "Graded: ${item.autoGradedScore}%" else "Pending Review",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (item.status == AssignmentStatus.GRADED) LeafGreen else PreschoolAmber
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Feedback: ${item.teacherFeedback}",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StudentRosterTab(students: List<StudentEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(students) { student ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        color = WarmCream
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            val emoji = when (student.gradeLevel) {
                                GradeLevel.NURSERY -> "🐰"
                                GradeLevel.LKG -> "🐻"
                                GradeLevel.UKG -> "🦁"
                            }
                            Text(text = emoji, fontSize = 24.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = student.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(
                            text = "${student.gradeLevel.displayName} • Guardian: ${student.guardianName}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Contact: ${student.guardianContact}",
                            fontSize = 11.sp,
                            color = MeadowTeal
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${student.starsEarned} ⭐",
                            fontWeight = FontWeight.Bold,
                            color = PreschoolAmber,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${student.flowersUnlocked} Blooms",
                            fontSize = 11.sp,
                            color = BloomPink
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SchoolInfoTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.School, contentDescription = null, tint = PreschoolAmber, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Blooming Buds Preschool",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color.DarkGray
                        )
                        Text(text = "Patoda Campus", fontSize = 13.sp, color = MeadowTeal, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider()
                Spacer(modifier = Modifier.height(14.dp))

                InfoRow(icon = Icons.Default.Person, label = "Principal", value = "Vasundhara Ekbote")
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.LocationOn, label = "Location", value = "Manjarsumbha Road, Patoda")
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.Call, label = "Phone", value = "9881127065")
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.School, label = "Curriculum", value = "Nursery, LKG, UKG (ECE Standards)")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🌱 Preschool Mission & Values",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = LeafGreen
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Fostering joyful, child-centric early education with accessible offline technology, active parent partnerships, and personalized multi-sensory milestones for every blooming child.",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MeadowTeal, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$label: ", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.DarkGray)
        Text(text = value, fontSize = 13.sp, color = Color.DarkGray)
    }
}

@Composable
fun MetricCard(title: String, value: String, subtitle: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Text(text = subtitle, fontSize = 9.sp, color = Color.Gray)
        }
    }
}

@Composable
fun GradeProgressCard(title: String, topics: String, progressPercent: Int, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "$progressPercent% Mastery", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = color)
            }
            Text(text = topics, fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = color,
                trackColor = Color(0xFFECEFF1)
            )
        }
    }
}

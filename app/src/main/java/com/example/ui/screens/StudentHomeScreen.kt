package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CurriculumModuleEntity
import com.example.data.model.GradeLevel
import com.example.data.model.ModuleCategory
import com.example.data.model.StudentEntity
import com.example.ui.components.OfflineSyncBadge
import com.example.ui.components.VoiceGuideSpeakerButton
import com.example.ui.theme.BloomPink
import com.example.ui.theme.GoldStar
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.MeadowTeal
import com.example.ui.theme.PreschoolAmber
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SunshineYellow
import com.example.ui.theme.WarmCard
import com.example.ui.theme.WarmCream

@Composable
fun StudentHomeScreen(
    student: StudentEntity,
    currentGrade: GradeLevel,
    modules: List<CurriculumModuleEntity>,
    isSpeaking: Boolean,
    voiceEnabled: Boolean,
    isSyncing: Boolean,
    onGradeSelected: (GradeLevel) -> Unit,
    onSelectModule: (CurriculumModuleEntity) -> Unit,
    onOpenWhiteboard: () -> Unit,
    onOpenPhonicsGame: () -> Unit,
    onOpenNumberGame: () -> Unit,
    onOpenGarden: () -> Unit,
    onOpenParentPortal: () -> Unit,
    onOpenTeacherDashboard: () -> Unit,
    onToggleVoice: () -> Unit,
    onTriggerSync: () -> Unit,
    onSwitchStudent: () -> Unit,
    onShareWhatsApp: () -> Unit,
    onSpeakPrompt: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .padding(16.dp)
            .testTag("student_home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Student profile avatar pill
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSwitchStudent() }
                        .testTag("switch_student_profile_button"),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape),
                            color = PreschoolAmber.copy(alpha = 0.2f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                val emoji = when (student.gradeLevel) {
                                    GradeLevel.NURSERY -> "🐰"
                                    GradeLevel.LKG -> "🐻"
                                    GradeLevel.UKG -> "🦁"
                                }
                                Text(text = emoji, fontSize = 20.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = student.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "${student.gradeLevel.displayName} Level",
                                fontSize = 11.sp,
                                color = MeadowTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Voice Guide Speaker & Sync badge & WhatsApp Share
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OfflineSyncBadge(
                        isOnlineMode = true,
                        isSyncing = isSyncing,
                        onTriggerSync = onTriggerSync
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        modifier = Modifier
                            .testTag("whatsapp_share_top_button")
                            .size(44.dp)
                            .clip(CircleShape)
                            .clickable { onShareWhatsApp() },
                        color = LeafGreen,
                        shadowElevation = 3.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "💬", fontSize = 18.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    VoiceGuideSpeakerButton(
                        isSpeaking = isSpeaking,
                        voiceEnabled = voiceEnabled,
                        onToggle = onToggleVoice,
                        onSpeakCurrentPrompt = {
                            onSpeakPrompt("Hello ${student.name}! Welcome to Blooming Buds Preschool! Let's touch a fun activity to start learning!")
                        }
                    )
                }
            }
        }

        // Hero Banner Illustration
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.img_preschool_hero),
                        contentDescription = "Preschool Garden Classroom",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Overlay greeting
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0xCC004D40))
                                )
                            )
                            .padding(14.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "Blooming Buds Preschool",
                                    color = SunshineYellow,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Let's Bloom & Grow! 🌸",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            // Bloom Garden Quick Pill
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onOpenGarden() }
                                    .testTag("quick_garden_button"),
                                color = PreschoolAmber,
                                shadowElevation = 3.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${student.starsEarned} ⭐",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // WhatsApp Share Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onShareWhatsApp() }
                    .testTag("whatsapp_invite_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape),
                        color = LeafGreen
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "💬", fontSize = 18.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Share Blooming Buds on WhatsApp",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = LeafGreen
                        )
                        Text(
                            text = "Send app link to parents & friends across Patoda",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = LeafGreen
                    ) {
                        Text(
                            text = "Share 💬",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Age-Appropriate Grade Level Tabs (Nursery, LKG, UKG)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GradeLevel.values().forEach { level ->
                    val isSelected = level == currentGrade
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                onGradeSelected(level)
                                onSpeakPrompt("Viewing ${level.displayName} curriculum for ${level.ageGroup}. ${level.description}")
                            }
                            .testTag("grade_tab_${level.name}"),
                        color = if (isSelected) PreschoolAmber else Color.White,
                        shadowElevation = if (isSelected) 3.dp else 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = level.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isSelected) Color.White else Color.DarkGray
                            )
                            Text(
                                text = level.ageGroup,
                                fontSize = 10.sp,
                                color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color.Gray
                            )
                        }
                    }
                }
            }
        }

        // Scaffolded Learning Actions (Quick Buttons)
        item {
            Text(
                text = "Interactive Learning Zones",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActivityHubCard(
                    title = "Whiteboard",
                    subtitle = "Tracing & Art",
                    emoji = "✍️",
                    bgColor = Color(0xFFFFF3E0),
                    accentColor = PreschoolAmber,
                    modifier = Modifier.weight(1f),
                    testTag = "hub_whiteboard_card",
                    onClick = onOpenWhiteboard
                )
                ActivityHubCard(
                    title = "Phonics Pop",
                    subtitle = "Letters & Sounds",
                    emoji = "🎈",
                    bgColor = Color(0xFFE1F5FE),
                    accentColor = SkyBlue,
                    modifier = Modifier.weight(1f),
                    testTag = "hub_phonics_card",
                    onClick = onOpenPhonicsGame
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActivityHubCard(
                    title = "Number Garden",
                    subtitle = "Count & Math",
                    emoji = "🔢",
                    bgColor = Color(0xFFE8F5E9),
                    accentColor = LeafGreen,
                    modifier = Modifier.weight(1f),
                    testTag = "hub_number_card",
                    onClick = onOpenNumberGame
                )
                ActivityHubCard(
                    title = "Bloom Garden",
                    subtitle = "Stars & Badges",
                    emoji = "🌸",
                    bgColor = Color(0xFFFCE4EC),
                    accentColor = BloomPink,
                    modifier = Modifier.weight(1f),
                    testTag = "hub_garden_card",
                    onClick = onOpenGarden
                )
            }
        }

        // Personalized Curriculum Modules
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentGrade.displayName} Early Milestones",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Text(
                    text = "${modules.count { it.isCompleted }}/${modules.size} Completed",
                    fontSize = 12.sp,
                    color = MeadowTeal,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(modules) { module ->
            ModuleItemCard(
                module = module,
                onClick = { onSelectModule(module) },
                onSpeak = { onSpeakPrompt(module.voiceInstruction) }
            )
        }

        // Secure Portals (Guardian & Teacher)
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Grown-Up & Faculty Portals",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenParentPortal() }
                        .testTag("open_parent_portal_button"),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape),
                            color = MeadowTeal.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.SupervisorAccount, contentDescription = null, tint = MeadowTeal, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "Parent Portal", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(text = "Milestones & Lock", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenTeacherDashboard() }
                        .testTag("open_teacher_portal_button"),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape),
                            color = PreschoolAmber.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.School, contentDescription = null, tint = PreschoolAmber, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "Teacher Admin", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(text = "Vasundhara Ekbote", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ActivityHubCard(
    title: String,
    subtitle: String,
    emoji: String,
    bgColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
                Text(text = subtitle, fontSize = 11.sp, color = accentColor, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ModuleItemCard(
    module: CurriculumModuleEntity,
    onClick: () -> Unit,
    onSpeak: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag("curriculum_module_${module.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val emoji = when (module.category) {
                ModuleCategory.LITERACY -> "🔤"
                ModuleCategory.NUMERACY -> "🔢"
                ModuleCategory.SHAPES_COLORS -> "🎨"
                ModuleCategory.WHITEBOARD_TRACING -> "✏️"
                ModuleCategory.RHYMES_STORIES -> "📖"
            }

            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                color = WarmCream
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = emoji, fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = module.category.displayName.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MeadowTeal
                )
                Text(
                    text = module.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = module.subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onSpeak, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speak instruction",
                        tint = PreschoolAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (module.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = LeafGreen,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PreschoolAmber.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "+${module.starsReward} ⭐",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreschoolAmber
                        )
                    }
                }
            }
        }
    }
}

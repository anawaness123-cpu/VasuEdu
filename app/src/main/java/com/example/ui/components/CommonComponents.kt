package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GradeLevel
import com.example.data.model.StudentEntity
import com.example.ui.theme.BloomPink
import com.example.ui.theme.GoldStar
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.MeadowTeal
import com.example.ui.theme.PreschoolAmber
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.WarmCream

@Composable
fun VoiceGuideSpeakerButton(
    isSpeaking: Boolean,
    voiceEnabled: Boolean,
    onToggle: () -> Unit,
    onSpeakCurrentPrompt: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Surface(
        modifier = modifier
            .testTag("voice_guide_button")
            .size(48.dp)
            .scale(if (isSpeaking) scale else 1f)
            .clip(CircleShape)
            .clickable {
                if (voiceEnabled) {
                    onSpeakCurrentPrompt()
                } else {
                    onToggle()
                }
            },
        color = if (!voiceEnabled) Color.LightGray else if (isSpeaking) BloomPink else PreschoolAmber,
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (!voiceEnabled) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                contentDescription = if (voiceEnabled) "Voice Guidance Speaker" else "Voice Muted",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
fun OfflineSyncBadge(
    isOnlineMode: Boolean,
    isSyncing: Boolean,
    onTriggerSync: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .testTag("sync_status_badge")
            .clip(RoundedCornerShape(16.dp))
            .clickable { onTriggerSync() },
        color = if (isSyncing) MeadowTeal.copy(alpha = 0.2f) else LeafGreen.copy(alpha = 0.15f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isSyncing) Icons.Default.Sync else Icons.Default.CheckCircle,
                contentDescription = "Sync Status",
                tint = if (isSyncing) MeadowTeal else LeafGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isSyncing) "Syncing..." else "Offline Ready • Synced",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSyncing) MeadowTeal else LeafGreen
            )
        }
    }
}

@Composable
fun GuardianSecurityDialog(
    targetRole: String, // "Guardian / Parent" or "Teacher / Principal"
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var pinText by remember { mutableStateOf("") }
    var mathAnswerText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val num1 = 4
    val num2 = 3
    val expectedMathAnswer = (num1 + num2).toString()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = PreschoolAmber,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Grown-Up Verification",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "To access the $targetRole area, solve the safety check or enter PIN (Default: 1234):",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = WarmCream),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Safety Challenge: What is $num1 + $num2?",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MeadowTeal
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = mathAnswerText,
                            onValueChange = {
                                mathAnswerText = it
                                errorMessage = null
                            },
                            placeholder = { Text("Answer here") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("math_challenge_input")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "— OR Enter 4-digit PIN —",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pinText,
                    onValueChange = {
                        if (it.length <= 4) {
                            pinText = it
                            errorMessage = null
                        }
                    },
                    label = { Text("Guardian PIN") },
                    placeholder = { Text("1234") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pin_code_input")
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = BloomPink,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (pinText == "1234" || mathAnswerText.trim() == expectedMathAnswer) {
                        onSuccess()
                    } else {
                        errorMessage = "Incorrect PIN or math answer. Try PIN: 1234"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PreschoolAmber),
                modifier = Modifier.testTag("verify_guardian_button")
            ) {
                Text("Enter $targetRole", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_guardian_button")
            ) {
                Text("Back to Student")
            }
        }
    )
}

@Composable
fun CelebrationDialog(
    starsWon: Int,
    title: String,
    message: String,
    onContinue: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onContinue,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🌟 Fantastic Job! 🌟",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PreschoolAmber,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(starsWon) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = GoldStar,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "🌸 A new flower is blooming in your Preschool Garden! 🌸",
                    fontSize = 12.sp,
                    color = BloomPink,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onContinue,
                colors = ButtonDefaults.buttonColors(containerColor = PreschoolAmber),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("celebration_continue_button")
            ) {
                Text("Keep Blooming! 🚀", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    )
}

@Composable
fun StudentSwitcherDialog(
    students: List<StudentEntity>,
    currentStudentId: String,
    onSelectStudent: (StudentEntity) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Student Profile",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Switch between preschool grade levels:",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                students.forEach { student ->
                    val isSelected = student.id == currentStudentId
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectStudent(student) }
                            .testTag("student_select_${student.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) PreschoolAmber.copy(alpha = 0.15f) else Color.White
                        ),
                        border = if (isSelected) borderBorder(PreschoolAmber) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val emoji = when (student.gradeLevel) {
                                GradeLevel.NURSERY -> "🐰"
                                GradeLevel.LKG -> "🐻"
                                GradeLevel.UKG -> "🦁"
                            }
                            Text(text = emoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = student.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "${student.gradeLevel.displayName} (${student.gradeLevel.ageGroup})",
                                    fontSize = 12.sp,
                                    color = MeadowTeal
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldStar,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = " ${student.starsEarned}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

private fun borderBorder(color: Color) = androidx.compose.foundation.BorderStroke(2.dp, color)

const val APP_SHARE_URL = "https://ais-pre-il4pkt7hmbnf7mjtrmtntv-541325066669.asia-southeast1.run.app"

val DEFAULT_SHARE_MESSAGE = """
🌸 Blooming Buds Preschool Patoda 🌸
Early Learning App for Nursery, LKG, and UKG!

✨ Highlights:
• Colorful Phonics & Early Literacy Lessons
• Interactive Whiteboard & Letter Tracing
• Motivating Number Garden & Bubble Pop Games
• 100% Ad-Free, Child-Safe & Offline-Ready
• Guardian Progress Reports & Automated Grading

👩‍🏫 Principal: Vasundhara Ekbote
📍 Location: Manjarsumbha Road, Patoda
📞 Contact: 9881127065

📲 Try the App here:
$APP_SHARE_URL
""".trimIndent()

fun shareAppViaWhatsApp(context: android.content.Context, message: String = DEFAULT_SHARE_MESSAGE) {
    val sendIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(android.content.Intent.EXTRA_TEXT, message)
        setPackage("com.whatsapp")
    }
    try {
        context.startActivity(sendIntent)
    } catch (_: Exception) {
        val chooserIntent = android.content.Intent.createChooser(
            android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_TEXT, message)
            },
            "Share Blooming Buds Preschool App"
        )
        context.startActivity(chooserIntent)
    }
}

@Composable
fun ShareAppDialog(
    onDismiss: () -> Unit,
    onShare: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "💬", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Share with Parents on WhatsApp",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Share Blooming Buds Preschool learning app link directly with parents, teachers, and family:",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = WarmCream),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Blooming Buds Preschool Patoda",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MeadowTeal
                        )
                        Text(
                            text = "Principal: Vasundhara Ekbote\nContact: 9881127065\nManjarsumbha Road, Patoda",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = APP_SHARE_URL,
                            fontSize = 10.sp,
                            color = SkyBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onShare()
                    shareAppViaWhatsApp(context)
                },
                colors = ButtonDefaults.buttonColors(containerColor = LeafGreen),
                modifier = Modifier.testTag("confirm_whatsapp_share_button")
            ) {
                Text("Share on WhatsApp 💬", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

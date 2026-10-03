package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CurriculumModuleEntity
import com.example.ui.theme.BloomPink
import com.example.ui.theme.GoldStar
import com.example.ui.theme.MeadowTeal
import com.example.ui.theme.PreschoolAmber
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.WarmCream

data class LessonSlide(
    val title: String,
    val bigEmoji: String,
    val phonicsSound: String,
    val explanation: String,
    val exampleSentence: String
)

@Composable
fun LessonViewerScreen(
    module: CurriculumModuleEntity,
    onBack: () -> Unit,
    onCompleteLesson: (score: Int) -> Unit,
    onSpeak: (String) -> Unit
) {
    val slides = remember(module.id) {
        listOf(
            LessonSlide(
                title = "Meet the Letter & Sound",
                bigEmoji = "🍎",
                phonicsSound = "/æ/ as in Apple",
                explanation = "A is the first letter! When we say A, our mouth opens wide: /æ/!",
                exampleSentence = "A big red Apple grows on the garden tree!"
            ),
            LessonSlide(
                title = "Phonics Word Family",
                bigEmoji = "🐜",
                phonicsSound = "A for Ant & Airplane",
                explanation = "Look at the busy Ant walking with friends!",
                exampleSentence = "The Ant sees an Airplane zooming high in the blue sky!"
            ),
            LessonSlide(
                title = "Checkpoint Fun Challenge",
                bigEmoji = "🌟",
                phonicsSound = "Test your Knowledge!",
                explanation = "Which of these starts with the /æ/ letter sound?",
                exampleSentence = "Listen carefully and choose the correct answer below!"
            )
        )
    }

    var currentSlideIndex by remember { mutableIntStateOf(0) }
    var selectedCheckpointAnswer by remember { mutableStateOf<String?>(null) }
    var checkpointSuccess by remember { mutableStateOf(false) }

    val currentSlide = slides[currentSlideIndex]

    LaunchedEffect(currentSlideIndex) {
        onSpeak("${currentSlide.title}. ${currentSlide.explanation} ${currentSlide.exampleSentence}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF8))
            .padding(16.dp)
            .testTag("lesson_viewer_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("lesson_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = PreschoolAmber
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = module.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Slide ${currentSlideIndex + 1} of ${slides.size}",
                    fontSize = 12.sp,
                    color = MeadowTeal,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(
                onClick = {
                    onSpeak("${currentSlide.explanation} ${currentSlide.exampleSentence}")
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PreschoolAmber.copy(alpha = 0.2f))
                    .testTag("lesson_audio_repeat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Read Aloud",
                    tint = PreschoolAmber
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress indicator
        LinearProgressIndicator(
            progress = { (currentSlideIndex + 1) / slides.size.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = PreschoolAmber,
            trackColor = PreschoolAmber.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Animated Slide Content
        AnimatedContent(
            targetState = currentSlide,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "slide_transition",
            modifier = Modifier.weight(1f)
        ) { slide ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("lesson_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = slide.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PreschoolAmber,
                        textAlign = TextAlign.Center
                    )

                    // Big visual illustration
                    Surface(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        color = WarmCream
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = slide.bigEmoji, fontSize = 72.sp)
                        }
                    }

                    // Phonics Sound badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MeadowTeal.copy(alpha = 0.15f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = slide.phonicsSound,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MeadowTeal,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }

                    Text(
                        text = slide.explanation,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )

                    Text(
                        text = "“${slide.exampleSentence}”",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = BloomPink,
                        fontWeight = FontWeight.SemiBold
                    )

                    // If it's the checkpoint slide, show quiz choices
                    if (currentSlideIndex == slides.size - 1) {
                        val choices = listOf("🍎 Apple", "🐶 Dog", "🚗 Car")
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            choices.forEach { choice ->
                                val isSelected = selectedCheckpointAnswer == choice
                                val isCorrect = choice.startsWith("🍎")
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            2.dp,
                                            if (isSelected) (if (isCorrect) MeadowTeal else BloomPink) else Color.LightGray,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            selectedCheckpointAnswer = choice
                                            if (isCorrect) {
                                                checkpointSuccess = true
                                                onSpeak("Correct! Apple starts with the /æ/ sound! You earned 3 stars!")
                                            } else {
                                                onSpeak("Try again! Listen for the /æ/ sound!")
                                            }
                                        }
                                        .testTag("quiz_choice_$choice"),
                                    color = if (isSelected) (if (isCorrect) MeadowTeal.copy(alpha = 0.15f) else BloomPink.copy(alpha = 0.1f)) else WarmCream
                                ) {
                                    Text(
                                        text = choice,
                                        modifier = Modifier.padding(12.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (currentSlideIndex > 0) currentSlideIndex--
                },
                enabled = currentSlideIndex > 0,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.testTag("lesson_previous_button")
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back")
            }

            if (currentSlideIndex < slides.size - 1) {
                Button(
                    onClick = { currentSlideIndex++ },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PreschoolAmber),
                    modifier = Modifier.testTag("lesson_next_button")
                ) {
                    Text("Next Slide", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            } else {
                Button(
                    onClick = {
                        onCompleteLesson(100)
                    },
                    enabled = checkpointSuccess || selectedCheckpointAnswer != null,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MeadowTeal),
                    modifier = Modifier.testTag("lesson_finish_button")
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Complete Lesson 🌟", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BloomPink
import com.example.ui.theme.GoldStar
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.MeadowTeal
import com.example.ui.theme.PreschoolAmber
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SunshineYellow
import com.example.ui.theme.WarmCard
import com.example.ui.theme.WarmCream
import kotlin.random.Random

// --- 1. Alphabet Phonics Bubble Pop Game ---

data class BubbleItem(
    val id: Int,
    val letter: String,
    val word: String,
    val emoji: String,
    val color: Color,
    val isPopped: Boolean = false
)

@Composable
fun PhonicsBubbleGameScreen(
    onBack: () -> Unit,
    onGameComplete: (score: Int) -> Unit,
    onSpeak: (String) -> Unit
) {
    val context = LocalContext.current
    var score by remember { mutableIntStateOf(0) }
    var currentTargetIndex by remember { mutableIntStateOf(0) }

    val alphabetList = remember {
        listOf(
            Triple("A", "Apple", "🍎"),
            Triple("B", "Butterfly", "🦋"),
            Triple("C", "Cat", "🐱"),
            Triple("D", "Duck", "🦆"),
            Triple("E", "Elephant", "🐘"),
            Triple("F", "Fish", "🐟")
        )
    }

    val currentTarget = alphabetList[currentTargetIndex % alphabetList.size]

    val bubbleColors = listOf(SkyBlue, MeadowTeal, BloomPink, PreschoolAmber, SunshineYellow)
    var bubbles by remember {
        mutableStateOf(
            alphabetList.shuffled().mapIndexed { index, (char, word, emoji) ->
                BubbleItem(index, char, word, emoji, bubbleColors[index % bubbleColors.size])
            }
        )
    }

    LaunchedEffect(currentTargetIndex) {
        onSpeak("Can you pop the letter ${currentTarget.first} for ${currentTarget.second}?")
    }

    fun triggerHapticFeedback() {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator?.vibrate(50)
            }
        } catch (_: Exception) {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE1F5FE), Color(0xFFFFF9C4))
                )
            )
            .padding(16.dp)
            .testTag("phonics_bubble_game_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("game_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = PreschoolAmber
                )
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = GoldStar)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Score: $score", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                }
            }

            IconButton(
                onClick = {
                    onSpeak("Find and pop the letter ${currentTarget.first} for ${currentTarget.second}!")
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PreschoolAmber)
            ) {
                Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Hear Again", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Target Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = currentTarget.third, fontSize = 42.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "POP THE LETTER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MeadowTeal
                    )
                    Text(
                        text = "${currentTarget.first} as in ${currentTarget.second}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PreschoolAmber
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bubbles Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(bubbles) { bubble ->
                val isTarget = bubble.letter == currentTarget.first
                Surface(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(if (bubble.isPopped) 0.6f else 1f)
                        .clip(CircleShape)
                        .clickable(enabled = !bubble.isPopped) {
                            triggerHapticFeedback()
                            if (isTarget) {
                                score += 10
                                bubbles = bubbles.map {
                                    if (it.id == bubble.id) it.copy(isPopped = true) else it
                                }
                                onSpeak("Awesome! ${bubble.letter} for ${bubble.word}! Pop!")
                                if (currentTargetIndex < alphabetList.size - 1) {
                                    currentTargetIndex++
                                } else {
                                    onGameComplete(score + 10)
                                }
                            } else {
                                onSpeak("That's letter ${bubble.letter}! Try looking for ${currentTarget.first}!")
                            }
                        }
                        .testTag("bubble_${bubble.letter}"),
                    color = if (bubble.isPopped) Color.LightGray.copy(alpha = 0.4f) else bubble.color,
                    shape = CircleShape,
                    shadowElevation = if (bubble.isPopped) 0.dp else 8.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (bubble.isPopped) {
                            Text(text = "💥 POP!", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 16.sp)
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = bubble.letter,
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = bubble.emoji,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 2. Number Garden Count & Match Game ---

@Composable
fun NumberGardenGameScreen(
    onBack: () -> Unit,
    onGameComplete: (score: Int) -> Unit,
    onSpeak: (String) -> Unit
) {
    var flowerCount by remember { mutableIntStateOf(4) }
    var currentRound by remember { mutableIntStateOf(1) }
    var score by remember { mutableIntStateOf(0) }
    val flowerTypes = listOf("🌸", "🌻", "🌷", "🌺", "🌼")

    LaunchedEffect(flowerCount) {
        onSpeak("How many blooming flowers are in the garden? Count them and tap the right number!")
    }

    val options = remember(flowerCount) {
        val wrong1 = if (flowerCount > 1) flowerCount - 1 else flowerCount + 2
        val wrong2 = flowerCount + 1
        listOf(flowerCount, wrong1, wrong2).shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
            .padding(16.dp)
            .testTag("number_garden_game_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("garden_game_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LeafGreen
                )
            }
            Text(
                text = "Garden Buds Counter (Round $currentRound/3)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = LeafGreen
            )
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = GoldStar, modifier = Modifier.size(16.dp))
                    Text(text = " $score", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Flower Garden Meadow
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .testTag("garden_meadow_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🌱 Blooming Buds Preschool Garden 🌱",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MeadowTeal
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Flowers Display
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(flowerCount) { index ->
                        val flowerEmoji = flowerTypes[index % flowerTypes.size]
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        ) {
                            Text(text = flowerEmoji, fontSize = 48.sp)
                            Text(
                                text = "${index + 1}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MeadowTeal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "How many flowers are there?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Answer Number Options
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            options.forEach { number ->
                Button(
                    onClick = {
                        if (number == flowerCount) {
                            score += 10
                            onSpeak("Spot on! There are $flowerCount beautiful flowers! Great counting!")
                            if (currentRound < 3) {
                                currentRound++
                                flowerCount = Random.nextInt(2, 6)
                            } else {
                                onGameComplete(score + 10)
                            }
                        } else {
                            onSpeak("Let's count together! One, two, three... Try again!")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .testTag("garden_number_$number"),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PreschoolAmber),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text(
                        text = "$number",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

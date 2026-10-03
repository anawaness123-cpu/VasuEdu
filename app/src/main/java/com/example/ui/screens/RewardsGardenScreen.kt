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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.data.model.StudentEntity
import com.example.ui.theme.BloomPink
import com.example.ui.theme.GoldStar
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.MeadowTeal
import com.example.ui.theme.PreschoolAmber
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SunshineYellow

data class GardenFlower(
    val id: Int,
    val name: String,
    val emoji: String,
    val requiredStars: Int,
    val unlockedMessage: String
)

@Composable
fun RewardsGardenScreen(
    student: StudentEntity,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val gardenFlowers = remember {
        listOf(
            GardenFlower(1, "Sunny Sunflower", "🌻", 5, "You unlocked the Sunny Sunflower by learning letter A!"),
            GardenFlower(2, "Pink Lotus", "🪷", 10, "You unlocked the Pink Lotus by mastering Number Counting!"),
            GardenFlower(3, "Patoda Rose", "🌹", 15, "You unlocked the Patoda Rose with your neat handwriting tracing!"),
            GardenFlower(4, "Happy Tulip", "🌷", 20, "You unlocked the Happy Tulip by popping phonics bubbles!"),
            GardenFlower(5, "Cherry Blossom", "🌸", 25, "You unlocked the Cherry Blossom with a 5-day learning streak!"),
            GardenFlower(6, "Golden Star Flower", "🌼", 30, "You unlocked the Golden Star Flower! Outstanding preschooler!")
        )
    }

    val badges = remember {
        listOf(
            Triple("Alphabet Explorer", "🏆", "Mastered A-M sounds"),
            Triple("Number Wizard", "⭐", "Counted 1 to 10 accurately"),
            Triple("Rainbow Painter", "🎨", "Traced 5 letters on Whiteboard"),
            Triple("Attendance Star", "🌟", "Learned 5 days continuously")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE8F5E9), Color(0xFFFFFDE7))
                )
            )
            .padding(16.dp)
            .testTag("rewards_garden_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("garden_back_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LeafGreen
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🌸 Bloom Garden & Badges 🌸",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MeadowTeal
                )
                Text(
                    text = "${student.name}'s Little Garden",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
            Surface(
                color = PreschoolAmber,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Text(
                        text = " ${student.starsEarned}",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Garden Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape),
                    color = LeafGreen.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.WaterDrop, contentDescription = null, tint = LeafGreen, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Watering the Buds",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = LeafGreen
                    )
                    Text(
                        text = "Keep completing preschool lessons to bloom more flowers!",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section: The Blooming Flowers
        Text(
            text = "Blooming Flowers (${gardenFlowers.count { student.starsEarned >= it.requiredStars }}/${gardenFlowers.size})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(gardenFlowers) { flower ->
                val isUnlocked = student.starsEarned >= flower.requiredStars
                Card(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            if (isUnlocked) {
                                onSpeak(flower.unlockedMessage)
                            } else {
                                onSpeak("Earn ${flower.requiredStars} stars to bloom the ${flower.name}!")
                            }
                        }
                        .testTag("garden_flower_${flower.id}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) Color.White else Color(0xFFEEEEEE)
                    ),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isUnlocked) {
                            Text(text = flower.emoji, fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = flower.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MeadowTeal,
                                textAlign = TextAlign.Center
                            )
                            Text(text = "Bloomed! 🌸", fontSize = 10.sp, color = BloomPink)
                        } else {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(vertical = 4.dp)
                            )
                            Text(
                                text = "${flower.requiredStars} ⭐ to bloom",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Section: Badges Showcase
        Text(
            text = "Achievement Badges",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            badges.forEach { (name, emoji, desc) ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onSpeak("Badge earned: $name! $desc!")
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = emoji, fontSize = 24.sp)
                        Text(
                            text = name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

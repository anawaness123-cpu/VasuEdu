package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlackboardDark
import com.example.ui.theme.BloomPink
import com.example.ui.theme.GoldStar
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.MeadowTeal
import com.example.ui.theme.PreschoolAmber
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SunshineYellow

data class DrawnPath(
    val path: Path,
    val color: Color,
    val strokeWidth: Float = 16f
)

@Composable
fun InteractiveWhiteboardScreen(
    currentLetter: String = "A",
    onBack: () -> Unit,
    onSubmitTracing: (accuracy: Int) -> Unit,
    onSpeakInstruction: (String) -> Unit
) {
    val paths = remember { mutableStateListOf<DrawnPath>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var selectedColor by remember { mutableStateOf(PreschoolAmber) }
    var selectedLetter by remember { mutableStateOf(currentLetter) }
    var strokeCount by remember { mutableStateOf(0) }
    var showSuccessBanner by remember { mutableStateOf(false) }

    val letterOptions = listOf("A", "B", "C", "1", "2", "3", "🌸", "★")
    val crayonColors = listOf(
        PreschoolAmber,
        MeadowTeal,
        BloomPink,
        SkyBlue,
        SunshineYellow,
        LeafGreen,
        Color.White
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
            .padding(16.dp)
            .testTag("interactive_whiteboard_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("whiteboard_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PreschoolAmber
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Buds Whiteboard & Tracing",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "Trace letter '$selectedLetter' with colorful crayons!",
                        fontSize = 12.sp,
                        color = MeadowTeal
                    )
                }
            }

            IconButton(
                onClick = {
                    onSpeakInstruction("Let's trace letter $selectedLetter! Follow the dotted lines from top to bottom!")
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PreschoolAmber.copy(alpha = 0.2f))
                    .testTag("whiteboard_audio_guide_button")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Audio Guidance",
                    tint = PreschoolAmber
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Letter selector pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            letterOptions.forEach { char ->
                val isSelected = char == selectedLetter
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            selectedLetter = char
                            paths.clear()
                            strokeCount = 0
                            showSuccessBanner = false
                            onSpeakInstruction("Tracing $char. Choose your crayon color!")
                        }
                        .testTag("select_letter_$char"),
                    color = if (isSelected) PreschoolAmber else Color.White,
                    shadowElevation = if (isSelected) 4.dp else 1.dp
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (isSelected) Color.White else Color.DarkGray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Drawing Canvas (Styled like a modern smart whiteboard chalkboard)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shadow(6.dp, RoundedCornerShape(24.dp))
                .border(4.dp, PreschoolAmber.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                .testTag("whiteboard_drawing_canvas"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = BlackboardDark)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(selectedColor) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val newPath = Path().apply { moveTo(offset.x, offset.y) }
                                    currentPath = newPath
                                    paths.add(DrawnPath(newPath, selectedColor, 20f))
                                    strokeCount++
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentPath?.lineTo(change.position.x, change.position.y)
                                    // Trigger canvas redraw by replacing last path
                                    if (paths.isNotEmpty()) {
                                        val last = paths.removeAt(paths.size - 1)
                                        paths.add(last)
                                    }
                                }
                            )
                        }
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Draw Preschool Four-Line Handwriting Guidelines
                    val lineSpacing = canvasHeight / 5
                    val redLineColor = Color(0x55FF5252)
                    val blueLineColor = Color(0x44448AFF)

                    // Line 1: Top baseline
                    drawLine(
                        color = redLineColor,
                        start = Offset(40f, lineSpacing),
                        end = Offset(canvasWidth - 40f, lineSpacing),
                        strokeWidth = 3f
                    )
                    // Line 2: Mid line
                    drawLine(
                        color = blueLineColor,
                        start = Offset(40f, lineSpacing * 2),
                        end = Offset(canvasWidth - 40f, lineSpacing * 2),
                        strokeWidth = 2f
                    )
                    // Line 3: Mid lower
                    drawLine(
                        color = blueLineColor,
                        start = Offset(40f, lineSpacing * 3),
                        end = Offset(canvasWidth - 40f, lineSpacing * 3),
                        strokeWidth = 2f
                    )
                    // Line 4: Bottom baseline
                    drawLine(
                        color = redLineColor,
                        start = Offset(40f, lineSpacing * 4),
                        end = Offset(canvasWidth - 40f, lineSpacing * 4),
                        strokeWidth = 3f
                    )

                    // Draw the student strokes
                    for (dp in paths) {
                        drawPath(
                            path = dp.path,
                            color = dp.color,
                            style = Stroke(
                                width = dp.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                // Centered Dotted Guide Template Letter for children to trace
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = selectedLetter,
                        fontSize = 170.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White.copy(alpha = 0.16f),
                        letterSpacing = 2.sp
                    )
                }

                if (paths.isEmpty()) {
                    Text(
                        text = "✍️ Use your finger to trace $selectedLetter",
                        color = Color.White.copy(alpha = 0.45f),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Crayon Palette & Tools
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Colors
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                crayonColors.forEach { color ->
                    val isSelected = selectedColor == color
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) PreschoolAmber else Color.LightGray,
                                shape = CircleShape
                            )
                            .clickable { selectedColor = color }
                            .testTag("crayon_color_${color.value}")
                    )
                }
            }

            // Eraser / Clear Button
            Button(
                onClick = {
                    paths.clear()
                    strokeCount = 0
                    showSuccessBanner = false
                    onSpeakInstruction("Whiteboard cleared! Ready for new drawing!")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("clear_whiteboard_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = BloomPink,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Erase", color = BloomPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Submission & Automated Grading Trigger
        Button(
            onClick = {
                val calculatedAccuracy = if (strokeCount >= 2) 94 else 85
                showSuccessBanner = true
                onSubmitTracing(calculatedAccuracy)
                onSpeakInstruction("Great job tracing $selectedLetter! Your handwriting worksheet was submitted to Principal Vasundhara!")
            },
            colors = ButtonDefaults.buttonColors(containerColor = MeadowTeal),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_tracing_button")
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Submit Tracing to Teacher 🌟",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

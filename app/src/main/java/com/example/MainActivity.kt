package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.CurriculumModuleEntity
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.GuardianSecurityDialog
import com.example.ui.components.ShareAppDialog
import com.example.ui.components.StudentSwitcherDialog
import com.example.ui.screens.InteractiveWhiteboardScreen
import com.example.ui.screens.LessonViewerScreen
import com.example.ui.screens.NumberGardenGameScreen
import com.example.ui.screens.ParentPortalScreen
import com.example.ui.screens.PhonicsBubbleGameScreen
import com.example.ui.screens.RewardsGardenScreen
import com.example.ui.screens.StudentHomeScreen
import com.example.ui.screens.TeacherDashboardScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel()
                BloomingBudsApp(viewModel)
            }
        }
    }
}

@Composable
fun BloomingBudsApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentStudent by viewModel.currentStudent.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val allStudents by viewModel.allStudents.collectAsStateWithLifecycle()
    val modules by viewModel.currentModules.collectAsStateWithLifecycle()
    val milestones by viewModel.currentMilestones.collectAsStateWithLifecycle()
    val assignments by viewModel.allAssignments.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.voiceGuideManager.isSpeaking.collectAsStateWithLifecycle()
    val voiceEnabled by viewModel.voiceGuideManager.voiceEnabled.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val celebration by viewModel.celebration.collectAsStateWithLifecycle()

    var showGuardianSecurityDialog by remember { mutableStateOf(false) }
    var securityTargetRole by remember { mutableStateOf("Guardian / Parent") }
    var showStudentSwitcherDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }

    // Intercept back presses on sub-screens
    if (currentScreen !is AppScreen.StudentHome) {
        BackHandler {
            viewModel.navigateBack()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
            when (val screen = currentScreen) {
                is AppScreen.StudentHome -> {
                    StudentHomeScreen(
                        student = currentStudent,
                        currentGrade = selectedGrade,
                        modules = modules,
                        isSpeaking = isSpeaking,
                        voiceEnabled = voiceEnabled,
                        isSyncing = isSyncing,
                        onGradeSelected = { viewModel.selectGrade(it) },
                        onSelectModule = { module ->
                            when (module.lessonType) {
                                "WHITEBOARD" -> viewModel.navigateTo(AppScreen.Whiteboard(module.title.takeLast(1).takeIf { it.first().isLetter() } ?: "A"))
                                "GAME" -> {
                                    if (module.category == com.example.data.model.ModuleCategory.NUMERACY) {
                                        viewModel.navigateTo(AppScreen.NumberGame)
                                    } else {
                                        viewModel.navigateTo(AppScreen.PhonicsGame)
                                    }
                                }
                                else -> viewModel.navigateTo(AppScreen.LessonViewer(module))
                            }
                        },
                        onOpenWhiteboard = { viewModel.navigateTo(AppScreen.Whiteboard("A")) },
                        onOpenPhonicsGame = { viewModel.navigateTo(AppScreen.PhonicsGame) },
                        onOpenNumberGame = { viewModel.navigateTo(AppScreen.NumberGame) },
                        onOpenGarden = { viewModel.navigateTo(AppScreen.RewardsGarden) },
                        onOpenParentPortal = {
                            securityTargetRole = "Guardian / Parent"
                            showGuardianSecurityDialog = true
                        },
                        onOpenTeacherDashboard = {
                            securityTargetRole = "Teacher / Principal"
                            showGuardianSecurityDialog = true
                        },
                        onToggleVoice = { viewModel.toggleVoice() },
                        onTriggerSync = { viewModel.triggerSync() },
                        onSwitchStudent = { showStudentSwitcherDialog = true },
                        onShareWhatsApp = { showShareDialog = true },
                        onSpeakPrompt = { text -> viewModel.speak(text) }
                    )
                }

                is AppScreen.LessonViewer -> {
                    LessonViewerScreen(
                        module = screen.module,
                        onBack = { viewModel.navigateBack() },
                        onCompleteLesson = { score ->
                            viewModel.completeLesson(screen.module, score)
                        },
                        onSpeak = { text -> viewModel.speak(text) }
                    )
                }

                is AppScreen.Whiteboard -> {
                    InteractiveWhiteboardScreen(
                        currentLetter = screen.letter,
                        onBack = { viewModel.navigateBack() },
                        onSubmitTracing = { accuracy ->
                            viewModel.submitWhiteboardTracing(accuracy)
                        },
                        onSpeakInstruction = { text -> viewModel.speak(text) }
                    )
                }

                is AppScreen.PhonicsGame -> {
                    PhonicsBubbleGameScreen(
                        onBack = { viewModel.navigateBack() },
                        onGameComplete = { score ->
                            viewModel.completeGame(starsWon = 3, gameName = "Phonics Bubble Pop")
                        },
                        onSpeak = { text -> viewModel.speak(text) }
                    )
                }

                is AppScreen.NumberGame -> {
                    NumberGardenGameScreen(
                        onBack = { viewModel.navigateBack() },
                        onGameComplete = { score ->
                            viewModel.completeGame(starsWon = 3, gameName = "Number Garden")
                        },
                        onSpeak = { text -> viewModel.speak(text) }
                    )
                }

                is AppScreen.RewardsGarden -> {
                    RewardsGardenScreen(
                        student = currentStudent,
                        onBack = { viewModel.navigateBack() },
                        onSpeak = { text -> viewModel.speak(text) }
                    )
                }

                is AppScreen.ParentPortal -> {
                    ParentPortalScreen(
                        student = currentStudent,
                        milestones = milestones,
                        onBack = { viewModel.navigateBack() },
                        onShareWhatsApp = { showShareDialog = true }
                    )
                }

                is AppScreen.TeacherDashboard -> {
                    TeacherDashboardScreen(
                        students = allStudents,
                        assignments = assignments,
                        onBack = { viewModel.navigateBack() },
                        onRunAutomatedGrading = { viewModel.runAutomatedGrading() },
                        onSyncNow = { viewModel.triggerSync() },
                        onShareWhatsApp = { showShareDialog = true },
                        isSyncing = isSyncing
                    )
                }
            }

            // Share App on WhatsApp Dialog
            if (showShareDialog) {
                ShareAppDialog(
                    onDismiss = { showShareDialog = false },
                    onShare = { showShareDialog = false }
                )
            }

            // Security PIN Gate Dialog for Parent/Teacher access
            if (showGuardianSecurityDialog) {
                GuardianSecurityDialog(
                    targetRole = securityTargetRole,
                    onDismiss = { showGuardianSecurityDialog = false },
                    onSuccess = {
                        showGuardianSecurityDialog = false
                        if (securityTargetRole == "Teacher / Principal") {
                            viewModel.navigateTo(AppScreen.TeacherDashboard)
                        } else {
                            viewModel.navigateTo(AppScreen.ParentPortal)
                        }
                    }
                )
            }

            // Student profile switcher modal
            if (showStudentSwitcherDialog) {
                StudentSwitcherDialog(
                    students = allStudents,
                    currentStudentId = currentStudent.id,
                    onSelectStudent = { selected ->
                        viewModel.selectStudent(selected)
                        showStudentSwitcherDialog = false
                    },
                    onDismiss = { showStudentSwitcherDialog = false }
                )
            }

            // Celebration Modal Dialog
            if (celebration.isShowing) {
                CelebrationDialog(
                    starsWon = celebration.stars,
                    title = celebration.title,
                    message = celebration.message,
                    onContinue = { viewModel.dismissCelebration() }
                )
            }
        }
    }
}

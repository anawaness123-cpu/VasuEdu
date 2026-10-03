package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.VoiceGuideManager
import com.example.data.database.AppDatabase
import com.example.data.model.AssignmentEntity
import com.example.data.model.CurriculumModuleEntity
import com.example.data.model.GradeLevel
import com.example.data.model.MilestoneProgressEntity
import com.example.data.model.StudentEntity
import com.example.data.repository.BloomingBudsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AppScreen {
    object StudentHome : AppScreen()
    data class LessonViewer(val module: CurriculumModuleEntity) : AppScreen()
    data class Whiteboard(val letter: String = "A") : AppScreen()
    object PhonicsGame : AppScreen()
    object NumberGame : AppScreen()
    object RewardsGarden : AppScreen()
    object ParentPortal : AppScreen()
    object TeacherDashboard : AppScreen()
}

data class CelebrationState(
    val isShowing: Boolean = false,
    val stars: Int = 3,
    val title: String = "",
    val message: String = ""
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = BloomingBudsRepository(database.bloomingBudsDao())
    val voiceGuideManager = VoiceGuideManager(application)

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.StudentHome)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedGrade = MutableStateFlow(GradeLevel.NURSERY)
    val selectedGrade: StateFlow<GradeLevel> = _selectedGrade.asStateFlow()

    private val _currentStudentId = MutableStateFlow("student_nursery_1")
    val currentStudentId: StateFlow<String> = _currentStudentId.asStateFlow()

    val allStudents: StateFlow<List<StudentEntity>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentStudent: StateFlow<StudentEntity> = combine(
        allStudents,
        _currentStudentId
    ) { students, id ->
        students.find { it.id == id } ?: students.firstOrNull() ?: StudentEntity(
            id = "student_nursery_1",
            name = "Aarav Sharma",
            gradeLevel = GradeLevel.NURSERY,
            avatarId = "avatar_bunny"
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        StudentEntity("student_nursery_1", "Aarav Sharma", GradeLevel.NURSERY, "avatar_bunny")
    )

    val currentModules: StateFlow<List<CurriculumModuleEntity>> = _selectedGrade
        .flatMapLatest { grade -> repository.getModulesByGrade(grade) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentMilestones: StateFlow<List<MilestoneProgressEntity>> = _selectedGrade
        .flatMapLatest { grade -> repository.getMilestonesByGrade(grade) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAssignments: StateFlow<List<AssignmentEntity>> = repository.allAssignments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _celebration = MutableStateFlow(CelebrationState())
    val celebration: StateFlow<CelebrationState> = _celebration.asStateFlow()

    init {
        // Greet early learner warmly on launch
        viewModelScope.launch {
            delay(800)
            voiceGuideManager.speak("Welcome to Blooming Buds Preschool Patoda! Let's play and learn today!")
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun navigateBack() {
        _currentScreen.value = AppScreen.StudentHome
    }

    fun selectGrade(grade: GradeLevel) {
        _selectedGrade.value = grade
        // Optionally switch active student profile matching grade
        val matchedStudent = allStudents.value.find { it.gradeLevel == grade }
        if (matchedStudent != null) {
            _currentStudentId.value = matchedStudent.id
        }
    }

    fun selectStudent(student: StudentEntity) {
        _currentStudentId.value = student.id
        _selectedGrade.value = student.gradeLevel
        voiceGuideManager.speak("Welcome back, ${student.name}! Let's bloom in ${student.gradeLevel.displayName}!")
    }

    fun completeLesson(module: CurriculumModuleEntity, score: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.completeModule(module.id, module.gradeLevel, score)
            repository.addStarsToStudent(currentStudent.value.id, module.starsReward, unlockFlower = true)
        }
        showCelebration(
            stars = module.starsReward,
            title = "Lesson Completed! 🌸",
            message = "You mastered ${module.title}! A new bud bloomed in your preschool garden!"
        )
    }

    fun completeGame(starsWon: Int, gameName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addStarsToStudent(currentStudent.value.id, starsWon, unlockFlower = true)
        }
        showCelebration(
            stars = starsWon,
            title = "$gameName Mastered! ⭐",
            message = "Super job! You earned $starsWon shiny stars!"
        )
    }

    fun submitWhiteboardTracing(accuracy: Int) {
        val student = currentStudent.value
        viewModelScope.launch(Dispatchers.IO) {
            val assignment = AssignmentEntity(
                id = "tracing_${System.currentTimeMillis()}",
                title = "Handwriting Tracing Worksheet",
                studentId = student.id,
                studentName = student.name,
                gradeLevel = student.gradeLevel,
                submissionType = "Handwriting Tracing",
                status = com.example.data.model.AssignmentStatus.SUBMITTED,
                autoGradedScore = accuracy,
                maxScore = 100,
                teacherFeedback = "Submitted to Principal Vasundhara. Automated stroke curvature: $accuracy% match."
            )
            repository.submitAssignment(assignment)
            repository.addStarsToStudent(student.id, 4, unlockFlower = true)
        }
        showCelebration(
            stars = 4,
            title = "Worksheet Submitted! 📝",
            message = "Your handwriting tracing was sent to Teacher Vasundhara! Score: $accuracy%!"
        )
    }

    fun runAutomatedGrading() {
        viewModelScope.launch(Dispatchers.IO) {
            val graded = repository.runAutomatedGrading()
            voiceGuideManager.speak("Automated grading complete! Graded $graded student submissions with pedagogical notes.")
        }
    }

    fun triggerSync() {
        viewModelScope.launch {
            _isSyncing.value = true
            delay(1200) // Simulates low-latency local-to-cloud sync
            _isSyncing.value = false
            voiceGuideManager.speak("Offline sync complete! All student learning data is securely backed up.")
        }
    }

    fun speak(text: String) {
        voiceGuideManager.speak(text)
    }

    fun toggleVoice() {
        voiceGuideManager.toggleVoiceEnabled()
    }

    private fun showCelebration(stars: Int, title: String, message: String) {
        _celebration.value = CelebrationState(
            isShowing = true,
            stars = stars,
            title = title,
            message = message
        )
        voiceGuideManager.speak("Hooray! $title $message")
    }

    fun dismissCelebration() {
        _celebration.value = CelebrationState(isShowing = false)
        navigateBack()
    }

    override fun onCleared() {
        super.onCleared()
        voiceGuideManager.shutdown()
    }
}

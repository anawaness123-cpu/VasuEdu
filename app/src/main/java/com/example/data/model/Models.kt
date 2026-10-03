package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GradeLevel(val displayName: String, val ageGroup: String, val description: String) {
    NURSERY("Nursery", "Ages 2.5 - 3.5", "Alphabet sounds, early counting 1-5, colors & rhymes"),
    LKG("LKG", "Ages 3.5 - 4.5", "Letter tracing, phonics blends, counting 1-10 & shapes"),
    UKG("UKG", "Ages 4.5 - 5.5", "Sight words, CVC reading, early math & story comprehension")
}

enum class ModuleCategory(val displayName: String, val colorHex: Long) {
    LITERACY("Early Literacy", 0xFFFF5722),
    NUMERACY("Numbers & Math", 0xFF2196F3),
    SHAPES_COLORS("Shapes & Colors", 0xFF9C27B0),
    RHYMES_STORIES("Rhymes & Tales", 0xFF4CAF50),
    WHITEBOARD_TRACING("Handwriting Tracing", 0xFFFF9800)
}

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val gradeLevel: GradeLevel,
    val avatarId: String,
    val starsEarned: Int = 15,
    val flowersUnlocked: Int = 3,
    val streakDays: Int = 4,
    val guardianName: String = "Parent / Guardian",
    val guardianContact: String = "9881127065",
    val totalTimeMinutes: Int = 145,
    val favoriteSubject: String = "Literacy & Phonics"
)

@Entity(tableName = "curriculum_modules")
data class CurriculumModuleEntity(
    @PrimaryKey val id: String,
    val gradeLevel: GradeLevel,
    val category: ModuleCategory,
    val title: String,
    val subtitle: String,
    val iconKey: String,
    val voiceInstruction: String,
    val isCompleted: Boolean = false,
    val scorePercent: Int = 0,
    val starsReward: Int = 3,
    val lessonType: String = "LESSON" // LESSON, WHITEBOARD, GAME
)

enum class AssignmentStatus {
    PENDING,
    SUBMITTED,
    GRADED
}

@Entity(tableName = "assignments")
data class AssignmentEntity(
    @PrimaryKey val id: String,
    val title: String,
    val studentId: String,
    val studentName: String,
    val gradeLevel: GradeLevel,
    val submissionType: String, // Tracing, Phonics Quiz, Counting
    val status: AssignmentStatus,
    val autoGradedScore: Int,
    val maxScore: Int = 100,
    val teacherFeedback: String,
    val submittedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "milestones")
data class MilestoneProgressEntity(
    @PrimaryKey val id: String,
    val gradeLevel: GradeLevel,
    val studentId: String,
    val category: String,
    val title: String,
    val isMastered: Boolean,
    val progressPercent: Int
)

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean
)

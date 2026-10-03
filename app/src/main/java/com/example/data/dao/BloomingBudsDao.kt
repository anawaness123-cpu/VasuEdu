package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AssignmentEntity
import com.example.data.model.CurriculumModuleEntity
import com.example.data.model.GradeLevel
import com.example.data.model.MilestoneProgressEntity
import com.example.data.model.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BloomingBudsDao {

    // Students
    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :id")
    fun getStudentById(id: String): Flow<StudentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>)

    @Update
    suspend fun updateStudent(student: StudentEntity)

    // Curriculum Modules
    @Query("SELECT * FROM curriculum_modules WHERE gradeLevel = :grade")
    fun getModulesByGrade(grade: GradeLevel): Flow<List<CurriculumModuleEntity>>

    @Query("SELECT * FROM curriculum_modules")
    fun getAllModules(): Flow<List<CurriculumModuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModules(modules: List<CurriculumModuleEntity>)

    @Update
    suspend fun updateModule(module: CurriculumModuleEntity)

    // Assignments
    @Query("SELECT * FROM assignments ORDER BY submittedTimestamp DESC")
    fun getAllAssignments(): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE gradeLevel = :grade ORDER BY submittedTimestamp DESC")
    fun getAssignmentsByGrade(grade: GradeLevel): Flow<List<AssignmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: AssignmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignments(assignments: List<AssignmentEntity>)

    @Update
    suspend fun updateAssignment(assignment: AssignmentEntity)

    // Milestones
    @Query("SELECT * FROM milestones WHERE gradeLevel = :grade")
    fun getMilestonesByGrade(grade: GradeLevel): Flow<List<MilestoneProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<MilestoneProgressEntity>)

    @Update
    suspend fun updateMilestone(milestone: MilestoneProgressEntity)
}

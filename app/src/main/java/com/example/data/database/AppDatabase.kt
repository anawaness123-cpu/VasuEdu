package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BloomingBudsDao
import com.example.data.model.AssignmentEntity
import com.example.data.model.AssignmentStatus
import com.example.data.model.CurriculumModuleEntity
import com.example.data.model.GradeLevel
import com.example.data.model.MilestoneProgressEntity
import com.example.data.model.ModuleCategory
import com.example.data.model.StudentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        StudentEntity::class,
        CurriculumModuleEntity::class,
        AssignmentEntity::class,
        MilestoneProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bloomingBudsDao(): BloomingBudsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "blooming_buds_preschool_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.bloomingBudsDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(dao: BloomingBudsDao) {
            // Seed Students
            val students = listOf(
                StudentEntity(
                    id = "student_nursery_1",
                    name = "Aarav Sharma",
                    gradeLevel = GradeLevel.NURSERY,
                    avatarId = "avatar_bunny",
                    starsEarned = 18,
                    flowersUnlocked = 4,
                    streakDays = 5,
                    guardianName = "Pooja Sharma",
                    guardianContact = "9881127065",
                    totalTimeMinutes = 120,
                    favoriteSubject = "Alphabet Phonics"
                ),
                StudentEntity(
                    id = "student_lkg_1",
                    name = "Anaya Patil",
                    gradeLevel = GradeLevel.LKG,
                    avatarId = "avatar_bear",
                    starsEarned = 26,
                    flowersUnlocked = 6,
                    streakDays = 7,
                    guardianName = "Rahul Patil",
                    guardianContact = "9881127065",
                    totalTimeMinutes = 195,
                    favoriteSubject = "Letter Tracing Whiteboard"
                ),
                StudentEntity(
                    id = "student_ukg_1",
                    name = "Vihaan Kulkarni",
                    gradeLevel = GradeLevel.UKG,
                    avatarId = "avatar_lion",
                    starsEarned = 34,
                    flowersUnlocked = 9,
                    streakDays = 12,
                    guardianName = "Sneha Kulkarni",
                    guardianContact = "9881127065",
                    totalTimeMinutes = 280,
                    favoriteSubject = "Sight Words & Math"
                )
            )
            dao.insertStudents(students)

            // Seed Curriculum Modules
            val modules = listOf(
                // NURSERY
                CurriculumModuleEntity(
                    id = "mod_nur_1",
                    gradeLevel = GradeLevel.NURSERY,
                    category = ModuleCategory.LITERACY,
                    title = "Phonics Fun: A to E",
                    subtitle = "Learn joyful letter sounds with animal buddies",
                    iconKey = "alphabet",
                    voiceInstruction = "Tap the letter to hear its phonics sound! A says /a/ as in Apple!",
                    isCompleted = true,
                    scorePercent = 100,
                    starsReward = 3,
                    lessonType = "LESSON"
                ),
                CurriculumModuleEntity(
                    id = "mod_nur_2",
                    gradeLevel = GradeLevel.NURSERY,
                    category = ModuleCategory.NUMERACY,
                    title = "Count Buds: 1 to 5",
                    subtitle = "Count blooming flowers in our preschool garden",
                    iconKey = "numbers",
                    voiceInstruction = "Let's count the flowers together: One, two, three, four, five!",
                    isCompleted = true,
                    scorePercent = 90,
                    starsReward = 3,
                    lessonType = "GAME"
                ),
                CurriculumModuleEntity(
                    id = "mod_nur_3",
                    gradeLevel = GradeLevel.NURSERY,
                    category = ModuleCategory.SHAPES_COLORS,
                    title = "Rainbow Colors & Shapes",
                    subtitle = "Spot the red circles and blue stars",
                    iconKey = "shapes",
                    voiceInstruction = "Can you find the bright yellow sun and round circle?",
                    isCompleted = false,
                    scorePercent = 0,
                    starsReward = 3,
                    lessonType = "GAME"
                ),
                CurriculumModuleEntity(
                    id = "mod_nur_4",
                    gradeLevel = GradeLevel.NURSERY,
                    category = ModuleCategory.WHITEBOARD_TRACING,
                    title = "Baby Stroke Whiteboard",
                    subtitle = "Draw lines and swirls with colorful chalk",
                    iconKey = "draw",
                    voiceInstruction = "Use your finger to trace straight and wavy lines on the whiteboard!",
                    isCompleted = false,
                    scorePercent = 0,
                    starsReward = 3,
                    lessonType = "WHITEBOARD"
                ),

                // LKG
                CurriculumModuleEntity(
                    id = "mod_lkg_1",
                    gradeLevel = GradeLevel.LKG,
                    category = ModuleCategory.LITERACY,
                    title = "Phonics Blends: Short Vowels",
                    subtitle = "Discover word sounds: -at, -an, -in",
                    iconKey = "alphabet",
                    voiceInstruction = "Listen closely: c plus at makes cat! Let's blend sounds!",
                    isCompleted = true,
                    scorePercent = 95,
                    starsReward = 3,
                    lessonType = "LESSON"
                ),
                CurriculumModuleEntity(
                    id = "mod_lkg_2",
                    gradeLevel = GradeLevel.LKG,
                    category = ModuleCategory.WHITEBOARD_TRACING,
                    title = "Letter Tracing Whiteboard",
                    subtitle = "Trace capital and small letters with crayon",
                    iconKey = "draw",
                    voiceInstruction = "Trace the letter A step by step: start at the top and slide down!",
                    isCompleted = true,
                    scorePercent = 88,
                    starsReward = 3,
                    lessonType = "WHITEBOARD"
                ),
                CurriculumModuleEntity(
                    id = "mod_lkg_3",
                    gradeLevel = GradeLevel.LKG,
                    category = ModuleCategory.NUMERACY,
                    title = "Numbers & Counting: 1 to 10",
                    subtitle = "Count friendly butterflies and match numbers",
                    iconKey = "numbers",
                    voiceInstruction = "How many butterflies do you see? Tap the right number!",
                    isCompleted = false,
                    scorePercent = 0,
                    starsReward = 3,
                    lessonType = "GAME"
                ),
                CurriculumModuleEntity(
                    id = "mod_lkg_4",
                    gradeLevel = GradeLevel.LKG,
                    category = ModuleCategory.SHAPES_COLORS,
                    title = "Shape Sorting Fiesta",
                    subtitle = "Sort triangles, rectangles, and diamonds",
                    iconKey = "shapes",
                    voiceInstruction = "Sort the shapes into their matching preschool baskets!",
                    isCompleted = false,
                    scorePercent = 0,
                    starsReward = 3,
                    lessonType = "GAME"
                ),

                // UKG
                CurriculumModuleEntity(
                    id = "mod_ukg_1",
                    gradeLevel = GradeLevel.UKG,
                    category = ModuleCategory.LITERACY,
                    title = "Sight Words Explorer",
                    subtitle = "Read 3-letter CVC words and simple sentences",
                    iconKey = "alphabet",
                    voiceInstruction = "Sight words help us read stories! Let's read: The cat sat on the mat!",
                    isCompleted = true,
                    scorePercent = 98,
                    starsReward = 4,
                    lessonType = "LESSON"
                ),
                CurriculumModuleEntity(
                    id = "mod_ukg_2",
                    gradeLevel = GradeLevel.UKG,
                    category = ModuleCategory.NUMERACY,
                    title = "Buds Math: Early Addition",
                    subtitle = "Add colorful apples and flowers up to 10",
                    iconKey = "numbers",
                    voiceInstruction = "If we have two apples and pick two more, how many do we have? Four!",
                    isCompleted = true,
                    scorePercent = 92,
                    starsReward = 4,
                    lessonType = "GAME"
                ),
                CurriculumModuleEntity(
                    id = "mod_ukg_3",
                    gradeLevel = GradeLevel.UKG,
                    category = ModuleCategory.WHITEBOARD_TRACING,
                    title = "Word Tracing Whiteboard",
                    subtitle = "Write short words C-A-T, S-U-N with guidance",
                    iconKey = "draw",
                    voiceInstruction = "Write the word SUN on the board and decorate it with your favorite colors!",
                    isCompleted = false,
                    scorePercent = 0,
                    starsReward = 4,
                    lessonType = "WHITEBOARD"
                ),
                CurriculumModuleEntity(
                    id = "mod_ukg_4",
                    gradeLevel = GradeLevel.UKG,
                    category = ModuleCategory.RHYMES_STORIES,
                    title = "Moral Story & Comprehension",
                    subtitle = "The Helpful Sprout of Patoda Garden",
                    iconKey = "story",
                    voiceInstruction = "Listen to the sweet story of the Little Sprout and answer quick questions!",
                    isCompleted = false,
                    scorePercent = 0,
                    starsReward = 4,
                    lessonType = "LESSON"
                )
            )
            dao.insertModules(modules)

            // Seed Assignments
            val assignments = listOf(
                AssignmentEntity(
                    id = "asg_1",
                    title = "Alphabet Tracing Sheet - Letter A & B",
                    studentId = "student_lkg_1",
                    studentName = "Anaya Patil",
                    gradeLevel = GradeLevel.LKG,
                    submissionType = "Handwriting Tracing",
                    status = AssignmentStatus.GRADED,
                    autoGradedScore = 92,
                    maxScore = 100,
                    teacherFeedback = "Excellent line curvature and pencil grip! Mastered stroke order."
                ),
                AssignmentEntity(
                    id = "asg_2",
                    title = "Preschool Count & Match 1-5",
                    studentId = "student_nursery_1",
                    studentName = "Aarav Sharma",
                    gradeLevel = GradeLevel.NURSERY,
                    submissionType = "Counting Game",
                    status = AssignmentStatus.SUBMITTED,
                    autoGradedScore = 88,
                    maxScore = 100,
                    teacherFeedback = "Pending teacher review. Automated accuracy: 88%."
                ),
                AssignmentEntity(
                    id = "asg_3",
                    title = "Sight Words CVC Worksheet (Cat, Dog, Sun)",
                    studentId = "student_ukg_1",
                    studentName = "Vihaan Kulkarni",
                    gradeLevel = GradeLevel.UKG,
                    submissionType = "Word Builder Quiz",
                    status = AssignmentStatus.SUBMITTED,
                    autoGradedScore = 95,
                    maxScore = 100,
                    teacherFeedback = "Automated grading completed. Outstanding vocabulary retention."
                ),
                AssignmentEntity(
                    id = "asg_4",
                    title = "Color & Shape Sorting Challenge",
                    studentId = "student_nursery_1",
                    studentName = "Aarav Sharma",
                    gradeLevel = GradeLevel.NURSERY,
                    submissionType = "Shape Sorter",
                    status = AssignmentStatus.PENDING,
                    autoGradedScore = 0,
                    maxScore = 100,
                    teacherFeedback = "Assignment assigned. Ready for student attempt."
                )
            )
            dao.insertAssignments(assignments)

            // Seed Milestones
            val milestones = listOf(
                // Nursery
                MilestoneProgressEntity("m_nur_1", GradeLevel.NURSERY, "student_nursery_1", "Early Literacy", "Recognizes Letters A - M", true, 100),
                MilestoneProgressEntity("m_nur_2", GradeLevel.NURSERY, "student_nursery_1", "Numeracy", "Counts items up to 5", true, 90),
                MilestoneProgressEntity("m_nur_3", GradeLevel.NURSERY, "student_nursery_1", "Cognitive", "Identifies Primary Colors (Red, Blue, Yellow)", true, 100),
                MilestoneProgressEntity("m_nur_4", GradeLevel.NURSERY, "student_nursery_1", "Fine Motor", "Finger Tracing Curved Lines", false, 65),

                // LKG
                MilestoneProgressEntity("m_lkg_1", GradeLevel.LKG, "student_lkg_1", "Early Literacy", "Upper & Lowercase Matching A-Z", true, 100),
                MilestoneProgressEntity("m_lkg_2", GradeLevel.LKG, "student_lkg_1", "Phonics", "Short Vowel Sounds (-at, -op, -en)", true, 95),
                MilestoneProgressEntity("m_lkg_3", GradeLevel.LKG, "student_lkg_1", "Fine Motor", "Whiteboard Tracing Within Guide Lines", true, 88),
                MilestoneProgressEntity("m_lkg_4", GradeLevel.LKG, "student_lkg_1", "Numeracy", "Quantities & Counting up to 10", false, 70),

                // UKG
                MilestoneProgressEntity("m_ukg_1", GradeLevel.UKG, "student_ukg_1", "Reading", "CVC Word Reading & Sight Words", true, 98),
                MilestoneProgressEntity("m_ukg_2", GradeLevel.UKG, "student_ukg_1", "Math", "Picture Addition & Subtraction (1-10)", true, 92),
                MilestoneProgressEntity("m_ukg_3", GradeLevel.UKG, "student_ukg_1", "Comprehension", "Story Recall & Sequencing", false, 75),
                MilestoneProgressEntity("m_ukg_4", GradeLevel.UKG, "student_ukg_1", "Fine Motor", "Independent 3-Letter Word Writing", true, 90)
            )
            dao.insertMilestones(milestones)
        }
    }
}

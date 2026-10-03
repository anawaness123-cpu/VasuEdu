package com.example.data.database

import androidx.room.TypeConverter
import com.example.data.model.AssignmentStatus
import com.example.data.model.GradeLevel
import com.example.data.model.ModuleCategory

class Converters {
    @TypeConverter
    fun fromGradeLevel(value: GradeLevel): String = value.name

    @TypeConverter
    fun toGradeLevel(value: String): GradeLevel = try {
        GradeLevel.valueOf(value)
    } catch (_: Exception) {
        GradeLevel.NURSERY
    }

    @TypeConverter
    fun fromModuleCategory(value: ModuleCategory): String = value.name

    @TypeConverter
    fun toModuleCategory(value: String): ModuleCategory = try {
        ModuleCategory.valueOf(value)
    } catch (_: Exception) {
        ModuleCategory.LITERACY
    }

    @TypeConverter
    fun fromAssignmentStatus(value: AssignmentStatus): String = value.name

    @TypeConverter
    fun toAssignmentStatus(value: String): AssignmentStatus = try {
        AssignmentStatus.valueOf(value)
    } catch (_: Exception) {
        AssignmentStatus.PENDING
    }
}

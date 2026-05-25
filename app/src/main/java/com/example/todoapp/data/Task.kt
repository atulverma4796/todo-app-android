package com.example.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Single task row in the Room database.
 *
 * Each task has a title (required), an optional description, a priority
 * level (LOW/MEDIUM/HIGH), an optional due-date timestamp, a completed
 * flag, and a created-at timestamp used for default ordering.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,           // epoch millis; null when no due date
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class Priority { LOW, MEDIUM, HIGH }

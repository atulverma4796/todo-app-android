package com.example.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * Type converter so Room can persist the Priority enum as a TEXT column
 * instead of requiring a separate table for it.
 */
class Converters {
    @TypeConverter
    fun fromPriority(p: Priority): String = p.name

    @TypeConverter
    fun toPriority(name: String): Priority = Priority.valueOf(name)
}

/**
 * The Room database. Schema version 1 — increment + add Migration objects
 * when changing columns.
 *
 * exportSchema = true so the JSON schema is checked into the project
 * (good practice for future migrations).
 */
@Database(entities = [Task::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "todo-database"
                )
                    .fallbackToDestructiveMigration() // OK for a college project; for prod, write real Migration objects
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

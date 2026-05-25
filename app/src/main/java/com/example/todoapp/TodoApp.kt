package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.TaskDatabase
import com.example.todoapp.data.TaskRepository

/**
 * Application class — holds the lazily-initialised database and
 * repository so they live for the entire app lifetime instead of
 * being recreated per activity.
 */
class TodoApp : Application() {
    val database by lazy { TaskDatabase.getInstance(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }
}

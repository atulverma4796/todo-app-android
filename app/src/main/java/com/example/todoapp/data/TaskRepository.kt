package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository — the single source of truth for task data.
 *
 * In a more complex app this would also pull from a network API and
 * decide whether to serve cached or fresh data. For this app it's a
 * thin pass-through to the DAO, but the abstraction keeps the
 * ViewModel free of Room-specific code.
 */
class TaskRepository(private val dao: TaskDao) {

    fun allTasks(): Flow<List<Task>> = dao.getAllTasks()
    fun activeTasks(): Flow<List<Task>> = dao.getActiveTasks()
    fun completedTasks(): Flow<List<Task>> = dao.getCompletedTasks()

    suspend fun insert(task: Task) = dao.insert(task)
    suspend fun update(task: Task) = dao.update(task)
    suspend fun delete(task: Task) = dao.delete(task)
    suspend fun deleteAllCompleted() = dao.deleteAllCompleted()
}

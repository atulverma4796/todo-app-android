package com.example.todoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

enum class TaskFilter { ALL, ACTIVE, COMPLETED }

/**
 * ViewModel — survives configuration changes (rotation, etc.) and
 * is the only place the UI talks to the repository.
 *
 * Holds the current filter as LiveData; whenever the filter changes
 * we switch to the corresponding Flow from the repository.
 */
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val filter = MutableLiveData(TaskFilter.ALL)

    val tasks: LiveData<List<Task>> = filter.switchMap { f ->
        when (f) {
            TaskFilter.ALL -> repository.allTasks()
            TaskFilter.ACTIVE -> repository.activeTasks()
            TaskFilter.COMPLETED -> repository.completedTasks()
        }.asLiveData()
    }

    fun setFilter(f: TaskFilter) {
        filter.value = f
    }

    fun addTask(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun toggleCompleted(task: Task) = viewModelScope.launch {
        repository.update(task.copy(isCompleted = !task.isCompleted))
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun clearCompleted() = viewModelScope.launch {
        repository.deleteAllCompleted()
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

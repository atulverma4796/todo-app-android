package com.example.todoapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.TodoApp
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

/**
 * The only screen. Hosts:
 *  - top app bar with "clear completed" overflow action
 *  - tab strip (All / Active / Completed) that drives the ViewModel filter
 *  - RecyclerView of tasks
 *  - empty-state view shown when the list is empty
 *  - FAB to add a new task
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TodoApp).repository)
    }

    private val adapter by lazy {
        TaskAdapter(
            onToggleComplete = { task -> viewModel.toggleCompleted(task) },
            onEdit = { task -> openAddEditDialog(task) },
            onDelete = { task -> confirmDelete(task) },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecycler()
        setupTabs()
        setupFab()
        observeTasks()
    }

    private fun setupRecycler() {
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = adapter
    }

    private fun setupTabs() {
        binding.filterTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.setFilter(
                    when (tab?.position) {
                        1 -> TaskFilter.ACTIVE
                        2 -> TaskFilter.COMPLETED
                        else -> TaskFilter.ALL
                    }
                )
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupFab() {
        binding.addTaskFab.setOnClickListener { openAddEditDialog(null) }
    }

    private fun observeTasks() {
        viewModel.tasks.observe(this) { list ->
            adapter.submitList(list)
            binding.emptyStateView.visibility =
                if (list.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    private fun openAddEditDialog(task: Task?) {
        val dialog = AddEditTaskDialog.newInstance(task)
        dialog.setOnSaveListener { saved ->
            if (task == null) viewModel.addTask(saved) else viewModel.updateTask(saved)
        }
        dialog.show(supportFragmentManager, "AddEditTask")
    }

    private fun confirmDelete(task: Task) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_task)
            .setMessage(getString(R.string.delete_confirm, task.title))
            .setPositiveButton(R.string.delete) { _, _ -> viewModel.deleteTask(task) }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_completed -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.clear_completed)
                    .setMessage(R.string.clear_completed_confirm)
                    .setPositiveButton(R.string.clear) { _, _ -> viewModel.clearCompleted() }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

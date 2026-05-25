package com.example.todoapp.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.Priority
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * RecyclerView Adapter for tasks.
 *
 * Uses ListAdapter + DiffUtil so the RecyclerView only redraws rows
 * that actually changed when the underlying list updates — smoother
 * UX than notifyDataSetChanged().
 */
class TaskAdapter(
    private val onToggleComplete: (Task) -> Unit,
    private val onEdit: (Task) -> Unit,
    private val onDelete: (Task) -> Unit,
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.titleTextView.text = task.title

            if (task.description.isBlank()) {
                binding.descriptionTextView.visibility = android.view.View.GONE
            } else {
                binding.descriptionTextView.visibility = android.view.View.VISIBLE
                binding.descriptionTextView.text = task.description
            }

            // Strike through completed task titles.
            binding.titleTextView.paintFlags = if (task.isCompleted) {
                binding.titleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.titleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // Detach + reattach listener around setChecked to avoid the
            // listener firing during programmatic state restore.
            binding.completeCheckBox.setOnCheckedChangeListener(null)
            binding.completeCheckBox.isChecked = task.isCompleted
            binding.completeCheckBox.setOnCheckedChangeListener { _, _ -> onToggleComplete(task) }

            // Priority indicator — colour the left strip.
            val priorityColour = when (task.priority) {
                Priority.HIGH -> R.color.priority_high
                Priority.MEDIUM -> R.color.priority_medium
                Priority.LOW -> R.color.priority_low
            }
            binding.priorityIndicator.setBackgroundResource(priorityColour)

            // Due date — show only if set.
            if (task.dueDate != null) {
                binding.dueDateTextView.visibility = android.view.View.VISIBLE
                val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                binding.dueDateTextView.text = binding.root.context.getString(
                    R.string.due_label, fmt.format(Date(task.dueDate))
                )
            } else {
                binding.dueDateTextView.visibility = android.view.View.GONE
            }

            binding.editButton.setOnClickListener { onEdit(task) }
            binding.deleteButton.setOnClickListener { onDelete(task) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
        }
    }
}

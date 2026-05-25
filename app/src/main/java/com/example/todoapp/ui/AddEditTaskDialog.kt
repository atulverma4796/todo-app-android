package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.example.todoapp.R
import com.example.todoapp.data.Priority
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.DialogAddTaskBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * One dialog handles both Add and Edit. Pass an existing Task into the
 * factory to edit; pass null (or call newInstance()) to add a new task.
 *
 * Result is delivered via the onSave callback set by the caller.
 */
class AddEditTaskDialog : DialogFragment() {

    private var existingTask: Task? = null
    private var onSave: ((Task) -> Unit)? = null
    private var selectedDueDate: Long? = null

    fun setOnSaveListener(listener: (Task) -> Unit) {
        onSave = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddTaskBinding.inflate(layoutInflater)

        // Pre-fill when editing.
        existingTask?.let { task ->
            binding.titleEditText.setText(task.title)
            binding.descriptionEditText.setText(task.description)
            when (task.priority) {
                Priority.LOW -> binding.priorityLowRadio.isChecked = true
                Priority.MEDIUM -> binding.priorityMediumRadio.isChecked = true
                Priority.HIGH -> binding.priorityHighRadio.isChecked = true
            }
            selectedDueDate = task.dueDate
            updateDueDateLabel(binding, task.dueDate)
        }

        binding.dueDateButton.setOnClickListener {
            val cal = Calendar.getInstance().apply {
                selectedDueDate?.let { timeInMillis = it }
            }
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val pickCal = Calendar.getInstance().apply {
                        set(year, month, day, 23, 59, 59)
                        set(Calendar.MILLISECOND, 0)
                    }
                    selectedDueDate = pickCal.timeInMillis
                    updateDueDateLabel(binding, selectedDueDate)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.clearDueDateButton.setOnClickListener {
            selectedDueDate = null
            updateDueDateLabel(binding, null)
        }

        val titleRes = if (existingTask == null) R.string.add_task else R.string.edit_task

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleRes)
            .setView(binding.root)
            .setPositiveButton(R.string.save) { _, _ ->
                val title = binding.titleEditText.text?.toString()?.trim().orEmpty()
                if (title.isEmpty()) return@setPositiveButton // could surface a toast/error here

                val priority = when (binding.priorityRadioGroup.checkedRadioButtonId) {
                    R.id.priorityLowRadio -> Priority.LOW
                    R.id.priorityHighRadio -> Priority.HIGH
                    else -> Priority.MEDIUM
                }

                val savedTask = (existingTask ?: Task(title = title)).copy(
                    title = title,
                    description = binding.descriptionEditText.text?.toString()?.trim().orEmpty(),
                    priority = priority,
                    dueDate = selectedDueDate,
                )
                onSave?.invoke(savedTask)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun updateDueDateLabel(binding: DialogAddTaskBinding, millis: Long?) {
        if (millis == null) {
            binding.dueDateLabel.text = getString(R.string.no_due_date)
            binding.clearDueDateButton.visibility = android.view.View.GONE
        } else {
            val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            binding.dueDateLabel.text = getString(R.string.due_label, fmt.format(Date(millis)))
            binding.clearDueDateButton.visibility = android.view.View.VISIBLE
        }
    }

    companion object {
        fun newInstance(task: Task? = null): AddEditTaskDialog {
            return AddEditTaskDialog().apply { existingTask = task }
        }
    }
}

@Suppress("unused")
private fun radio(id: Int): RadioButton? = null // tiny helper kept for symmetry; not used

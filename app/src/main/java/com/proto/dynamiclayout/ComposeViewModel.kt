package com.proto.dynamiclayout

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class ComposeViewModel : ViewModel() {
    private val _tasks = getWellnessTasks().toMutableStateList()
    val tasks: List<WellnessTask>
        get() = _tasks


    fun remove(item: WellnessTask) {
        _tasks.remove(item)
    }

    fun changeTaskChecked(item: WellnessTask, checked: Boolean) =
        tasks.find { it.id == item.id }?.let { task ->
            val idx = tasks.indexOf(task)
            _tasks[idx] = task.copy(checked = checked)
        }


}


data class WellnessTask(val id: Int, val label: String, val checked: Boolean = false)

private fun getWellnessTasks() = List(200) { i -> WellnessTask(i, "Task # $i") }
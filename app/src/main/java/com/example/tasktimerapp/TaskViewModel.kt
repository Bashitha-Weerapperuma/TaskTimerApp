package com.example.tasktimerapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tasktimerapp.room.Data
import com.example.tasktimerapp.room.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val connection = TaskDatabase.getInstance(application).taskDao()

    fun getAllTasks(): LiveData<List<Data>> {
        return connection.getAllTasks()
    }

    fun getTask(givenPk: Int): Data {
        return connection.getTask(givenPk)
    }

    fun addNewTask(task: Data) {
        CoroutineScope(IO).launch {
            connection.addNewTask(task)
        }
    }

    fun updateTask(task: Data) {
        CoroutineScope(IO).launch {
            connection.updateTask(task)
        }
    }

    fun updateTaskTime(taskTime: Long, givenPk: Int) {
        CoroutineScope(IO).launch {
            connection.updateTaskTime(taskTime, givenPk)
        }
    }

    fun updateTaskStatus(status: Boolean, givenPk: Int) {
        CoroutineScope(IO).launch {
            connection.updateTaskStatus(status, givenPk)
        }
    }

    fun deleteTask(task: Data) {
        CoroutineScope(IO).launch {
            connection.deleteTask(task)
        }
    }
}
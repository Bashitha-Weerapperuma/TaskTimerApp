package com.example.tasktimerapp.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM Tasks ORDER BY priority")
    fun getAllTasks(): LiveData<List<Data>>

    @Query("SELECT * FROM Tasks WHERE pk = :givenPk")
    fun getTask(givenPk: Int): Data

    @Query("UPDATE Tasks SET taskTime = :taskTime WHERE pk = :givenPk")
    fun updateTaskTime(taskTime: Long, givenPk: Int)

    @Query("UPDATE Tasks SET isRunning = :status WHERE pk = :givenPk")
    fun updateTaskStatus(status: Boolean, givenPk: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewTask(task: Data)

    @Delete
    fun deleteTask(task: Data)

    @Update
    fun updateTask(task: Data)
}
package com.example.tasktimerapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class Data(
    @PrimaryKey(autoGenerate = true) val pk: Int,
    val taskName: String,
    val taskDescription: String,
    val taskTime: Long,
    val priority: String,
    val isRunning: Boolean = false
)

package com.example.tasktimerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimerapp.room.Data
import com.example.tasktimerapp.rv_adapter.RVMainAdapter
import com.example.tasktimerapp.rv_adapter.SwipeTasks
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.Dispatchers.IO

class MainActivity : AppCompatActivity() {

    private val taskViewModel by lazy { ViewModelProvider(this).get(TaskViewModel::class.java) }
    private lateinit var adapter: RVMainAdapter

    private lateinit var rvItem: RecyclerView
    private lateinit var taskName: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var timer: Chronometer
    private lateinit var reset: TextView
    private lateinit var addButton: ImageView
    private lateinit var showAllButton: ImageView

    private lateinit var tasksList: ArrayList<Data>
    private var running = false
    private var pauseOffset: Long = 0
    private var previousPosition = -1
    private var savedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvItem = findViewById(R.id.rvItems)
        taskName = findViewById(R.id.taskName)
        progressBar = findViewById(R.id.progressBar)
        timer = findViewById(R.id.timer)
        reset = findViewById(R.id.reset)
        addButton = findViewById(R.id.bAdd)
        showAllButton = findViewById(R.id.showAll)

        taskName.text = "No Task Started"
        timer.base = SystemClock.elapsedRealtime()

        tasksList = arrayListOf()
        adapter = RVMainAdapter(this, tasksList)
        rvItem.adapter = adapter
        rvItem.layoutManager = LinearLayoutManager(this)

        adapterListener()

        taskViewModel.getAllTasks().observe(this) {
            tasksList.clear()
            tasksList.addAll(it)
            adapter.updateRVMain()
        }

        swipeTasksBuilder()

        addButtonsListeners()
    }

    private fun adapterListener() {
        adapter.setOnItemClickListener(object : RVMainAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (previousPosition == position && running) {
                    timer.stop()
                    pauseOffset = SystemClock.elapsedRealtime() - timer.base
                    val second: Int = (pauseOffset / 1000).toInt()
                    progressBar.isIndeterminate = false
                    val nowTime = pauseOffset - savedTime
                    progressBar.progress = (pauseOffset - (second * 1000)).toInt()
                    savedTime = pauseOffset
                    running = false
                    taskViewModel.updateTaskTime(
                        tasksList[position].taskTime + nowTime,
                        tasksList[position].pk
                    )
                    taskViewModel.updateTaskStatus(false, tasksList[position].pk)
                } else if (previousPosition == position && !running) {
                    progressBar.isIndeterminate = true
                    val second: Int = (pauseOffset / 1000).toInt()
                    progressBar.progress = (pauseOffset - (second * 1000)).toInt()
                    timer.base = SystemClock.elapsedRealtime() - pauseOffset
                    timer.start()
                    running = true
                    taskViewModel.updateTaskStatus(true, tasksList[position].pk)
                } else if (running) {
                    taskName.text = tasksList[position].taskName
                    reset.performClick()
                    taskViewModel.updateTaskStatus(false, tasksList[previousPosition].pk)
                    taskViewModel.updateTaskStatus(true, tasksList[position].pk)
                    previousPosition = position
                } else {
                    taskName.text = tasksList[position].taskName
                    progressBar.isIndeterminate = true
                    timer.start()
                    timer.base = SystemClock.elapsedRealtime()
                    pauseOffset = 0
                    savedTime = 0
                    running = true
                    taskViewModel.updateTaskStatus(true, tasksList[position].pk)
                    previousPosition = position
                }
            }
        })
    }

    private fun addButtonsListeners() {
        reset.setOnClickListener {
            if (running) {
                progressBar.isIndeterminate = false
                progressBar.isIndeterminate = true
                pauseOffset = SystemClock.elapsedRealtime() - timer.base
                val nowTime = pauseOffset - savedTime
                taskViewModel.updateTaskTime(
                    tasksList[previousPosition].taskTime + nowTime,
                    tasksList[previousPosition].pk
                )
                timer.base = SystemClock.elapsedRealtime()
                savedTime = 0
                pauseOffset = 0
            } else {
                progressBar.isIndeterminate = false
                progressBar.progress = 0
                timer.base = SystemClock.elapsedRealtime()
                savedTime = 0
                pauseOffset = 0
            }
        }
        addButton.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
            ifRunning()
        }
        showAllButton.setOnClickListener {
            startActivity(Intent(this, TaskDetailsActivity::class.java))
            ifRunning()
        }
    }

    private fun ifRunning() {
        if (running) {
            timer.stop()
            progressBar.isIndeterminate = false
            pauseOffset = SystemClock.elapsedRealtime() - timer.base
            val nowTime = pauseOffset - savedTime
            taskViewModel.updateTaskTime(
                tasksList[previousPosition].taskTime + nowTime,
                tasksList[previousPosition].pk
            )
            taskViewModel.updateTaskStatus(false, tasksList[previousPosition].pk)
            timer.base = SystemClock.elapsedRealtime()
            running = false
            savedTime = 0
            pauseOffset = 0
        }
    }

    private fun swipeTasksBuilder() {
        val swipeTasks = object : SwipeTasks(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        val intent = Intent(this@MainActivity, EditTasks::class.java)
                        intent.putExtra("pk", tasksList[viewHolder.adapterPosition].pk)
                        startActivity(intent)
                        adapter.updateRVMain()
                        ifRunning()
                    }
                    ItemTouchHelper.LEFT -> {
                        taskViewModel.deleteTask(tasksList[viewHolder.adapterPosition])
                        StyleableToast.makeText(
                            this@MainActivity,
                            "Deleted Successfully",
                            R.style.deleteToast
                        ).show()
                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeTasks)
        touchHelper.attachToRecyclerView(rvItem)
    }
}
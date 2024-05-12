package com.example.tasktimerapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerapp.room.Data
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrInterface
import com.r0adkll.slidr.model.SlidrPosition
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditTasks : AppCompatActivity() {

    private val taskViewModel by lazy { ViewModelProvider(this).get(TaskViewModel::class.java) }

    private lateinit var taskNameEntry: EditText
    private lateinit var taskDescriptionEntry: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var taskName: String
    private lateinit var taskDescription: String
    private lateinit var priority: String
    private lateinit var oldPriority: String
    private var pk = 0
    private lateinit var data: Data
    private lateinit var bundle: Bundle
    private val priorityList = arrayListOf("High", "Medium", "Low")
    private lateinit var slidr: SlidrInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_task)

        taskNameEntry = findViewById(R.id.eTask)
        taskDescriptionEntry = findViewById(R.id.eDescription)
        prioritySpinner = findViewById(R.id.spinner)
        saveButton = findViewById(R.id.save_button)

        bundle = intent.extras!!
        pk = bundle.getInt("pk")
        CoroutineScope(IO).launch {
            data = taskViewModel.getTask(pk)
            withContext(Main) {
                taskName = data.taskName
                taskDescription = data.taskDescription
                priority = data.priority

                onSpinnerSelected()

                setHint()
            }
        }

        onSaveButtonClicked()

        slidrBuilding()
    }

    private fun onSaveButtonClicked() {
        saveButton.setOnClickListener {
            if (checkEntry()) {
                taskViewModel.updateTask(
                    Data(
                        pk,
                        taskName,
                        taskDescription,
                        data.taskTime,
                        priority
                    )
                )
                StyleableToast.makeText(this, "Updated Successfully", R.style.updateToast).show()
            } else {
                StyleableToast.makeText(this, "Please Enter Valid Values", R.style.failToast).show()
            }
            setHint()
        }
    }

    private fun checkEntry(): Boolean {
        var check = false
        if (taskNameEntry.text.isNotBlank()) {
            taskName = taskNameEntry.text.toString()
            check = true
        }
        if (taskDescriptionEntry.text.isNotBlank()) {
            taskDescription = taskDescriptionEntry.text.toString()
            check = true
        }
        if (oldPriority != priority) {
            check = true
        }
        return check
    }

    private fun setHint() {
        taskNameEntry.hint = "Task Name: $taskName"
        taskNameEntry.text.clear()
        taskNameEntry.clearFocus()
        taskDescriptionEntry.hint = "Task Description:\n$taskDescription"
        taskDescriptionEntry.text.clear()
        taskDescriptionEntry.clearFocus()
        oldPriority = priority
        for (i in 0 until priorityList.size) {
            if (priority == priorityList[i]) {
                prioritySpinner.setSelection(i)
            }
        }
    }

    private fun onSpinnerSelected() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, priorityList
        )
        prioritySpinner.adapter = adapter
        prioritySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                priority = priorityList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun slidrBuilding() {
        val config = SlidrConfig.Builder()
            .primaryColor(resources.getColor(R.color.design_default_color_primary))
            .secondaryColor(resources.getColor(R.color.design_default_color_secondary))
            .position(SlidrPosition.LEFT) //LEFT|RIGHT|TOP|BOTTOM|VERTICAL|HORIZONTAL
            .sensitivity(1f)
            .scrimColor(Color.BLACK)
            .scrimStartAlpha(0.8f)
            .scrimEndAlpha(0f)
            .velocityThreshold(2400f)
            .distanceThreshold(0.25f)
            .edge(false) //true|false
            .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
            .build() //You can add .listener(new SlidrListener(){...}) before build

        slidr = Slidr.attach(this, config)
    }
}
package com.example.tasktimerapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerapp.room.Data
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrInterface
import com.r0adkll.slidr.model.SlidrPosition
import io.github.muddz.styleabletoast.StyleableToast

class AddTaskActivity : AppCompatActivity() {

    private val taskViewModel by lazy { ViewModelProvider(this).get(TaskViewModel::class.java) }

    private lateinit var taskNameEntry: EditText
    private lateinit var taskDescriptionEntry: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var priority: String
    private lateinit var slidr: SlidrInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        taskNameEntry = findViewById(R.id.eTask)
        taskDescriptionEntry = findViewById(R.id.eDescription)
        prioritySpinner = findViewById(R.id.spinner)
        saveButton = findViewById(R.id.save_button)

        onSpinnerSelected()

        onSaveButtonClicked()

        slidrBuilding()
    }

    private fun onSaveButtonClicked() {
        saveButton.setOnClickListener {
            if (checkEntry()) {
                when (priority) {
                    "High" -> {
                        taskViewModel.addNewTask(
                            Data(
                                0,
                                taskNameEntry.text.toString(),
                                taskDescriptionEntry.text.toString(),
                                0,
                                "High"
                            )
                        )
                        StyleableToast.makeText(this, "Add Successfully", R.style.addToast).show()
                    }
                    "Medium" -> {
                        taskViewModel.addNewTask(
                            Data(
                                0,
                                taskNameEntry.text.toString(),
                                taskDescriptionEntry.text.toString(),
                                0,
                                "Medium"
                            )
                        )
                        StyleableToast.makeText(this, "Add Successfully", R.style.addToast).show()
                    }
                    "Low" -> {
                        taskViewModel.addNewTask(
                            Data(
                                0,
                                taskNameEntry.text.toString(),
                                taskDescriptionEntry.text.toString(),
                                0,
                                "Low"
                            )
                        )
                        StyleableToast.makeText(this, "Add Successfully", R.style.addToast).show()
                    }
                    else -> {
                        StyleableToast.makeText(this, "Please Choose Priority", R.style.failToast)
                            .show()
                    }
                }
                finish()
            } else {
                StyleableToast.makeText(this, "Please Enter Valid Values", R.style.failToast).show()
            }
        }
    }

    private fun checkEntry(): Boolean {
        if (taskNameEntry.text.isBlank())
            return false
        if (taskDescriptionEntry.text.isBlank())
            return false
        return true
    }

    private fun onSpinnerSelected() {
        val priorityList = arrayListOf("High", "Medium", "Low")
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

            override fun onNothingSelected(parent: AdapterView<*>) {
                priority = ""
            }
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
package com.example.tasktimerapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.tasktimerapp.room.Data
import com.example.tasktimerapp.rv_adapter.RVViewAdapter
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrInterface
import com.r0adkll.slidr.model.SlidrPosition

class TaskDetailsActivity : AppCompatActivity() {

    private val taskViewModel by lazy { ViewModelProvider(this).get(TaskViewModel::class.java) }
    private lateinit var rvViewAdapter: RVViewAdapter

    private lateinit var slidr: SlidrInterface
    private lateinit var pieChart: AnyChartView
    private lateinit var rvAll: RecyclerView
    private lateinit var tasksList: ArrayList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        pieChart = findViewById(R.id.pieChart)
        rvAll = findViewById(R.id.rvAll)
        tasksList = arrayListOf()

        rvViewAdapter = RVViewAdapter(this, tasksList)
        rvAll.adapter = rvViewAdapter
        rvAll.layoutManager = LinearLayoutManager(this)

        taskViewModel.getAllTasks().observe(this) {
            tasksList.addAll(it)
            rvViewAdapter.notifyDataSetChanged()
            setPieChart()
        }

        rvOnScrollListener()

        slidrBuilding()
    }

    private fun rvOnScrollListener() {
        rvAll.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (tasksList.size > 5) {
                    if (dy > 0)
                        pieChart.isVisible = false
                }
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (tasksList.size > 5) {
                    if (recyclerView.canScrollVertically(1))
                        pieChart.isVisible = true
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun setPieChart() {
        val pie = AnyChart.pie()
        val dataEntries = ArrayList<DataEntry>()
        for (i in tasksList) {
            dataEntries.add(ValueDataEntry(i.taskName, (i.taskTime / 1000).toInt()))
        }
        pie.data(dataEntries)
        pieChart.setChart(pie)
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
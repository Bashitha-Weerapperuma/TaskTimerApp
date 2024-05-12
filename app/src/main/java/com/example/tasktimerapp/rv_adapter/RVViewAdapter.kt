package com.example.tasktimerapp.rv_adapter

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimerapp.R
import com.example.tasktimerapp.databinding.ViewRvBinding
import com.example.tasktimerapp.room.Data

class RVViewAdapter(private val context: Context, private val tasks: ArrayList<Data>) :
    RecyclerView.Adapter<RVViewAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: ViewRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ViewRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val task = tasks[position]

        holder.binding.apply {
            title.text = task.taskName
            timer.base = SystemClock.elapsedRealtime() - (task.taskTime)
            totalTime.text = "Total Time\n${timer.contentDescription}"
            when (task.priority) {
                "High" -> {
                    rvLay.background =
                        ContextCompat.getDrawable(context, R.drawable.round_corners_high)
                }
                "Medium" -> {
                    rvLay.background =
                        ContextCompat.getDrawable(context, R.drawable.round_corners_medium)
                }
                "Low" -> {
                    rvLay.background =
                        ContextCompat.getDrawable(context, R.drawable.round_corners_low)
                }
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

}
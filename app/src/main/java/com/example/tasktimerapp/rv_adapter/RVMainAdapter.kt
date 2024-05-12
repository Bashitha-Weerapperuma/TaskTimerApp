package com.example.tasktimerapp.rv_adapter

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimerapp.R
import com.example.tasktimerapp.databinding.MainRvBinding
import com.example.tasktimerapp.room.Data
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection

class RVMainAdapter(private val context: Context, private val tasks: ArrayList<Data>) :
    RecyclerView.Adapter<RVMainAdapter.ItemViewHolder>() {

    private lateinit var myListener: OnItemClickListener
    private var expansionsCollection: ExpansionLayoutCollection = ExpansionLayoutCollection()

    init {
        expansionsCollection.openOnlyOne(true)
    }

    class ItemViewHolder(val binding: MainRvBinding, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.title.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            MainRvBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            myListener
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val task = tasks[position]

        holder.binding.apply {
            expansionsCollection.add(expansionLayout)

            timer.base = SystemClock.elapsedRealtime() - (task.taskTime)

            title.text = task.taskName

            descriptionAndTime.text =
                "Description: ${tasks[position].taskDescription}\nTotal Time: ${timer.contentDescription}"

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

            if (task.isRunning) {
                running.text = "Running"
            } else
                running.text = "Stop"
        }
    }

    override fun getItemCount() = tasks.size

    fun updateRVMain() {
        notifyDataSetChanged()
    }
}
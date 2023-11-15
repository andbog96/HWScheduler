package com.example.hwscheduler.event

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.R
import com.example.hwscheduler.dayMonthYearHoursMinutes
import java.util.Date

class EventAdapter(
    private val activity: Activity
) : RecyclerView.Adapter<EventViewHolder>() {

    private var data: List<Event> = listOf()
    private var channelIdToName: Map<Long, String> = mapOf()

    fun setData(data: List<Event>, channelIdToName: Map<Long, String>) {
        this.data = data
        this.channelIdToName = channelIdToName
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventViewHolder(inflater.inflate(R.layout.item_event, parent, false)).apply {
            itemView.findViewById<ImageView>(R.id.complete_img).setOnClickListener {
                val event = data[this.adapterPosition]
                completeEvent(event)
            }
            itemView.findViewById<ConstraintLayout>(R.id.item_event_cl).setOnClickListener {
                val event = data[this.adapterPosition]
                showEventInfo(event)
            }
        }
    }

    private fun completeEvent(event: Event) {
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.popup_complete_task, null)
        val taskName: TextView = view.findViewById(R.id.popup_comp_task_task_name)
        val taskChannel: TextView = view.findViewById(R.id.popup_comp_task_channel_name)
        val taskDeadline: TextView = view.findViewById(R.id.popup_comp_task_deadline)
        val taskTimeEt: EditText = view.findViewById(R.id.popup_comp_task_time_et)

        taskName.text = event.name
        taskDeadline.text = Date(event.deadline).dayMonthYearHoursMinutes()
        taskChannel.text = channelIdToName[event.channelId].toString()

        val alertDialog = AlertDialog.Builder(activity).apply {
            setView(view)
            setCancelable(false)
            setPositiveButton(R.string.submit, null)
            setNeutralButton("Cancel") { _, _ -> run {} }
        }.create()

        alertDialog.setOnShowListener {
            val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val taskTimeStr = taskTimeEt.text.toString()
                if (taskTimeStr.isNotEmpty()) {
                    var taskTime = taskTimeStr.toInt()
                    alertDialog.dismiss()
                }
            }
        }
        alertDialog.show()
    }

    private fun showEventInfo(event: Event) {
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.popup_info_task, null)
        val taskName: TextView = view.findViewById(R.id.popup_info_task_name)
        val taskChannel: TextView = view.findViewById(R.id.popup_info_task_channel_name)
        val taskDeadline: TextView = view.findViewById(R.id.popup_info_task_deadline)
        val taskDescription: TextView = view.findViewById(R.id.popup_info_task_description)

        taskName.text = event.name
        taskDeadline.text = Date(event.deadline).dayMonthYearHoursMinutes()
        taskDescription.text = event.description
        taskChannel.text = channelIdToName[event.channelId].toString()

        val alertDialog = AlertDialog.Builder(activity).apply {
            setView(view)
            setCancelable(false)
            setPositiveButton(R.string.cancel) { _, _ -> run {} }
        }.create()

        alertDialog.show()
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = data[position]
        holder.bind(event, channelIdToName[event.channelId]!!)
    }

}

class EventViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val eventName: TextView = root.findViewById(R.id.event_name)

    @SuppressLint("SetTextI18n")
    fun bind(event: Event, channelName: String) {
        eventName.text = "$channelName/${event.name}"
    }
}
package com.example.hwscheduler.group

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.ErrorUtils
import com.example.hwscheduler.R
import com.example.hwscheduler.api.CreateEventReq
import com.example.hwscheduler.app.HWApplication
import com.example.hwscheduler.dayMonthYearHoursMinutes
import com.example.hwscheduler.event.Event
import com.example.hwscheduler.userToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class GroupEventAdapter(
    private val activity: Activity,
    private val channel: Group,
    private val updateUI: () -> Unit
) : RecyclerView.Adapter<GroupEventViewHolder>() {

    private var data: List<Event> = listOf()

    fun setData(data: List<Event>) {
        this.data = data
        notifyDataSetChanged()
    }

    @SuppressLint("CheckResult")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupEventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GroupEventViewHolder(
            inflater.inflate(
                R.layout.item_group_event,
                parent,
                false
            )
        ).apply {
            itemView.findViewById<ImageView>(R.id.event_edit_img).setOnClickListener {
                val event = data[this.adapterPosition]
                editEvent(event, channel)
            }
            itemView.findViewById<ImageView>(R.id.event_remove_img).setOnClickListener {
                val event = data[this.adapterPosition]
                HWApplication.instance.hwApi.deleteEvent(userToken!!, event.channelId, event.eventId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateUI()
                    }, {
                        ErrorUtils.showMessage(it, activity)
                    })
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: GroupEventViewHolder, position: Int) {
        val event = data[position]
        holder.bind(event)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    private fun editEvent(event: Event, channel: Group) {
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.popup_edit_task, null)
        val title: TextView = view.findViewById(R.id.popup_edit_task_title)
        val channelName: TextView = view.findViewById(R.id.popup_edit_task_channel_name)
        val taskNameEt: EditText = view.findViewById(R.id.popup_edit_task_name)
        val taskDeadlineTv: TextView = view.findViewById(R.id.popup_edit_task_deadline)
        val taskDescriptionEt: EditText = view.findViewById(R.id.popup_edit_task_description)

        title.setText(R.string.edit_task)
        channelName.text = channel.name
        taskNameEt.setText(event.name, TextView.BufferType.EDITABLE)
        taskDeadlineTv.setText(event.deadline.toString(), TextView.BufferType.EDITABLE)
        taskDescriptionEt.setText(event.description, TextView.BufferType.EDITABLE)

        val alertDialog = AlertDialog.Builder(activity).apply {
            setView(view)
            setCancelable(false)
            setPositiveButton(R.string.submit, null)
            setNeutralButton("Cancel") { _, _ -> run {} }
        }.create()

        val selectedDate = GregorianCalendar()
            .apply { time = Date(event.deadline) }
        taskDeadlineTv.text = selectedDate.time.dayMonthYearHoursMinutes()

        taskDeadlineTv.setOnClickListener {
            DatePickerDialog(
                activity,
                { _, y, moy, dom ->
                    selectedDate[Calendar.YEAR] = y
                    selectedDate[Calendar.MONTH] = moy
                    selectedDate[Calendar.DAY_OF_MONTH] = dom
                    TimePickerDialog(
                        activity, { _, hod, m ->
                            selectedDate[Calendar.HOUR_OF_DAY] = hod
                            selectedDate[Calendar.MINUTE] = m
                            taskDeadlineTv.text = selectedDate.time.dayMonthYearHoursMinutes()
                        },
                        selectedDate[Calendar.HOUR_OF_DAY],
                        selectedDate[Calendar.MINUTE],
                        true
                    ).show()
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH),
            ).show()
        }

        alertDialog.setOnShowListener {
            val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val taskName = taskNameEt.text.toString()
                val taskDeadline = taskDeadlineTv.text.toString()
                val taskDescription = taskDescriptionEt.text.toString()
                if (taskName.isNotEmpty() && taskDeadline.isNotEmpty()) {
                    HWApplication.instance.hwApi.updateEvent(
                        userToken!!, event.eventId, CreateEventReq(
                            taskName, taskDescription, taskDeadline
                        )
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            updateUI()
                            alertDialog.dismiss()
                        }, {
                            ErrorUtils.showMessage(it, activity)
                        })
                }
            }
        }
        alertDialog.show()
    }

}

class GroupEventViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val eventName: TextView = root.findViewById(R.id.group_event_name)

    @SuppressLint("SetTextI18n")
    fun bind(event: Event) {
        eventName.text = event.name
    }
}
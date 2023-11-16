package com.example.hwscheduler.group

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.ErrorUtils
import com.example.hwscheduler.R
import com.example.hwscheduler.api.CreateEventReq
import com.example.hwscheduler.app.HWApplication
import com.example.hwscheduler.dayMonthYearHoursMinutes
import com.example.hwscheduler.event.Event
import com.example.hwscheduler.event.EventActivity
import com.example.hwscheduler.updateUserInfo
import com.example.hwscheduler.userInfo
import com.example.hwscheduler.userToken
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Calendar
import java.util.GregorianCalendar

class GroupEventsActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var createEventBtn: Button

    private lateinit var groupEventsRV: RecyclerView
    private lateinit var eventAdapter: GroupEventAdapter

    private lateinit var channel: Group

    companion object {
        const val GROUP_ID = "GROUP_ID"
        const val GROUP_NAME = "GROUP_NAME"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_events)

        val channelId = intent.getLongExtra(GROUP_ID, -1)
        channel = userInfo.channels.find { channelId == it.channelId }!!

        groupEventsRV = findViewById(R.id.group_events_rv)

        bottomNavView = findViewById(R.id.group_events_bottom_nav)
        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile_tab -> true
                R.id.event_tab -> {
                    val intent = Intent(this, EventActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        createEventBtn = findViewById(R.id.create_event_btn)
        createEventBtn.setOnClickListener {
            val selectedDate = GregorianCalendar()
            selectedDate.add(GregorianCalendar.DAY_OF_MONTH, 7)
            selectedDate.add(GregorianCalendar.HOUR_OF_DAY, 3)

            val event =
                Event(-1, channelId, "", "", selectedDate.time.dayMonthYearHoursMinutes(), 0)

            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.popup_edit_task, null)
            val title: TextView = view.findViewById(R.id.popup_edit_task_title)
            val channelName: TextView = view.findViewById(R.id.popup_edit_task_channel_name)
            val taskNameEt: EditText = view.findViewById(R.id.popup_edit_task_name)
            val taskDeadlineTv: TextView = view.findViewById(R.id.popup_edit_task_deadline)
            val taskDescriptionEt: EditText = view.findViewById(R.id.popup_edit_task_description)

            title.setText(R.string.create_task)
            channelName.text = channel.name
            taskNameEt.setText(event.name, TextView.BufferType.EDITABLE)
            taskDeadlineTv.setText(event.deadline, TextView.BufferType.EDITABLE)
            taskDescriptionEt.setText(event.description, TextView.BufferType.EDITABLE)

            val alertDialog = AlertDialog.Builder(this).apply {
                setView(view)
                setCancelable(false)
                setPositiveButton(R.string.submit, null)
                setNeutralButton("Cancel") { _, _ -> run {} }
            }.create()


            taskDeadlineTv.text = selectedDate.time.dayMonthYearHoursMinutes()

            taskDeadlineTv.setOnClickListener {
                DatePickerDialog(
                    this,
                    { _, y, moy, dom ->
                        selectedDate[Calendar.YEAR] = y
                        selectedDate[Calendar.MONTH] = moy
                        selectedDate[Calendar.DAY_OF_MONTH] = dom
                        TimePickerDialog(
                            this, { _, hod, m ->
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
                        HWApplication.instance.hwApi.createEvent(
                            userToken!!, channelId, CreateEventReq(
                                taskName, taskDescription, taskDeadline
                            )
                        ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                alertDialog.dismiss()
                                updateUserInfo(this) { updateUI() }
                            }, {
                                ErrorUtils.showMessage(it, this)
                            })
                        alertDialog.dismiss()
                    }
                }
            }
            alertDialog.show()
        }

        updateUI()
        updateUserInfo(this) { updateUI() }
    }

    private fun updateUI() {
        val events = userInfo.events.filter { channel.channelId == it.channelId }
        eventAdapter = GroupEventAdapter(this, channel) { updateUserInfo(this) { updateUI() } }
        eventAdapter.setData(events)

        groupEventsRV.layoutManager = LinearLayoutManager(this)
        groupEventsRV.adapter = eventAdapter

        bottomNavView.selectedItemId = R.id.profile_tab
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

}
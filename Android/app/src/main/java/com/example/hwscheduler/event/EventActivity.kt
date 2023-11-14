package com.example.hwscheduler.event

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.R
import com.example.hwscheduler.group.GroupActivity
import com.example.hwscheduler.userInfo
import com.google.android.material.bottomnavigation.BottomNavigationView

class EventActivity : AppCompatActivity() {

    private lateinit var eventRV: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var subscribeBtn: Button

    private lateinit var taskTimeEt: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        eventAdapter = EventAdapter()

        val map = mutableMapOf<Int, String>()
        userInfo.channels.forEach { map[it.channelId] = it.name }

        eventAdapter.setData(userInfo.events, map)

        eventRV = findViewById(R.id.event_rv)
        eventRV.layoutManager = LinearLayoutManager(this)
        eventRV.adapter = eventAdapter

        bottomNavView = findViewById(R.id.event_bottom_nav)
        bottomNavView.selectedItemId = R.id.event_tab
        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.event_tab -> true
                R.id.profile_tab -> {
                    val intent = Intent(this, GroupActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        subscribeBtn = findViewById(R.id.subscribe_btn)
        subscribeBtn.setOnClickListener { subscribePopup() }
    }

    private fun subscribePopup() {
        val inflater = LayoutInflater.from(this@EventActivity)
        val view = inflater.inflate(R.layout.popup_subscribe, null)
        taskTimeEt = view.findViewById(R.id.channel_name)

        val alertDialog = AlertDialog.Builder(this@EventActivity).apply {
            setTitle(R.string.subscribe_on_channel)
            setView(view)
            setCancelable(false)
            setPositiveButton(R.string.subscribe, null)
            setNeutralButton("Cancel") { _, _ -> run {} }
        }.create()

        alertDialog.setOnShowListener {
            val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val channelName = taskTimeEt.text.toString()
                if (channelName.isNotEmpty()) alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        bottomNavView.selectedItemId = R.id.event_tab
    }
}
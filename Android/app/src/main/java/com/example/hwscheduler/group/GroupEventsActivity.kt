package com.example.hwscheduler.group

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.R
import com.example.hwscheduler.event.EventActivity
import com.example.hwscheduler.userInfo
import com.google.android.material.bottomnavigation.BottomNavigationView

class GroupEventsActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var createEventBtn: Button

    private lateinit var groupEventsRV: RecyclerView
    private lateinit var eventAdapter: GroupEventAdapter

    companion object {
        const val GROUP_ID = "GROUP_ID"
        const val GROUP_NAME = "GROUP_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_events)

        val channelId = intent.getLongExtra(GROUP_ID, -1)
        val events = userInfo.events.filter { channelId == it.channelId }
        val channel = userInfo.channels.find { channelId == it.channelId }!!

        eventAdapter = GroupEventAdapter(this, channel)
        eventAdapter.setData(events)

        groupEventsRV = findViewById(R.id.group_events_rv)
        groupEventsRV.layoutManager = LinearLayoutManager(this)
        groupEventsRV.adapter = eventAdapter

        bottomNavView = findViewById(R.id.group_events_bottom_nav)
        bottomNavView.selectedItemId = R.id.profile_tab
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
    }

    override fun onResume() {
        super.onResume()
        bottomNavView.selectedItemId = R.id.profile_tab
    }

}
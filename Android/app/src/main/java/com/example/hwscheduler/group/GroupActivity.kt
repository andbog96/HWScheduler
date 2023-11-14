package com.example.hwscheduler.group

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.R
import com.example.hwscheduler.authorize.AuthorizeActivity
import com.example.hwscheduler.event.EventActivity
import com.example.hwscheduler.userInfo
import com.google.android.material.bottomnavigation.BottomNavigationView

class GroupActivity : AppCompatActivity() {

    private lateinit var groupRV: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var bottomNavView: BottomNavigationView

    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        groupAdapter = GroupAdapter(this)
        groupAdapter.setData(userInfo.channels)

        groupRV = findViewById(R.id.group_rv)
        groupRV.layoutManager = LinearLayoutManager(this)
        groupRV.adapter = groupAdapter

        bottomNavView = findViewById(R.id.group_bottom_nav)
        bottomNavView.selectedItemId = R.id.profile_tab
        bottomNavView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.profile_tab -> true
                R.id.event_tab -> {
                    val intent = Intent(this, EventActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        logoutBtn = findViewById(R.id.logout_btn)
        logoutBtn.setOnClickListener {
            val intent = Intent(this, AuthorizeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNavView.selectedItemId = R.id.profile_tab
    }
}
package com.example.hwscheduler.event

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.ErrorUtils
import com.example.hwscheduler.R
import com.example.hwscheduler.api.SubscribeChannelReq
import com.example.hwscheduler.app.HWApplication
import com.example.hwscheduler.group.GroupActivity
import com.example.hwscheduler.updateUserInfo
import com.example.hwscheduler.userInfo
import com.example.hwscheduler.userToken
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EventActivity : AppCompatActivity() {

    private lateinit var eventRV: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var subscribeBtn: Button


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        eventAdapter = EventAdapter(this) { updateUserInfo(this) { updateUI() } }
        eventRV = findViewById(R.id.event_rv)
        bottomNavView = findViewById(R.id.event_bottom_nav)
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

        updateUI()
        updateUserInfo(this) { updateUI() }
    }

    private fun updateUI() {
        val map = mutableMapOf<Long, String>()
        userInfo.channels.forEach { map[it.channelId] = it.name }
        eventAdapter.setData(userInfo.events, map)
        bottomNavView.selectedItemId = R.id.event_tab
        eventRV.layoutManager = LinearLayoutManager(this)
        eventRV.adapter = eventAdapter
    }

    @SuppressLint("CheckResult")
    private fun subscribePopup() {
        val inflater = LayoutInflater.from(this@EventActivity)
        val view = inflater.inflate(R.layout.popup_subscribe, null)
        val channelNameEt: EditText = view.findViewById(R.id.channel_name)

        val alertDialog = AlertDialog.Builder(this@EventActivity).apply {
            setView(view)
            setCancelable(false)
            setPositiveButton(R.string.subscribe, null)
            setNeutralButton("Cancel") { _, _ -> run {} }
        }.create()

        alertDialog.setOnShowListener {
            val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val channelName = channelNameEt.text.toString()
                if (channelName.isNotEmpty()) {
                    HWApplication.instance.hwApi.subscribe(
                        userToken!!,
                        SubscribeChannelReq(channelName)
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            alertDialog.dismiss()
                            updateUserInfo(this) { updateUI() }
                        }, {
                            ErrorUtils.showMessage(it, this)
                        })
                }
            }
        }
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
        updateUserInfo(this) { updateUI() }
    }
}
package com.example.hwscheduler.group

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
import com.example.hwscheduler.api.CreateChannelReq
import com.example.hwscheduler.app.HWApplication
import com.example.hwscheduler.authorize.AuthorizeActivity
import com.example.hwscheduler.event.EventActivity
import com.example.hwscheduler.updateUserInfo
import com.example.hwscheduler.userInfo
import com.example.hwscheduler.userToken
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GroupActivity : AppCompatActivity() {

    private lateinit var groupRV: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var bottomNavView: BottomNavigationView

    private lateinit var logoutBtn: Button
    private lateinit var createChannelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        groupAdapter = GroupAdapter(this) { updateUserInfo(this) { updateUI() } }

        bottomNavView = findViewById(R.id.group_bottom_nav)
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

        groupRV = findViewById(R.id.group_rv)

        logoutBtn = findViewById(R.id.logout_btn)
        logoutBtn.setOnClickListener {
            val intent = Intent(this, AuthorizeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        createChannelBtn = findViewById(R.id.create_channel_btn)
        createChannelBtn.setOnClickListener {
            createChannelPopup()
        }

        updateUI()
        updateUserInfo(this) { updateUI() }
    }

    @SuppressLint("CheckResult")
    private fun createChannelPopup() {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.popup_create_channel, null)
        val channelNameEt: EditText = view.findViewById(R.id.create_channel_name)

        val alertDialog = AlertDialog.Builder(this).apply {
            setView(view)
            setCancelable(false)
            setPositiveButton(R.string.create_channel, null)
            setNeutralButton("Cancel") { _, _ -> run {} }
        }.create()

        alertDialog.setOnShowListener {
            val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val channelName = channelNameEt.text.toString()
                if (channelName.isNotEmpty()) {
                    HWApplication.instance.hwApi.createChannel(
                        userToken!!,
                        CreateChannelReq(channelName)
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

    private fun updateUI() {
        bottomNavView.selectedItemId = R.id.profile_tab
        groupAdapter.setData(userInfo.channels)

        groupRV.layoutManager = LinearLayoutManager(this)
        groupRV.adapter = groupAdapter
    }

    override fun onResume() {
        super.onResume()
        updateUI()
        updateUserInfo(this) { updateUI() }
    }
}
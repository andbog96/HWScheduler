package com.example.hwscheduler

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import com.example.hwscheduler.app.HWApplication
import com.example.hwscheduler.event.Event
import com.example.hwscheduler.group.Group
import com.squareup.moshi.Json
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Date

var curDate = Date().time
var day = 1000 * 60 * 60 * 24

var userInfo = UserInfo(listOf(), listOf())
var userToken: String? = null

@SuppressLint("CheckResult")
fun updateUserInfo(activity: Activity, updateUI: () -> Unit) {
    HWApplication.instance.hwApi.userInfo(userToken!!)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {
                Log.i("Get user info", "$it")
                userInfo = it
                updateUI()
            }, {
                ErrorUtils.showMessage(it, activity)
            }
        )
}

data class UserInfo(
    @field:Json(name = "channels") val channels: List<Group>,
    @field:Json(name = "events") val events: List<Event>
)
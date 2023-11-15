package com.example.hwscheduler

import com.example.hwscheduler.event.Event
import com.example.hwscheduler.group.Group
import com.squareup.moshi.Json
import java.util.Date

var curDate = Date().time
var day = 1000 * 60 * 60 * 24

var userInfo = UserInfo(
    listOf(
        Group(0, "ИТМО", false),
        Group(1, "ВШЭ", false),
        Group(2, "СПБГУ", true)
    ),
    listOf(
        Event(0, 0, "БД ДЗ 1", "", curDate + 1 * day, 0),
        Event(1, 0, "МЛ ДЗ 2", "", curDate + 2 * day, 0),
        Event(2, 0, "МатАн ДЗ 3", "", curDate + 3 * day, 0),

        Event(3, 1, "МатСтат ДЗ 234", "", curDate + 5 * day, 0),
        Event(4, 1, "ТеорВер ДЗ 123", "", curDate + 8 * day, 0),

        Event(5, 2, "ЛинАл ДЗ 1337", "", curDate + 13 * day, 0)
    )
)

var userToken: String? = null

data class UserInfo(
    @field:Json(name = "channels") val channels: List<Group>,
    @field:Json(name = "events") val events: List<Event>
)
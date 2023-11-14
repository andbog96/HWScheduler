package com.example.hwscheduler

import com.example.hwscheduler.event.Event
import com.example.hwscheduler.group.Group

var userInfo = UserInfo(
    listOf(
        Group(0, "ИТМО", false),
        Group(1, "ВШЭ", false),
        Group(2, "СПБГУ", true)
    ),
    listOf(
        Event(0, 0, "БД ДЗ 1", "", 10, 0),
        Event(1, 0, "МЛ ДЗ 2", "", 20, 0),
        Event(2, 0, "МатАн ДЗ 3", "", 30, 0),

        Event(3, 1, "МатСтат ДЗ 234", "", 10, 0),
        Event(4, 1, "ТеорВер ДЗ 123", "", 70, 0),

        Event(5, 2, "ЛинАл ДЗ 1337", "", 45, 0)
    )
)

data class UserInfo(
    val channels: List<Group>,
    val events: List<Event>
)
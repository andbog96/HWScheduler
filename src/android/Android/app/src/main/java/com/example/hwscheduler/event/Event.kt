package com.example.hwscheduler.event

import com.squareup.moshi.Json

data class Event(
    @field:Json(name = "event_id") val eventId: Long,
    @field:Json(name = "channel_id") val channelId: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "deadline") val deadline: String,
    @field:Json(name = "estimated") val estimated: Long
)
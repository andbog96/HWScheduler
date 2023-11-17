package com.example.hwscheduler.group

import com.squareup.moshi.Json

data class Group(
    @field:Json(name = "channel_id") val channelId: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "is_admin") val isAdmin: Boolean = true
)
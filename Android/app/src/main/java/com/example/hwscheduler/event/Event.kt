package com.example.hwscheduler.event

data class Event(
    val eventId: Long,
    val channelId: Long,
    val name: String,
    val description: String,
    val deadline: Long,
    val estimated: Long
)
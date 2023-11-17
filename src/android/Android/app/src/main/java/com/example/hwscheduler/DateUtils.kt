package com.example.hwscheduler

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.dayAndMonth(): String = SimpleDateFormat("dd MMM").format(this)

@SuppressLint("SimpleDateFormat")
fun Date.dayMonthYearHoursMinutes(): String = SimpleDateFormat("yyyy-MM-dd HH:mm").format(this)
//fun Date.dayMonthYearHoursMinutes(): String = SimpleDateFormat("dd-MM-yyyy HH:mm").format(this)

@SuppressLint("SimpleDateFormat")
fun Date.hoursAndMinutes(): String = SimpleDateFormat("HH:mm").format(this)

@SuppressLint("SimpleDateFormat")
fun GregorianCalendar.dayAndMonth(): String = this.time.dayAndMonth()

fun GregorianCalendar.dayMonthYear(): GregorianCalendar =
    GregorianCalendar(this[Calendar.YEAR], this[Calendar.MONTH], this[Calendar.DAY_OF_MONTH])

fun GregorianCalendar.hoursAndMinutes(): String = this.time.hoursAndMinutes()

@RequiresApi(Build.VERSION_CODES.O)
fun fromString(dateString: String): GregorianCalendar {
    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val time = LocalDateTime.from(pattern.parse(dateString))
    return GregorianCalendar(
        time.year,
        time.month.value,
        time.dayOfMonth,
        time.hour,
        time.minute,
        time.second
    )
}
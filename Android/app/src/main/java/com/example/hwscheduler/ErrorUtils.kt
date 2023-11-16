package com.example.hwscheduler

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

object ErrorUtils {

    @SuppressLint("InflateParams")
    fun showMessage(e: Throwable, activity: Activity) {
        val mes = e.message?: e.toString()
        Log.e("Error: ", mes)

        val layout: View = activity.layoutInflater.inflate(R.layout.layout_error, null)
        val text = layout.findViewById<View>(R.id.text_error) as TextView
        val img = layout.findViewById<ImageView>(R.id.img_error)
        text.text = mes
        text.width = 900
        img.setImageResource(R.drawable.ic_error)
        Toast(activity).apply {
            duration = Toast.LENGTH_LONG
            view = layout
            setGravity(Gravity.TOP, 0, 0)
        }.show()
    }
}

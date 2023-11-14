package com.example.hwscheduler.authorize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.hwscheduler.R
import com.example.hwscheduler.event.EventActivity

class AuthorizeActivity : AppCompatActivity() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorize)
        loginEditText = findViewById(R.id.login_et)
        passwordEditText = findViewById(R.id.password_et)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val intent = Intent(this, EventActivity::class.java)
            val login = loginEditText.text.toString()
            val pass = passwordEditText.text.toString()
            startActivity(intent)
            finish()
        }
    }
}
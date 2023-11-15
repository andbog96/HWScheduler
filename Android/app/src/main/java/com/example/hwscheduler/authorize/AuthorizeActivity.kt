package com.example.hwscheduler.authorize

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.hwscheduler.R
import com.example.hwscheduler.api.LoginPasswordReq
import com.example.hwscheduler.app.HWApplication
import com.example.hwscheduler.event.EventActivity
import com.example.hwscheduler.userToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AuthorizeActivity : AppCompatActivity() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorize)
        loginEditText = findViewById(R.id.login_et)
        passwordEditText = findViewById(R.id.password_et)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            HWApplication.instance.hwApi.registerUser(LoginPasswordReq(login, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val intent = Intent(this, EventActivity::class.java)
                    userToken = it.token
                    startActivity(intent)
                    finish()
                }, { Log.e("error: ", "$it") })
        }
    }
}
package com.group2.pacepal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class SessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.session_activity)

        val sessionId = intent.getStringExtra("sessionID")

    }
}
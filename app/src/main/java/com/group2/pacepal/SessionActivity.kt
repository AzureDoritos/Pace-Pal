package com.group2.pacepal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity


class SessionActivity : AppCompatActivity() {

    val sessionId = intent.getStringExtra("sessionID")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.session_activity)

        val transaction = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("message", sessionId)
        val fragInfo = Fragment()
        fragInfo.arguments = bundle
        transaction.replace(R.id.heldFragment, fragInfo)
        transaction.commit()










    }


}
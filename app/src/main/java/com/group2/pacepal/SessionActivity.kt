package com.group2.pacepal

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity


class SessionActivity : AppCompatActivity() {

    //val sessionId = intent.getStringExtra("sessionID")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.session_activity)

        val readyFragment = ReadyUpFragment.newInstance()
        openFragment(readyFragment)









    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.sessionHolder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
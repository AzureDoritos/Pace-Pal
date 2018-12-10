package com.group2.pacepal

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.session_activity.*
import android.R.attr.fragment
import android.content.Intent


class SessionActivity : AppCompatActivity() {

    //val sessionId = intent.getStringExtra("sessionID")
    private val user = FirebaseAuth.getInstance().currentUser
    private val userid = user!!.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.session_activity)


        Log.d("sessionActivity", "init")

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sessionID = preferences.getString("sessionID", "")
        val readyState = preferences.getBoolean("readyState", false)
        val initState = preferences.getBoolean("initState", false)
        val sessionType = preferences.getString("sessionType", "")

        Log.d("readyState", readyState.toString())
        Log.d("initState", initState.toString())
        Log.d("sessionID", sessionID)
        Log.d("userID", userid)

        if(sessionID == userid) {
            openFragment(ReadyUpFragment.newInstance())
            openFragment(SessionInitFragment.newInstance())
        }
        else
            openFragment(ReadyUpFragment.newInstance())

        var palSelected = false

        supportFragmentManager.addOnBackStackChangedListener {
            val readyState = preferences.getBoolean("readyState", false)
            val initState = preferences.getBoolean("initState", false)

            if( !palSelected && initState ){
                //fill in profile at top
                palSelected = true
            }


            if(readyState && initState){
                val intent = Intent(this, MyMap::class.java)
                this.startActivity(intent)
            }
        }


    }





    override fun onResume() {
        super.onResume()

        //openFragment(SessionInitFragment.newInstance())

       /* Log.d("sessionActivity", "resumed")

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sessionID = preferences.getString("sessionID", "")
        val readyState = preferences.getBoolean("readyState", false)
        val initStater = preferences.getBoolean("initState", false)
        val sessionType = preferences.getString("sessionType", "")

        Log.d("readyState", readyState.toString())
        Log.d("initState", initStater.toString())
        Log.d("sessionID", sessionID)
        Log.d("userID", userid)



        if(sessionID == userid){
            if(!initStater){
                Log.d("initState", "launching Initstate fragment")
                //val initFragment = SessionInitFragment.newInstance()
                openFragment(SessionInitFragment.newInstance())
            }
            else if(!readyState){
                Log.d("initState", "launching ready fragment")
                val readyFragment = ReadyUpFragment.newInstance()
                openFragment(readyFragment)
            }
            else{
                //begin session
            }

        }
        else{
            if(!readyState){
                val readyFragment = ReadyUpFragment.newInstance()
                openFragment(readyFragment)
            }
            else{
                //begin session
            }
        }*/


    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.sessionHolder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
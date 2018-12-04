package com.group2.pacepal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.readyup_fragment.*


class ReadyUpFragment : Fragment() {

    private val user = FirebaseAuth.getInstance().currentUser
    private val userid = user!!.uid
    val sessionID = arguments!!.getString("sessionID")
    private val rtdb = FirebaseDatabase.getInstance().reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var hostReady = false
        var p2Ready = false
        var absReady = false

        val hostListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.child("p2ready").toString() == "true") {
                    p2Ready = true
                    palStatus.text = "Ready"
                    //palStatus.setTextColor(#ff669900) = #ff669900
                }
                else{
                    p2Ready = false
                    palStatus.text = "Not Ready"
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        val p2Listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.child("ready").value.toString() == userid) {



                }}

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        if(userid == sessionID)
            rtdb.child("sessionManager").child("sessionIndex").child(sessionID).child("ready").addValueEventListener(hostListener)
        else
            rtdb.child("sessionManager").child("sessionIndex").child(sessionID).addValueEventListener(p2Listener)



        return inflater.inflate(R.layout.readyup_fragment, container, false)
        }

    companion object {
        fun newInstance(): ReadyUpFragment = ReadyUpFragment()
    }

}
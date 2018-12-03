package com.group2.pacepal

import android.app.ActionBar
import android.os.Bundle
import android.se.omapi.Session
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.api.Distribution
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.session_menu.*
//import mypackage.util.ContextExtensions.toast
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.*
import android.widget.LinearLayout
import android.widget.TextView
import android.support.v7.widget.RecyclerView.LayoutManager
//import com.google.firebase.database.ValueEventListener


class SessionFragment : Fragment() {

    private val fsdb = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser
    private val userid = user!!.uid
    private val invitesList = ArrayList<Invite>(0)
    private val adapter = RecyclerAdapter1(invitesList)
    private val rtdb = FirebaseDatabase.getInstance().reference

    private lateinit var inviteRefrence: DatabaseReference


    private lateinit var linearLayoutManager:LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.session_menu, container, false)

        val invView = view?.findViewById(R.id.sessionInvites) as RecyclerView
        invView.layoutManager = LinearLayoutManager(this.context)

        invView.adapter = adapter

        val refreshButton = view.findViewById<Button>(R.id.inviteRefresh)
        refreshButton.setOnClickListener { refreshInvites() }

        refreshInvites()




        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): SessionFragment = SessionFragment()
    }

    private val clickListener: View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.inviteRefresh -> {
                refreshInvites()
                Toast.makeText(context,"Clicked!",Toast.LENGTH_SHORT).show()
            }
            //R.id.textview2-> {
            //    Toast.makeText(this, "Clicked 2", Toast.LENGTH_SHORT).show()
            //}
        }
    }


    private fun refreshInvites() {
        invitesList.clear()
        //inviteRefresh.text = "loading.."
        val friendsList = fsdb.collection("users").document(userid).collection("friends")
        friendsList.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result!!.size() == 0)
                            Toast.makeText(context, "You need friends first.", Toast.LENGTH_SHORT).show()
                        for (document in task.result!!) {
                            //invitesList.add(Invite(document.id, document.id, document.id))
                            inviteRefrence = FirebaseDatabase.getInstance().reference

                            val invListener = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    //invScan.clear()
                                    //dataSnapshot.mapNotNullTo(invScan) { it.getValue<SessionMetadata>(SessionMetadata::class.java) }
                                    //Toast.makeText(context,dataSnapshot.child("P2").toString(),Toast.LENGTH_SHORT)
                                    //invitesList.add(Invite(document.id, document.id, dataSnapshot.child("P2").toString()))
                                    if(dataSnapshot.child("P2").value.toString() == userid) {

                                        val host = fsdb.collection("users").document(document.id)
                                        host.get().addOnSuccessListener { hostProfile ->
                                            val hostUsername = hostProfile.getString("username").toString()
                                            val hostUID = document.id
                                            var sessionType = dataSnapshot.child("type").value.toString()
                                            if(sessionType != "0") {
                                                if(sessionType == "1") sessionType = "Competitive"
                                                if(sessionType == "2") sessionType = "Collaborative"
                                                if(sessionType == "3") sessionType = "Social"

                                                invitesList.add(Invite(hostUsername, hostUID, sessionType))
                                                adapter.notifyDataSetChanged()
                                            }
                                    }

                                }}

                                override fun onCancelled(databaseError: DatabaseError) {
                                    println("loadPost:onCancelled ${databaseError.toException()}")
                                }
                           }

                            rtdb.child("sessionManager").child("sessionIndex").child(document.id).addListenerForSingleValueEvent(invListener)

                            }


                        adapter.notifyDataSetChanged()

                    }
                    else
                        Toast.makeText(context,"Connection error.", Toast.LENGTH_SHORT)
                }



    }

    //data class Invite (val host: String, val hostID: String, val type: String)

}
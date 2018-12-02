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

    private lateinit var inviteRefrence: DatabaseReference
    private var sesListener: ValueEventListener? = null

    private lateinit var linearLayoutManager:LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.session_menu, container, false)

        val invView = view?.findViewById(R.id.sessionInvites) as RecyclerView
        invView.layoutManager = LinearLayoutManager(this.context)







        invView.adapter = adapter


        val refreshButton = view.findViewById<Button>(R.id.inviteRefresh)
        refreshButton.setOnClickListener {refreshInvites() }
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

        //Toast.makeText(context, "Getting invites..", Toast.LENGTH_SHORT).show()
        //invitesList.clear()
        val friendsList = fsdb.collection("users").document(userid).collection("friends")
        friendsList.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result!!.size() == 0)
                            Toast.makeText(context, "You need friends first.", Toast.LENGTH_SHORT).show()
                        for (document in task.result!!) {
                            invitesList.add(Invite(document.id, document.id, document.id))
                            inviteRefrence = FirebaseDatabase.getInstance().reference
                                    //.child("sessionManager").child("sessionIndex").child(document.id)

                            inviteRefrence.child("sessionManager").child("sessionIndex").child(document.id)
                                    .addListenerForSingleValueEvent(object : ValueEventListener
                                    {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            // Get Post object and use the values to update the UI
                                            val friendSes = dataSnapshot.getValue(SessionMetadata::class.java)
                                            // [START_EXCLUDE]
                                            if (friendSes!!.P2 == userid) {
                                                val host = fsdb.collection("users").document(document.id)
                                                host.get().addOnSuccessListener { hostProfile ->
                                                    val hostUsername = hostProfile.getString("username").toString()
                                                    val hostUID = document.id
                                                    val sessionType = friendSes!!.type
                                                    invitesList.add(Invite(hostUsername, hostUID, sessionType))
                                                }
                                            }
                                            // [END_EXCLUDE]
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            // Getting Post failed, log a message
                                            // [START_EXCLUDE]
                                            // [END_EXCLUDE]
                                        }
                                    }

                                    )



                            /*val sesListener = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    // Get Post object and use the values to update the UI
                                    val inv = dataSnapshot.getValue(SessionMetadata::class.java)
                                    // [START_EXCLUDE]
                                    if (inv!!.P2 == userid) {
                                        val host = fsdb.collection("users").document(document.id)
                                        host.get().addOnSuccessListener { hostProfile ->
                                            val hostUsername = hostProfile.getString("username").toString()
                                            val hostUID = document.id
                                            val sessionType = inv!!.type
                                            invitesList.add(Invite(hostUsername, hostUID, sessionType))
                                        }
                                    }
                                    // [END_EXCLUDE]
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Getting Post failed, log a message
                                    // [START_EXCLUDE]
                                    // [END_EXCLUDE]
                                }
                            }
                            //sessionRefrence.addValueEventListener(sesListener)



                            FirebaseDatabase.getInstance().reference.child("sessionManager").child("sessionIndex").child(document.id)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            // Get user information
                                            val sessionInstance = dataSnapshot.getValue(SessionMetadata::class.java)
                                                    ?: return
                                            Toast.makeText(context,sessionInstance.P2,Toast.LENGTH_SHORT)
                                            if (sessionInstance.P2 == userid) {
                                                val host = fsdb.collection("users").document(document.id)
                                                host.get().addOnSuccessListener { hostProfile ->
                                                    val hostUsername = hostProfile.getString("username").toString()
                                                    val hostUID = document.id
                                                    val sessionType = sessionInstance.type
                                                    invitesList.add(Invite(hostUsername, hostUID, sessionType))
                                            }
                                        }}


                                        override fun onCancelled(databaseError: DatabaseError) {
                                        }

                                    })*/



                            }


                        adapter.notifyDataSetChanged()

                    }
                    else
                        Toast.makeText(context,"Connection error.", Toast.LENGTH_SHORT)
                }



    }

    //data class Invite (val host: String, val hostID: String, val type: String)

}
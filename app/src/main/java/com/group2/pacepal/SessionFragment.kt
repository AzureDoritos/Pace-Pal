package com.group2.pacepal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.session_menu.*
//import mypackage.util.ContextExtensions.toast
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.*
import android.widget.EditText
import android.widget.LinearLayout


class SessionFragment : Fragment() {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val fsdb = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser
    private val userid = user!!.uid
    private val invitesList = mutableListOf<Invite>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //sessionRequests.addView()
        //val view = inflater.inflate(R.layout.add_ip, container, false)

        refreshInvites()


        val view = inflater.inflate(R.layout.session_menu, container, false)

        val refreshButton = view.findViewById<Button>(R.id.inviteRefresh)
        refreshButton.setOnClickListener(clickListener)


        return view
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
        invitesList.clear()
        val friendsList = fsdb.collection("users").document(userid).collection("friends")
        friendsList.get()
                .addOnSuccessListener { task ->
                    if(task.size() == 0)
                        Toast.makeText(context, "You need friends first.", Toast.LENGTH_SHORT).show()
                    for (document in task)
                    {
                        if (database.child(document.id).child("P2").toString() == userid) {
                            val host = fsdb.collection("users").document(document.id)
                            host.get().addOnSuccessListener { hostProfile ->

                            val hostUsername = hostProfile.getString("username").toString()
                            val hostUID = document.id
                            val sessionType = database.child(document.id).child("type").toString()

                            invitesList.add(Invite(hostUsername, hostUID, sessionType))
                            }
                        }
                    }

                }
                .addOnFailureListener{
                    Toast.makeText(context, "Firestore error on Friends List", Toast.LENGTH_SHORT).show()
                }
        //sessionRequests.removeAllViewsInLayout()
        if(invitesList.size != 0) {

            Toast.makeText(context, "New invites!", Toast.LENGTH_SHORT).show()
            for (invite in invitesList) {
                val newInvite = button5
                //val newInvite = Button(context)
                val displayInvite = invite.host + " " + invite.type
                newInvite.text = displayInvite
                newInvite.setOnClickListener {
                    Toast.makeText(context, invite.hostID, Toast.LENGTH_SHORT).show()
                }
                sessionRequests.addView(newInvite)
            }
        }
        //else
            //Toast.makeText(context, "No invites found!", Toast.LENGTH_SHORT).show()

    }

    data class Invite (val host: String, val hostID: String, val type: String)

}
package com.group2.pacepal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore


class FriendsFragment : Fragment() {

    private val fsdb = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser
    private val userid = user!!.uid
    private val friendsList = ArrayList<Friend>(0)
    private val adapter = FriendsAdapter(friendsList)
    //private val rtdb = FirebaseDatabase.getInstance().reference

    private lateinit var friendReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //inflates the layout xml to be displayed
        val view = inflater.inflate(R.layout.friends_list, container, false)

        //initializes the recyclerView with its adapter
        val invView = view?.findViewById(R.id.friendsList) as RecyclerView
        invView.layoutManager = LinearLayoutManager(this.context)
        invView.adapter = adapter

        //refresh button to refresh friends list
        val refreshButton = view.findViewById<Button>(R.id.friendsRefresh)
        refreshButton.setOnClickListener { refreshFriends() }

        //initial load of friends list
        refreshFriends()
        return view
    }

    companion object {
        fun newInstance(): FriendsFragment = FriendsFragment()
    }

    private val clickListener: View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.friendsRefresh -> {
                refreshFriends()
                Toast.makeText(context,"Clicked!", Toast.LENGTH_SHORT).show()
            }
            //R.id.textview2-> {
            //    Toast.makeText(this, "Clicked 2", Toast.LENGTH_SHORT).show()
            //}
        }
    }

    private fun refreshFriends() {
        friendsList.clear() //starting here on updates
        //inviteRefresh.text = "loading.."
        val intentContext = this.context!!
        val friendsFromFS = fsdb.collection("users").document(userid).collection("friends")
        friendsFromFS.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result!!.size() == 0)
                            Toast.makeText(context, "You need to add friends before you can ever have a list of them.", Toast.LENGTH_SHORT).show()
                        for (document in task.result!!) {

                            val friendGet = fsdb.collection("users").document(document.id)
                            friendGet.get().addOnSuccessListener { friendProfile ->

                                friendsList.add(Friend(
                                        "https://firebasestorage.googleapis.com/v0/b/pace-pal-ad8c4.appspot.com/o/defaultAVI.png?alt=media&token=6c9c47df-8151-4e5b-8843-3440e317346c",
                                        friendProfile.getString("username").toString(),
                                        friendProfile.getString("first") + " " + friendProfile.getString("last"),
                                        document.id,
                                        1,
                                        intentContext
                                ))
                                adapter.notifyDataSetChanged()

                            }

                            /*
                            val friendListener = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    //invScan.clear()
                                    //dataSnapshot.mapNotNullTo(invScan) { it.getValue<SessionMetadata>(SessionMetadata::class.java) }
                                    //Toast.makeText(context,dataSnapshot.child("P2").toString(),Toast.LENGTH_SHORT)
                                    //invitesList.add(Invite(document.id, document.id, dataSnapshot.child("P2").toString()))
                                        val currentUser = fsdb.collection("users").document(document.id)
                                        currentUser.get().addOnSuccessListener { currentUserProfile ->
                                            val currentUserFriend = document["userName"]//iffy here. I think this reference a single friend in the collection

                                            friendsList.add(Friend( , hostUID, sessionType,intentContext))
                                            adapter.notifyDataSetChanged()
                                            }


                                    }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    println("loadPost:onCancelled ${databaseError.toException()}")
                                }
                            } */

                            //rtdb.child("sessionManager").child("sessionIndex").child(document.id).addListenerForSingleValueEvent(invListener)

                        }


                        adapter.notifyDataSetChanged()

                    }
                    else
                        Toast.makeText(context,"Connection error.", Toast.LENGTH_SHORT)
                }



    }


}
package com.group2.pacepal

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.session_init.*

class SessionInitFragment : Fragment() {

    private val user = FirebaseAuth.getInstance().currentUser
    private val userid = user!!.uid

    private val fsdb = FirebaseFirestore.getInstance()

    private val friendsList = ArrayList<Friend>(0)
    private val adapter = FriendsAdapter(friendsList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.session_init, container, false)

        val invView = view?.findViewById(R.id.friendInvite) as RecyclerView
        invView.layoutManager = LinearLayoutManager(this.context)
        invView.adapter = adapter

        refreshFriends()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        val initState = preferences.getBoolean("initState", false)

        //if(initState)
        //    fragmentManager!!.popBackStack()


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
                                        2,
                                        intentContext
                                ))
                                adapter.notifyDataSetChanged()

                            }

                        }
                        //adapter.notifyDataSetChanged()

                    }
                    else
                        Toast.makeText(context,"Connection error.", Toast.LENGTH_SHORT)
                }



    }

    companion object {
        fun newInstance(): SessionInitFragment = SessionInitFragment()
    }

}
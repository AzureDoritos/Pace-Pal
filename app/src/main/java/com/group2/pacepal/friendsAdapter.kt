package com.group2.pacepal

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.friendview_row_item.view.*
import android.widget.Toast

class FriendsAdapter (private val friends: ArrayList<Friend>): RecyclerView.Adapter<FriendsAdapter.FriendHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder{
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.friendview_row_item,parent,false)
        return FriendHolder(inflatedView)
    }

    override fun getItemCount() = friends.size

    override fun onBindViewHolder(friend: FriendsAdapter.FriendHolder, position: Int) {
        val itemInvite = friends[position]
        friends[position].feature

        friend.bindFriend(itemInvite)
    }

    class FriendHolder(v:View) : RecyclerView.ViewHolder(v){

        private var view : View = v
        private var friend : Friend? = null

        init {
            v.setOnClickListener {this}
        }

        fun bindFriend(friend: Friend) {
            this.friend = friend
            val displayText = friend.userName

            view.friend_button.setOnClickListener{
                val parentContext = friend.feature
                view.friend_button.text = displayText
                Toast.makeText(parentContext, "WIP", Toast.LENGTH_SHORT).show(); //questionable context
               // val intent = Intent(parentContext, SessionActivity::class.java)
                //intent.putExtra("sessionID", invite.hostID)
               // parentContext.startActivity(intent)
            }
        }
    }
}
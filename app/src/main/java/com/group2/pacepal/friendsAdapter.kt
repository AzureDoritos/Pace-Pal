package com.group2.pacepal

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.friendview_row_item.view.*
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_profile.view.*
import java.security.AccessController.getContext


class FriendsAdapter (private val friends: ArrayList<Friend>): RecyclerView.Adapter<FriendsAdapter.FriendHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder{
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.friendview_row_item,parent,false)
        return FriendHolder(inflatedView)
    }

    override fun getItemCount() = friends.size

    override fun onBindViewHolder(friend: FriendsAdapter.FriendHolder, position: Int) {
        val itemFriend = friends[position]
        friends[position].feature

        friend.bindFriend(itemFriend)
    }

    class FriendHolder(v:View) : RecyclerView.ViewHolder(v), View.OnClickListener{

        private var view : View = v
        private var friend : Friend? = null

        init {
            v.setOnClickListener {this}
        }

        override fun onClick(v: View?) {
            Log.d("RecyclerView", "CLICK!")

        }

        fun bindFriend(friend: Friend) {
            this.friend = friend

            Log.d("friendListUname", friend.userName)
            view.uName.text = friend.userName
            Log.d("friendListRname", friend.userName)
            view.realName.text = friend.realName

            Picasso.with(view.context).load(friend.profilePictureURL).fit().into(view.profilePic)
            //view.profilePic
            //val displayText = friend.userName

            /*view.friend_button.setOnClickListener{
                val parentContext = friend.feature
                view.friend_button.text = displayText
                Toast.makeText(parentContext, "WIP", Toast.LENGTH_SHORT).show(); //questionable context
               // val intent = Intent(parentContext, SessionActivity::class.java)
                //intent.putExtra("sessionID", invite.hostID)
               // parentContext.startActivity(intent)
            }*/
        }
    }
}
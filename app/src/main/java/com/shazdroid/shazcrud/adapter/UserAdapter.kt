package com.shazdroid.shazcrud.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.shazdroid.shazcrud.R
import com.shazdroid.shazcrud.model.UserModel

class UserAdapter(private val userList : ArrayList<UserModel>,private val userAdapterClickHandlers: UserAdapterClickHandlers) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName.text = userList.get(position).name
        holder.deleteBtn.setOnClickListener {
            userAdapterClickHandlers.onDeleteClick(userList.get(position).id,position)
        }
        holder.root.setOnClickListener {
            userAdapterClickHandlers.onItemClick(userList.get(position).id)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun deleteUser(id: Int){
        if (userList.contains(id)){
           // userList.removeAt(userList.indexOf(id))
            notifyItemRemoved(userList.indexOf(id))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root = itemView.findViewById<CardView>(R.id.root)
        val userName = itemView.findViewById<TextView>(R.id.userItemRow_userNameTV)
        val deleteBtn = itemView.findViewById<ImageView>(R.id.userItemRow_deleteBtnIV)
    }
}
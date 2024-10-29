package com.headoncloud.personalproject.room_database_ver1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.room.util.splitToIntList
import com.headoncloud.personalproject.room_database_ver1.databinding.ItemUserBinding

class UserHolder(val binding: ItemUserBinding) : ViewHolder(binding.root){
    fun bind(user: User){
        binding.tvUsername.text = user.userName
        binding.tvPhonenumber.text = user.userPhoneNumber
        binding.tvBirth.text = user.birth
    }
}

class UserAdapter(val listUser: List<User>, val iClickItemUser: IClickItemUser) : Adapter<UserHolder>(){

    interface IClickItemUser{
        fun updateUser(user: User)

        fun deleteUser(user: User)
    }

    init {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserHolder(binding)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
        holder.binding.btnUpdate.setOnClickListener {
            iClickItemUser.updateUser(user)
        }
        holder.binding.btnDelete.setOnClickListener {
            iClickItemUser.deleteUser(user)
        }
    }
}
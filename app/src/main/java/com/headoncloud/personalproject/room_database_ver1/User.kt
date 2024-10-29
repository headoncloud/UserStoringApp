package com.headoncloud.personalproject.room_database_ver1

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var userName: String,
    var userPhoneNumber: String,
    var birth: String? = "0000"
) : Serializable{

}

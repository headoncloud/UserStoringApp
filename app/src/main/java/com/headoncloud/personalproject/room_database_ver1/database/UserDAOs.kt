package com.headoncloud.personalproject.room_database_ver1.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.headoncloud.personalproject.room_database_ver1.User

@Dao
interface UserDAOs {

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM user")
    fun getListUser(): List<User>

    @Query("SELECT * FROM user WHERE userName= :username")
    fun checkUser(username: String) : List<User>

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM user")
    fun deleteAll()

    @Query("SELECT * FROM user WHERE userName LIKE '%' || :name || '%'")
    fun findUser(name: String) : List<User>
}
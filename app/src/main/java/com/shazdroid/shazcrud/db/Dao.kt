package com.shazdroid.shazcrud.db

import androidx.room.*
import androidx.room.Dao
import com.shazdroid.shazcrud.model.UserModel

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userModel: UserModel)

    @Query("SELECT * FROM user")
    fun getAllUserList() : List<UserModel>

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteUser(id: Int)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id:Int) : UserModel

    @Update
    fun updateUser(userModel: UserModel)

}
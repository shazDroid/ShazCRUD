package com.shazdroid.shazcrud.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "user_name")
    val name: String,
    @ColumnInfo(name = "email")
    val email :String,
    @ColumnInfo(name = "phone")
    val phone: Long,
    @ColumnInfo(name = "password")
    val password : String

)
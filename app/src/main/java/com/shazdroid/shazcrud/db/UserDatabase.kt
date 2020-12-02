package com.shazdroid.shazcrud.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shazdroid.shazcrud.model.UserModel

@Database(entities = [UserModel::class],version = 1,exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao() : Dao

    companion object {
        fun getInstance(context: Context) : UserDatabase{
            var instance : UserDatabase? =null
            if (instance == null){
                instance = Room.databaseBuilder(
                    context,
                    UserDatabase::class.java,
                    "user_db"
                ).build()
            }
            return instance
        }
    }
}
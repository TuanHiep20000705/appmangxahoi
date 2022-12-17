package com.mth.example.socialmediaapp.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mth.example.socialmediaapp.model.FriendRequest

@Database(entities = [FriendRequest::class], version = 1)
abstract class FriendRequestDatabase : RoomDatabase() {
    abstract fun getFriendRequestDao(): FriendRequestDao

    companion object {
        @Volatile
        private var instance: FriendRequestDatabase? = null
        fun getInstance(context: Context): FriendRequestDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    FriendRequestDatabase::class.java,
                    "FriendRequestDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}
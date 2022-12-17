package com.mth.example.socialmediaapp.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mth.example.socialmediaapp.model.Notification

@Database(entities = [Notification::class], version = 1)
abstract class NotificationDatabase: RoomDatabase() {
    abstract fun getNotificationDao(): NotificationDao

    companion object {
        @Volatile
        private var instance: NotificationDatabase? = null
        fun getInstance(context: Context):NotificationDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(context,NotificationDatabase::class.java,"NotificationDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}
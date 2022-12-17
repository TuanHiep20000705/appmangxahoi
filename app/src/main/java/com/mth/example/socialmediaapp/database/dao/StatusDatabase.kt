package com.mth.example.socialmediaapp.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mth.example.socialmediaapp.model.Status

@Database(entities = [Status::class], version = 1)
abstract class StatusDatabase : RoomDatabase() {
    abstract fun getStatusDao(): StatusDao

    companion object {
        @Volatile
        private var instance: StatusDatabase? = null
        fun getInstance(context: Context): StatusDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, StatusDatabase::class.java, "StatusDatabase")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance!!
        }
    }
}
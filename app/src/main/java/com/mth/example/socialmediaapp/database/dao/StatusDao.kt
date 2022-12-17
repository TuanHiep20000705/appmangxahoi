package com.mth.example.socialmediaapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mth.example.socialmediaapp.model.Status

@Dao
interface StatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatus(list: List<Status>)

    @Update()
    suspend fun updateStatus(status: Status)

    @Delete()
    suspend fun deleteStatus(status: Status)

    @Query("SELECT * FROM status_table")
    fun getAllStatus(): LiveData<List<Status>>
}
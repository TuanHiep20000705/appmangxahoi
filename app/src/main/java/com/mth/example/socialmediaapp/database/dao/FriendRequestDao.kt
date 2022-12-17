package com.mth.example.socialmediaapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mth.example.socialmediaapp.model.FriendRequest

@Dao
interface FriendRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriendRequest(list: List<FriendRequest>)

    @Update
    suspend fun updateFriendRequest(friendRequest: FriendRequest)

    @Delete
    suspend fun deleteFriendRequest(friendRequest: FriendRequest)

    @Query("SELECT * FROM friend_request_table")
    fun getAllFriendRequest(): LiveData<List<FriendRequest>>
}
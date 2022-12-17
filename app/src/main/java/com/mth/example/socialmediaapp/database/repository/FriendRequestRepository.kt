package com.mth.example.socialmediaapp.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.mth.example.socialmediaapp.database.dao.FriendRequestDao
import com.mth.example.socialmediaapp.database.dao.FriendRequestDatabase
import com.mth.example.socialmediaapp.model.FriendRequest
import com.mth.example.socialmediaapp.utils.ApiUtils

class FriendRequestRepository(app: Application) {
    private val friendRequestDao: FriendRequestDao

    init {
        val friendRequestDatabase = FriendRequestDatabase.getInstance(app)
        friendRequestDao = friendRequestDatabase.getFriendRequestDao()
    }

    suspend fun insertFriendtRequest(list: List<FriendRequest>) =
        friendRequestDao.insertFriendRequest(list)

    suspend fun updateFriendRequest(friendRequest: FriendRequest) =
        friendRequestDao.updateFriendRequest(friendRequest)

    suspend fun deleteFriendRequest(friendRequest: FriendRequest) =
        friendRequestDao.deleteFriendRequest(friendRequest)

    fun getAllFriendRequest(): LiveData<List<FriendRequest>> =
        friendRequestDao.getAllFriendRequest()

    suspend fun getFriendRequestFromApi(idUser: Int) =
        ApiUtils.getData().getFriendRequestFromApi(idUser)

    suspend fun updateListStatusWhenAddFriend(idMainUser: Int, idListUser: Int) =
        ApiUtils.getData().updateListStatusWhenAddFriend(idMainUser, idListUser)
}
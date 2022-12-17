package com.mth.example.socialmediaapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.mth.example.socialmediaapp.database.repository.FriendRequestRepository
import com.mth.example.socialmediaapp.model.FriendRequest
import com.mth.example.socialmediaapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendRequestViewModel(app: Application) : ViewModel() {
    private val friendRequestRepository = FriendRequestRepository(app)
    fun insertFriendRequest(list: List<FriendRequest>) = viewModelScope.launch {
        friendRequestRepository.insertFriendtRequest(list)
    }

    fun updateFriendRequest(friendRequest: FriendRequest) = viewModelScope.launch {
        friendRequestRepository.updateFriendRequest(friendRequest)
    }

    fun deleteFriendRequest(friendRequest: FriendRequest) = viewModelScope.launch {
        friendRequestRepository.deleteFriendRequest(friendRequest)
    }

    fun getAllFriendRequest(): LiveData<List<FriendRequest>> =
        friendRequestRepository.getAllFriendRequest()

    fun getFriendRequestFromApi(idUser: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(friendRequestRepository.getFriendRequestFromApi(idUser)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun updateListStatusWhenAddFriend(idMainUser: Int, idListUser: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(
                Resource.success(
                    friendRequestRepository.updateListStatusWhenAddFriend(
                        idMainUser,
                        idListUser
                    )
                )
            )
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    class FriendRequestViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendRequestViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FriendRequestViewModel(app) as T
            }
            throw IllegalArgumentException("Unable construct FriendRequestViewModel")
        }
    }
}
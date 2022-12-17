package com.mth.example.socialmediaapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.mth.example.socialmediaapp.database.repository.UserRepository
import com.mth.example.socialmediaapp.model.User
import com.mth.example.socialmediaapp.utils.Resource
import kotlinx.coroutines.Dispatchers

class UserViewModel(app: Application) : ViewModel() {
    private val userRepository = UserRepository(app)

    fun getUserFromApi() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.getUserFromApi()))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error"))
        }
    }

    fun registerUser(user: User) = liveData(Dispatchers.IO) {
        emit(Resource.loading((null)))
        try {
            emit(Resource.success(userRepository.registerUser(user)))
        } catch (e: Exception) {
            emit((Resource.error(null, e.message ?: "error")))
        }
    }

    class UserViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(app) as T
            }
            throw IllegalArgumentException("Unable construct userViewModel")
        }
    }
}
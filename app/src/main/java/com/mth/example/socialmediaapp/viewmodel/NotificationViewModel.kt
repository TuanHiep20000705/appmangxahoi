package com.mth.example.socialmediaapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.mth.example.socialmediaapp.database.repository.NotificationRepository
import com.mth.example.socialmediaapp.model.Notification
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(app: Application) : ViewModel() {
    private val notificationRepository = NotificationRepository(app)
    fun insertNotification(list: List<Notification>) = viewModelScope.launch {
        notificationRepository.insertNotification(list)
    }

    fun updateNotification(notification: Notification) = viewModelScope.launch {
        notificationRepository.updateNotification(notification)
    }

    fun deleteNotification(notification: Notification) = viewModelScope.launch {
        notificationRepository.deleteNotification(notification)
    }

    fun getAllNotification(): LiveData<List<Notification>> =
        notificationRepository.getAllNotification()

    fun getNotificationFromApi(idUserReceive: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(notificationRepository.getNotificationFromApi(idUserReceive)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error"))
        }
    }

    class NotificationViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotificationViewModel(app) as T
            }
            throw IllegalArgumentException("Unable construct notificationViewModel")
        }
    }
}
package com.mth.example.socialmediaapp.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.mth.example.socialmediaapp.database.dao.NotificationDao
import com.mth.example.socialmediaapp.database.dao.NotificationDatabase
import com.mth.example.socialmediaapp.model.Notification
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.utils.ApiUtils

class NotificationRepository(app: Application) {
    private val notificationDao: NotificationDao

    init {
        val notificationDatabase = NotificationDatabase.getInstance(app)
        notificationDao = notificationDatabase.getNotificationDao()
    }

    suspend fun insertNotification(list: List<Notification>) =
        notificationDao.insertNotification(list)

    suspend fun updateNotification(notification: Notification) =
        notificationDao.updateNotification(notification)

    suspend fun deleteNotification(notification: Notification) =
        notificationDao.deleteNotification(notification)

    fun getAllNotification(): LiveData<List<Notification>> = notificationDao.getAllNotification()

    suspend fun getNotificationFromApi(idUserReceive: Int) =
        ApiUtils.getData().getListNotification(idUserReceive)
}
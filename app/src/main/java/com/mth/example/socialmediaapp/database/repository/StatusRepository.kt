package com.mth.example.socialmediaapp.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.mth.example.socialmediaapp.database.dao.StatusDao
import com.mth.example.socialmediaapp.database.dao.StatusDatabase
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.utils.ApiUtils
import okhttp3.MultipartBody

class StatusRepository(app: Application) {
    private val statusDao: StatusDao

    init {
        val statusDatabase = StatusDatabase.getInstance(app)
        statusDao = statusDatabase.getStatusDao()
    }

    suspend fun insertStatus(list: List<Status>) = statusDao.insertStatus(list)
    suspend fun updateStatus(status: Status) = statusDao.updateStatus(status)
    suspend fun deleteStatus(status: Status) = statusDao.deleteStatus(status)
    fun getAllStatus(): LiveData<List<Status>> = statusDao.getAllStatus()

    suspend fun getStatusFromApi(idUser: Int) = ApiUtils.getData().getStatusFromApi(idUser)

    suspend fun likeStatus(id: Int, idStatus: Int, idUser: Int, stateLiked: Int, soLike: Int) =
        ApiUtils.getData().likeStatus(id, idStatus, idUser, stateLiked, soLike)

    suspend fun postStatus(
        idUserPost: Int,
        tenUserPost: String,
        avatUserPost: String,
        content: String,
        imgContent: String,
        ngayPost: String
    ) =
        ApiUtils.getData()
            .postStatus(idUserPost, tenUserPost, avatUserPost, content, imgContent, ngayPost)

    suspend fun uploadImgStatus(photo: MultipartBody.Part) =
        ApiUtils.getData().uploadImgStatus(photo)

    suspend fun deleteStatusInApi(id: Int, idStatus: Int) =
        ApiUtils.getData().deleteStatusInApi(id, idStatus)

    suspend fun updateContentStatusInApi(idStatus: Int, content: String) =
        ApiUtils.getData().updateContentStatusInApi(idStatus, content)

    suspend fun updateImgContentStatusInApi(idStatus: Int, content: String, imgContent: String) =
        ApiUtils.getData().updateImgContentStatusInApi(idStatus, content, imgContent)
}
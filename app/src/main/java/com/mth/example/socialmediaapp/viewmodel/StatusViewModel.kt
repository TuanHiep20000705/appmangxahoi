package com.mth.example.socialmediaapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.mth.example.socialmediaapp.database.repository.StatusRepository
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class StatusViewModel(app: Application) : ViewModel() {
    private val statusRepository = StatusRepository(app)
    fun insertStatus(list: List<Status>) = viewModelScope.launch {
        statusRepository.insertStatus(list)
    }

    fun updateStatus(status: Status) = viewModelScope.launch {
        statusRepository.updateStatus(status)
    }

    fun deleteStatus(status: Status) = viewModelScope.launch {
        statusRepository.deleteStatus(status)
    }

    fun getAllStatus(): LiveData<List<Status>> = statusRepository.getAllStatus()

    fun getStatusFromApi(idUser: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(statusRepository.getStatusFromApi(idUser)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error"))
        }
    }

    fun likeStatus(id: Int, idStatus: Int, idUser: Int, stateLiked: Int, soLike: Int) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                emit(
                    Resource.success(
                        statusRepository.likeStatus(
                            id,
                            idStatus,
                            idUser,
                            stateLiked,
                            soLike
                        )
                    )
                )
            } catch (e: Exception) {
                emit(Resource.error(null, e.message ?: "error"))
            }
        }

    fun postStatus(
        idUserPost: Int,
        tenUserPost: String,
        avatUserPost: String,
        content: String,
        imgContent: String,
        ngayPost: String
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(
                Resource.success(
                    statusRepository.postStatus(
                        idUserPost,
                        tenUserPost,
                        avatUserPost,
                        content,
                        imgContent,
                        ngayPost
                    )
                )
            )
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error"))
        }
    }

    fun uploadImgStatus(photo: MultipartBody.Part) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(statusRepository.uploadImgStatus(photo)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error"))
        }
    }

    fun deleteStatusInApi(id: Int, idStatus: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(statusRepository.deleteStatusInApi(id, idStatus)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error"))
        }
    }

    fun updateContentStatusInApi(idStatus: Int, content: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(statusRepository.updateContentStatusInApi(idStatus, content)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error"))
        }
    }

    fun updateImgContentStatusInApi(idStatus: Int, content: String, imgContent: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                emit(
                    Resource.success(
                        statusRepository.updateImgContentStatusInApi(
                            idStatus,
                            content,
                            imgContent
                        )
                    )
                )
            } catch (e: Exception) {
                emit(Resource.error(null, e.message ?: "error"))
            }
        }

    class StatusViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatusViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StatusViewModel(app) as T
            }
            throw IllegalArgumentException("Unable construct StatusViewModel")
        }
    }
}
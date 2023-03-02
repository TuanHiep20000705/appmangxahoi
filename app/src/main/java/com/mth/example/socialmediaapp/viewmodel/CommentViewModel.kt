package com.mth.example.socialmediaapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.mth.example.socialmediaapp.database.repository.CommentRepository
import com.mth.example.socialmediaapp.utils.Resource
import kotlinx.coroutines.Dispatchers

class CommentViewModel(app: Application) : ViewModel() {
    private val commentRepository: CommentRepository = CommentRepository(app)
    fun getCommentFromApi(idStatusCmt: Int) = liveData(Dispatchers.IO) {

        emit(Resource.loading(null))
        try {

            emit(Resource.success(commentRepository.getCommentFromApi(idStatusCmt)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error"))
        }
    }

    class CommentViewHolderFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CommentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable construct commentViewModel")
        }
    }
}
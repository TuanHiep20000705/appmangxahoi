package com.mth.example.socialmediaapp.database.repository

import android.app.Application
import com.mth.example.socialmediaapp.utils.ApiUtils

class CommentRepository(app: Application) {
    suspend fun getCommentFromApi(idStatusCmt: Int) = ApiUtils.getData().getListComment(idStatusCmt)
}
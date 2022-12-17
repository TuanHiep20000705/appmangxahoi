package com.mth.example.socialmediaapp.database.repository

import android.app.Application
import com.mth.example.socialmediaapp.model.User
import com.mth.example.socialmediaapp.utils.ApiUtils

class UserRepository(app:Application) {
    suspend fun getUserFromApi() = ApiUtils.getData().getAllUser()

    suspend fun registerUser(user: User) = ApiUtils.getData().registerUser(user.UserName,user.PassWord,user.TenUser,user.QueQuan,user.Sdt,user.Birthday)
}
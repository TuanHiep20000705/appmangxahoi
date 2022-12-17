package com.mth.example.socialmediaapp.utils

import com.mth.example.socialmediaapp.retrofit.DataClient
import com.mth.example.socialmediaapp.retrofit.RetrofitConfig

class ApiUtils {
    companion object {
        const val baseUrl: String = "https://hostcuahiep123.000webhostapp.com/socialmedia/"
        fun getData(): DataClient {
            return RetrofitConfig.getClient(baseUrl).create(DataClient::class.java)
        }
    }
}
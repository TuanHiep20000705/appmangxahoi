package com.mth.example.socialmediaapp.retrofit

import com.mth.example.socialmediaapp.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface DataClient {
    @GET("getalluser.php")
    suspend fun getAllUser(): List<User>

    @FormUrlEncoded
    @POST("registeruser.php")
    suspend fun registerUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("tenuser") tenuser: String,
        @Field("quequan") quequan: String,
        @Field("sdt") sdt: Int,
        @Field("date") date: String
    ): String

    @FormUrlEncoded
    @POST("getliststatus.php")
    suspend fun getStatusFromApi(@Field("iduser") iduser: Int): List<Status>

    @FormUrlEncoded
    @POST("likestatus.php")
    suspend fun likeStatus(
        @Field("id") id: Int,
        @Field("idstatus") idStatus: Int,
        @Field("iduser") idUser: Int,
        @Field("stateliked") stateLiked: Int,
        @Field("solike") solike: Int
    ): String

    @FormUrlEncoded()
    @POST("poststatus.php")
    suspend fun postStatus(
        @Field("iduserpost") iduserpost: Int,
        @Field("tenuserpost") tenuserpost: String,
        @Field("avatuserpost") avatuserpost: String,
        @Field("content") content: String,
        @Field("imgcontent") imgcontent: String,
        @Field("ngaypost") ngaypost: String
    ): String

    @FormUrlEncoded()
    @POST("deletestatus.php")
    suspend fun deleteStatusInApi(
        @Field("id") id: Int,
        @Field("idstatus") idstatus: Int
    ): String

    @FormUrlEncoded()
    @POST("updatecontentstatus.php")
    suspend fun updateContentStatusInApi(
        @Field("idstatus") idstatus: Int,
        @Field("content") content: String
    ): String

    @FormUrlEncoded()
    @POST("updateimgcontentstatus.php")
    suspend fun updateImgContentStatusInApi(
        @Field("idstatus") idstatus: Int,
        @Field("content") content: String,
        @Field("imgcontent") imgcontent: String
    ): String

    @Multipart
    @POST("uploadimgstatus.php")
    suspend fun uploadImgStatus(@Part photo: MultipartBody.Part): String

    @FormUrlEncoded
    @POST("getallrequest.php")
    suspend fun getFriendRequestFromApi(@Field("iduser") iduser: Int): List<FriendRequest>

    @FormUrlEncoded()
    @POST("setdatasearchfriend.php")
    fun setDataSearchFriend(
        @Field("iduser") iduser: Int,
        @Field("tenmainuser") tenmainuser: String,
        @Field("quequanmainuser") quequanmainuser: String
    ): Call<String>

    @FormUrlEncoded()
    @POST("getdatasearchfriend.php")
    fun getDataSearchFriend(@Field("iduser") iduser: Int): Call<List<SearchFriend>>

    @FormUrlEncoded()
    @POST("updatestatefriend.php")
    fun updateStateFriend(
        @Field("id") id: Int,
        @Field("statefriend") statefriend: Int
    ): Call<String>

    @FormUrlEncoded()
    @POST("updatestatefriendbyidmainandidlist.php")
    fun updateStateFriendByIdMainAndIdList(
        @Field("idmainuser") idmainuser: Int,
        @Field("idlistuser") idlistuser: Int,
        @Field("statefriend") statefriend: Int
    ): Call<String>

    @FormUrlEncoded()
    @POST("insertfriendrequest.php")
    fun insertFriendRequest(
        @Field("idusersend") idusersend: Int,
        @Field("tenusersend") tenusersend: String,
        @Field("anhusersend") anhusersend: String,
        @Field("iduserreceive") iduserreceive: Int,
        @Field("ngayrequest") ngayrequest: String
    ): Call<String>

    @FormUrlEncoded()
    @POST("deletefriendrequest.php")
    fun deleteFriendRequest(
        @Field("idusersend") idusersend: Int,
        @Field("iduserreceive") iduserreceive: Int
    ): Call<String>

    @FormUrlEncoded()
    @POST("insertfriend.php")
    fun insertFriend(
        @Field("idmainuser") idmainuser: Int,
        @Field("idlistuser") idlistuser: Int,
        @Field("tenlistuser") tenlistuser: String,
        @Field("anhlistuser") anhlistuser: String,
        @Field("tenmainuser") tenmainuser: String,
        @Field("anhmainuser") anhmainuser: String
    ): Call<String>

    @FormUrlEncoded()
    @POST("updateliststatuswhenaddfriend.php")
    suspend fun updateListStatusWhenAddFriend(
        @Field("idmainuser") idmainuser: Int,
        @Field("idlistuser") idlistuser: Int
    ): String

    @FormUrlEncoded()
    @POST("getlistcomment.php")
    suspend fun getListComment(@Field("idstatuscmt") idstatuscmt: Int): List<Comment>

    @FormUrlEncoded()
    @POST("insertcomment.php")
    fun insertComment(
        @Field("idstatuscmt") idstatuscmt: Int,
        @Field("idusercmt") idusercmt: Int,
        @Field("tenusercmt") tenusercmt: String,
        @Field("avatusercmt") avatusercmt: String,
        @Field("ngaychat") ngaychat: String,
        @Field("noidung") noidung: String
    ): Call<List<Comment>>

    @FormUrlEncoded()
    @POST("deletecomment.php")
    fun deleteComment(
        @Field("idcmt") idcmt: Int,
        @Field("idstatuscmt") idstatuscmt: Int
    ): Call<List<Comment>>

    @FormUrlEncoded()
    @POST("updateprofile.php")
    fun updateProfile(
        @Field("iduser") iduser: Int,
        @Field("tenuser") tenuser: String,
        @Field("birthday") birthday: String,
        @Field("quequan") quequan: String,
        @Field("mota") mota: String,
        @Field("sdt") sdt: Int,
        @Field("hinhanh") hinhanh: String,
        @Field("anhbia") anhbia: String
    ): Call<String>

    @FormUrlEncoded()
    @POST("getlistnotification.php")
    suspend fun getListNotification(@Field("iduserreceive") iduserreceive: Int): List<Notification>

    @FormUrlEncoded()
    @POST("insertnotification.php")
    fun insertNotificationToApi(
        @Field("idprimarystatus") idprimarystatus: Int,
        @Field("idstatus") idstatus: Int,
        @Field("idusersend") idusersend: Int,
        @Field("iduserreceive") iduserreceive: Int,
        @Field("tenusersend") tenusersend: String,
        @Field("anhusersend") anhusersend: String,
        @Field("ngaysend") ngaysend: String,
        @Field("notitype") notitype: Int
    ): Call<String>

    @FormUrlEncoded()
    @POST("huyketban.php")
    fun huyKetBan(
        @Field("idmainuser") idmainuser: Int,
        @Field("idlistuser") idlistuser: Int
    ): Call<String>

    @FormUrlEncoded()
    @POST("getdatastatus.php")
    fun getDataStatus(
        @Field("idstatus") idstatus: Int,
        @Field("idmainuser") idmainuser: Int
    ): Call<Status>
}
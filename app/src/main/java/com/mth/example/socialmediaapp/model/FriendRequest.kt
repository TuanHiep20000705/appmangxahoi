package com.mth.example.socialmediaapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friend_request_table")
class FriendRequest(
    @ColumnInfo(name = "idusersend_col") var idusersend: Int = 0,
    @ColumnInfo(name = "tenusersend_col") var tenusersend: String = "",
    @ColumnInfo(name = "anhusersend_col") var anhusersend: String = "",
    @ColumnInfo(name = "iduserreceive_col") var iduserreceive: Int = 0,
    @ColumnInfo(name = "ngayrequest_col") var ngayrequest: String = ""
) : java.io.Serializable, Comparable<FriendRequest> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "friend_request_id_col")
    var id: Int = 0
    override fun compareTo(other: FriendRequest): Int {
        if (this.id < other.id) {
            return 1
        }
        if (this.id > other.id){
            return -1
        }
        return 0
    }
}
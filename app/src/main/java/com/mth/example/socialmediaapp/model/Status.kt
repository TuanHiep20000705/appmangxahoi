package com.mth.example.socialmediaapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "status_table")
class Status(
    @ColumnInfo(name = "idstatus_col") var idstatus: Int = 0,
    @ColumnInfo(name = "iduserpost_col") var iduserpost: Int = 0,
    @ColumnInfo(name = "iduserpost2_col") var iduserpost2: Int = 0,
    @ColumnInfo(name = "iduserview_col") var iduserview: Int = 0,
    @ColumnInfo(name = "iduserview2_col") var iduserview2: Int = 0,
    @ColumnInfo(name = "tenuserpost_col") var tenuserpost: String = "",
    @ColumnInfo(name = "avatuserpost_col") var avatuserpost: String = "",
    @ColumnInfo(name = "content_col") var content: String = "",
    @ColumnInfo(name = "imgcontent_col") var imgcontent: String = "",
    @ColumnInfo(name = "solike_col") var solike: Int = 0,
    @ColumnInfo(name = "socomment_col") var socomment: Int = 0,
    @ColumnInfo(name = "ngaypost_col") var ngaypost: String = "",
    @ColumnInfo(name = "stateliked_col") var stateliked: Int = 0
) : java.io.Serializable,Comparable<Status> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "status_id_col")
    var id: Int = 0
    override fun compareTo(other: Status): Int {
        if (this.id < other.id) {
            return 1
        }
        if (this.id > other.id){
            return -1
        }
        return 0
    }
}
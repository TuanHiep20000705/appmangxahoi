package com.mth.example.socialmediaapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
class Notification(
    @ColumnInfo(name = "idprimarystatus_col") var idprimarystatus: Int = 0,
    @ColumnInfo(name = "idstatus_col") var idstatus: Int = 0,
    @ColumnInfo(name = "idusersend_col") var idusersend: Int = 0,
    @ColumnInfo(name = "iduserreceive_col") var iduserreceive: Int = 0,
    @ColumnInfo(name = "tenusersend_col") var tenusersend: String = "",
    @ColumnInfo(name = "anhusersend_col") var anhusersend: String = "",
    @ColumnInfo(name = "ngaysend_col") var ngaysend: String = "",
    @ColumnInfo(name = "notitype_col") var notitype: Int = 0
):Comparable<Notification> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notification_id_col")
    var id: Int = 0
    override fun compareTo(other: Notification): Int {
        if (this.id < other.id) {
            return 1
        }
        if (this.id > other.id){
            return -1
        }
        return 0
    }
}
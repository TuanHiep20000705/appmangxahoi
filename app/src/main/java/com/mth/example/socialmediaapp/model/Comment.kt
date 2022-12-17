package com.mth.example.socialmediaapp.model

class Comment(
    var idstatuscmt: Int = 0,
    var idusercmt: Int = 0,
    var tenusercmt: String = "",
    var avatusercmt: String = "",
    var ngaychat: String = "",
    var noidung: String = ""
):Comparable<Comment> {
    var idcmt: Int = 0
    override fun compareTo(other: Comment): Int {
        if (this.idcmt < other.idcmt) {
            return 1
        }
        if (this.idcmt > other.idcmt){
            return -1
        }
        return 0
    }
}
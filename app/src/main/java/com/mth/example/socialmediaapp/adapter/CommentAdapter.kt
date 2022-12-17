package com.mth.example.socialmediaapp.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.model.Comment

class CommentAdapter(
    private val context: Context,
    private val onDelete: (Comment) -> Unit

) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var commentList: List<Comment> = listOf()
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    }
    private val iduser: Int by lazy {
        sharedPreferences.getInt("iduser", 1)
    }

    fun setData(list: List<Comment>) {
        this.commentList = list
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatUserItemComment: ImageView =
            itemView.findViewById(R.id.img_avt_user_item_comment)
        private val txtTenUserItemComment: TextView =
            itemView.findViewById(R.id.txt_ten_user_item_comment)
        private val txtNgayChat: TextView = itemView.findViewById(R.id.txt_ngay_chat_item_comment)
        private val butDelete: ImageButton =
            itemView.findViewById(R.id.but_delete_comment_item_comment)
        private val txtNoiDung: TextView = itemView.findViewById(R.id.txt_noi_dung_item_comment)
        fun onBind(comment: Comment) {
            if (comment.avatusercmt.length > 0) {
                Glide.with(context).load(comment.avatusercmt).into(avatUserItemComment)
            } else {
                avatUserItemComment.setImageResource(R.drawable.default_avatar)
            }
            if (comment.idusercmt == iduser) {
                butDelete.visibility = View.VISIBLE
            } else {
                butDelete.visibility = View.INVISIBLE
            }
            txtTenUserItemComment.setText(comment.tenusercmt)
            txtNgayChat.setText(comment.ngaychat)
            butDelete.setOnClickListener { onDelete(comment) }
            txtNoiDung.setText(comment.noidung)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.onBind(commentList[position])
    }

    override fun getItemCount(): Int = commentList.size
}
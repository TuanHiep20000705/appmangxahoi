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
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.ui.fragment.HomeFragment

class StatusAdapter(
    private val context: Context,
    private val onDelete: (Status) -> Unit,
    private val onEdit: (Status) -> Unit,
    private val onLike: (Status) -> Unit,
    private val onComment: (Status) -> Unit
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {
    private var statusList: List<Status> = listOf()

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    }
    private val iduser: Int by lazy {
        sharedPreferences.getInt("iduser", 1)
    }

    fun setData(list: List<Status>) {
        this.statusList = list
        notifyDataSetChanged()
    }

    inner class StatusViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        private val avatUserPost: ImageView =
            itemview.findViewById(R.id.img_avt_user_post_item_status)
        private val tenUserPost: TextView =
            itemview.findViewById(R.id.txt_ten_user_post_item_status)
        private val ngayPost: TextView = itemview.findViewById(R.id.txt_ngay_post_item_status)
        private val butEditStatus: ImageButton =
            itemview.findViewById(R.id.but_edit_status_item_status)
        private val butDeleteStatus: ImageButton =
            itemview.findViewById(R.id.but_delete_status_item_status)
        private val content: TextView = itemview.findViewById(R.id.txt_content_status_item_status)
        private val imgContent: ImageView = itemview.findViewById(R.id.img_hinh_status_item_status)
        private val soLikes: TextView = itemview.findViewById(R.id.txt_so_like_item_status)
        private val soComments: TextView = itemview.findViewById(R.id.txt_so_comment_item_status)
        private val butLikeStatus: ImageButton = itemview.findViewById(R.id.but_like_item_status)
        private val butCommentStatus: ImageButton =
            itemview.findViewById(R.id.but_comment_item_status)

        fun onBind(status: Status, position: Int) {
            tenUserPost.setText(status.tenuserpost)
            ngayPost.setText(status.ngaypost)

            if (status.content.length == 0) {
                content.visibility = View.GONE

            } else {
                content.visibility = View.VISIBLE
                content.setText(status.content)
            }

            if (status.avatuserpost.length == 0) {
                avatUserPost.setImageResource(R.drawable.default_avatar)
            } else {
                Glide.with(itemView.context).load(status.avatuserpost).into(avatUserPost)
            }
            if (status.imgcontent.length == 0) {
                imgContent.visibility = View.GONE

            } else {
                imgContent.visibility = View.VISIBLE
                Glide.with(itemView.context).load(status.imgcontent).into(imgContent)
            }

            if (status.iduserpost == iduser) {
                butEditStatus.visibility = View.VISIBLE
            } else {
                butEditStatus.visibility = View.GONE
            }

            if (status.solike.toString().toInt() > 1) {
                soLikes.setText("${status.solike} Likes")
            } else {
                soLikes.setText("${status.solike} Like")
            }
            if (status.socomment.toString().toInt() > 1) {
                soComments.setText("${status.socomment} Commments")
            } else {
                soComments.setText("${status.socomment} Comment")
            }
            if (status.stateliked == 1) {
                butLikeStatus.setImageResource(R.drawable.icon_like)
            } else {
                butLikeStatus.setImageResource(R.drawable.icon_unlike)
            }

            butLikeStatus.setOnClickListener {
                onLike.invoke(status)
                if (status.stateliked == 1) {
                    status.stateliked = 0
                    val soLikeMoi = status.solike - 1
                    status.solike = soLikeMoi
                } else {
                    status.stateliked = 1
                    val soLikeMoi = status.solike + 1
                    status.solike = soLikeMoi
                }
                notifyItemChanged(position)
            }
            butEditStatus.setOnClickListener { onEdit(status) }
            butDeleteStatus.setOnClickListener { onDelete(status) }
            butCommentStatus.setOnClickListener { onComment(status) }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.onBind(statusList[position], position)
    }

    override fun getItemCount(): Int = statusList.size
}
package com.mth.example.socialmediaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.model.FriendRequest

class FriendRequestAdapter(
    private val context: Context,
    private val onClick: (FriendRequest) -> Unit,
    private val onDongY: (FriendRequest) -> Unit,
    private val onXoa: (FriendRequest) -> Unit
) : RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>() {
    private var friendRequestList: List<FriendRequest> = listOf()
    fun setData(list: List<FriendRequest>) {
        this.friendRequestList = list
        notifyDataSetChanged()
    }

    inner class FriendRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatUserSend: ImageView =
            itemView.findViewById(R.id.img_avt_item_friend_request)
        private val tenUserSend: TextView =
            itemView.findViewById(R.id.txt_ten_user_send_item_friend_request)
        private val ngaySend: TextView = itemView.findViewById(R.id.txt_ngay_send_friend_request)
        private val butDongY: Button = itemView.findViewById(R.id.but_dong_y_item_friend_request)
        private val butXoa: Button = itemView.findViewById(R.id.but_xoa_item_friend_request)
        private val rltLayout: RelativeLayout =
            itemView.findViewById(R.id.rlt_layout_item_friend_request)

        fun onBind(friendRequest: FriendRequest) {
            tenUserSend.setText(friendRequest.tenusersend)
            ngaySend.setText(friendRequest.ngayrequest)
            butDongY.setOnClickListener { onDongY(friendRequest) }
            butXoa.setOnClickListener { onXoa(friendRequest) }
            rltLayout.setOnClickListener { onClick(friendRequest) }
            if (friendRequest.anhusersend.length > 0) {
                Glide.with(context).load(friendRequest.anhusersend).into(avatUserSend)
            } else {
                avatUserSend.setImageResource(R.drawable.default_avatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_friend_request, parent, false)
        return FriendRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.onBind(friendRequestList[position])
    }

    override fun getItemCount(): Int = friendRequestList.size
}
package com.mth.example.socialmediaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.model.Notification

class NotificationAdapter(
    private val context: Context,
    private val onClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    private var listNotifcation: List<Notification> = listOf()
    fun setData(list: List<Notification>) {
        this.listNotifcation = list
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgAvatUser: ImageView =
            itemView.findViewById(R.id.img_avt_user_notication_item)
        private val tenUser: TextView = itemView.findViewById(R.id.txt_ten_user_notification_item)
        private val content: TextView = itemView.findViewById(R.id.txt_content_notification_item)
        private val ngaySend: TextView = itemView.findViewById(R.id.txt_ngay_send_notification_item)
        private val iconLike: ImageView =
            itemView.findViewById(R.id.img_icon_like_notification_item)
        private val rltLayout: RelativeLayout = itemView.findViewById(R.id.rlt_layout_item_notification)
        fun onBind(notification: Notification) {
            if (notification.anhusersend.length > 0) {
                Glide.with(context).load(notification.anhusersend).into(imgAvatUser)
            } else {
                imgAvatUser.setImageResource(R.drawable.default_avatar)
            }
            tenUser.setText(notification.tenusersend)
            ngaySend.setText(notification.ngaysend)
            if (notification.notitype == 1) {
                content.setText(R.string.txt_da_thich)
                iconLike.setImageResource(R.drawable.icon_like)
            } else {
                content.setText(R.string.txt_da_binh_luan)
                iconLike.setImageResource(R.drawable.ic_comment)
            }
            rltLayout.setOnClickListener { onClick(notification) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.onBind(listNotifcation[position])
    }

    override fun getItemCount(): Int = listNotifcation.size
}
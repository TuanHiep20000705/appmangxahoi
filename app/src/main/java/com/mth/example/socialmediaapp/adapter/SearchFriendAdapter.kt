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
import com.mth.example.socialmediaapp.model.SearchFriend

class SearchFriendAdapter(
    private val context: Context,
    private val onClick: (SearchFriend) -> Unit,
    private val onClickThemBan: (SearchFriend,Button) -> Unit
) : RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder>() {
    private var searchFriendList: List<SearchFriend> = listOf()
    fun setData(list: List<SearchFriend>) {
        this.searchFriendList = list
        notifyDataSetChanged()
    }

    inner class SearchFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgAvatUserSearch: ImageView = itemView.findViewById(R.id.img_avt_user_search)
        private val txtTenUserSearch: TextView = itemView.findViewById(R.id.txt_ten_user_search)
        private val txtQueQuanUserSearch: TextView =
            itemView.findViewById(R.id.txt_que_quan_user_search)
        private val butThemBan: Button = itemView.findViewById(R.id.but_them_ban_be_search)
        private val rltLayoutUserSearch: RelativeLayout =
            itemView.findViewById(R.id.rlt_layout_item_search)

        fun onBind(searchFriend: SearchFriend) {
            txtTenUserSearch.setText(searchFriend.tenlistuser)
            txtQueQuanUserSearch.setText(searchFriend.quequanlistuser)
            butThemBan.setOnClickListener { onClickThemBan(searchFriend,butThemBan) }
            rltLayoutUserSearch.setOnClickListener { onClick(searchFriend) }
            if (searchFriend.statefriend == 0) {
                butThemBan.setText(R.string.but_ket_ban)
            } else if (searchFriend.statefriend == 1) {
                butThemBan.setText(R.string.but_ban_be)
            } else {
                butThemBan.setText(R.string.but_dang_gui_ket_ban)
            }
            if (searchFriend.anhlistuser.length > 0) {
                Glide.with(context).load(searchFriend.anhlistuser).into(imgAvatUserSearch)
            } else {
                imgAvatUserSearch.setImageResource(R.drawable.default_avatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFriendViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_user_search, parent, false)
        return SearchFriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchFriendViewHolder, position: Int) {
        holder.onBind(searchFriendList[position])
    }

    override fun getItemCount(): Int = searchFriendList.size
}
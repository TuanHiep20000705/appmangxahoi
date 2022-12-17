package com.mth.example.socialmediaapp.ui.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.adapter.StatusAdapter
import com.mth.example.socialmediaapp.model.User
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.utils.Status.SUCCESS
import com.mth.example.socialmediaapp.utils.Status.ERROR
import com.mth.example.socialmediaapp.utils.Status.LOADING
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import com.mth.example.socialmediaapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_info_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.Collections

class InfoProfileActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(this.application)
        )[UserViewModel::class.java]
    }

    private val idListUser: Int by lazy {
        intent.getIntExtra("dulieuidusersearch", 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_profile)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        swipe_refresh_info_profile.setOnRefreshListener { refreshData() }
        but_back_info_profile.setOnClickListener { finish() }
    }

    private fun initControls() {
        refreshData()
    }

    private fun refreshData() {
        userViewModel.getUserFromApi().observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        swipe_refresh_info_profile.isRefreshing = false
                        resource.data?.let { users ->
                            for (user in users) {
                                if (idListUser == user.Id) {
                                    setInfo(user)
                                    break
                                }
                            }
                        }
                    }
                    ERROR -> {
                        swipe_refresh_info_profile.isRefreshing = false
                        Toast.makeText(
                            this,
                            resources.getString(R.string.txt_ketnoimang),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    LOADING -> {
                        swipe_refresh_info_profile.isRefreshing = true
                    }
                }
            }
        }
    }


    private fun setInfo(user: User) {
        if (user.AnhBia.length > 0) {
            Glide.with(this).load(user.AnhBia).into(img_anh_bia_info_profile)
        }
        if (user.HinhAnh.length > 0) {
            Glide.with(this).load(user.HinhAnh).into(img_avt_user_info_profile)
        }
        if (user.MoTa.length > 0) {
            txt_mo_ta_info_profile.setText(user.MoTa)
        }
        txt_ten_user_info_profile.setText(user.TenUser)
        txt_address_info_profile.setText(user.QueQuan)
        txt_birthday_info_profile.setText(user.Birthday)
        txt_phone_info_profile.setText(user.Sdt.toString())
    }

}
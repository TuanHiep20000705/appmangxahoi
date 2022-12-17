package com.mth.example.socialmediaapp.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.adapter.NotificationAdapter
import com.mth.example.socialmediaapp.model.Notification
import com.mth.example.socialmediaapp.ui.activities.MoveFromNotifyToComment
import com.mth.example.socialmediaapp.utils.ApiUtils
import com.mth.example.socialmediaapp.utils.Status.SUCCESS
import com.mth.example.socialmediaapp.utils.Status.ERROR
import com.mth.example.socialmediaapp.utils.Status.LOADING
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private val notificationViewModel: NotificationViewModel by lazy {
        ViewModelProvider(
            this,
            NotificationViewModel.NotificationViewModelFactory(requireActivity().application)
        )[NotificationViewModel::class.java]
    }
    private val notificationAdapter: NotificationAdapter by lazy {
        NotificationAdapter(requireContext(), onItemClick)
    }

    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    }
    private val iduser: Int by lazy {
        sharedPreferences.getInt("iduser", 1)
    }
    private val moveFromNotifyToComment: MoveFromNotifyToComment by lazy {
        activity as MoveFromNotifyToComment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        swipe_refresh_notification.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        notificationViewModel.getNotificationFromApi(iduser).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        swipe_refresh_notification.isRefreshing = false
                        resource.data?.let { notifications ->
                            notificationViewModel.insertNotification(notifications)
                            notificationViewModel.getAllNotification().observe(viewLifecycleOwner) {
                                for (item in it) {
                                    var x = 0
                                    for (item2 in notifications) {
                                        if (item.id != item2.id) {
                                            x++
                                        }
                                    }
                                    if (x == notifications.size) {
                                        notificationViewModel.deleteNotification(item)
                                    }
                                }
                                if (it.size == 0) {
                                    txt_trong_notification_frag.visibility = View.VISIBLE
                                    Collections.sort(it)
                                    notificationAdapter.setData(it)
                                } else {
                                    txt_trong_notification_frag.visibility = View.GONE
                                    Collections.sort(it)
                                    notificationAdapter.setData(it)
                                }
                            }
                        }
                    }
                    ERROR -> {
                        swipe_refresh_notification.isRefreshing = false
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.txt_ketnoimang),
                            Toast.LENGTH_SHORT
                        ).show()
                        notificationViewModel.getAllNotification().observe(viewLifecycleOwner) {
                            if (it.size == 0) {
                                txt_trong_notification_frag.visibility = View.VISIBLE
                                Collections.sort(it)
                                notificationAdapter.setData(it)
                            } else {
                                txt_trong_notification_frag.visibility = View.GONE
                                Collections.sort(it)
                                notificationAdapter.setData(it)
                            }
                        }
                        Log.d("AAA", it.message.toString())
                    }
                    LOADING -> {
                        swipe_refresh_notification.isRefreshing = true
                    }
                }
            }
        }
    }

    private fun initControls() {
        rcv_notification.setHasFixedSize(true)
        rcv_notification.layoutManager = LinearLayoutManager(requireContext())
        rcv_notification.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
        rcv_notification.adapter = notificationAdapter
        refreshData()
    }

    private val onItemClick: (Notification) -> Unit = {
        ApiUtils.getData().getDataStatus(it.idstatus,iduser).enqueue(object :Callback<Status>{
            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                if (response.isSuccessful){
                    moveFromNotifyToComment.initMoveFromNotyToComment(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Status>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.txt_ketnoimang),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        })
    }
}
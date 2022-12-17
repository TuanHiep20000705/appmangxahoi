package com.mth.example.socialmediaapp.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.adapter.FriendRequestAdapter
import com.mth.example.socialmediaapp.model.FriendRequest
import com.mth.example.socialmediaapp.utils.ApiUtils
import com.mth.example.socialmediaapp.utils.Status
import com.mth.example.socialmediaapp.viewmodel.FriendRequestViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class RequestFragment : Fragment(R.layout.fragment_request) {
    private val friendRequestViewModel: FriendRequestViewModel by lazy {
        ViewModelProvider(
            this,
            FriendRequestViewModel.FriendRequestViewModelFactory(requireActivity().application)
        )[FriendRequestViewModel::class.java]
    }
    private val controller: NavController by lazy {
        findNavController()
    }
    private val friendRequestAdapter: FriendRequestAdapter by lazy {
        FriendRequestAdapter(requireContext(), onItemClick, onItemDongY, onItemXoa)
    }
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    }
    private val iduser: Int by lazy {
        sharedPreferences.getInt("iduser", 1)
    }
    private val tenuser: String? by lazy {
        sharedPreferences.getString("tenuser", "")
    }
    private val anhuser: String? by lazy {
        sharedPreferences.getString("anhuser", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        swipe_refresh_request_frag.setOnRefreshListener {
            refreshData()
        }
    }

    private fun initControls() {
        rcv_friend_request.setHasFixedSize(true)
        rcv_friend_request.layoutManager = LinearLayoutManager(requireContext())
        rcv_friend_request.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
        rcv_friend_request.adapter = friendRequestAdapter
        refreshData()
    }

    private fun refreshData() {
        friendRequestViewModel.getFriendRequestFromApi(iduser).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        swipe_refresh_request_frag.isRefreshing = false
                        resource.data?.let { friendRequests ->
                            friendRequestViewModel.insertFriendRequest(friendRequests)
                            friendRequestViewModel.getAllFriendRequest()
                                .observe(viewLifecycleOwner) {
                                    for (item in it) {
                                        var x = 0
                                        for (item2 in friendRequests) {
                                            if (item.id != item2.id) {
                                                x++
                                            }
                                        }
                                        if (x == friendRequests.size) {
                                            friendRequestViewModel.deleteFriendRequest(item)
                                        }
                                    }
                                    if (it.size == 0) {
                                        txt_trong_request_frag.visibility = View.VISIBLE
                                        Collections.sort(it)
                                        friendRequestAdapter.setData(it)
                                    } else {
                                        txt_trong_request_frag.visibility = View.GONE
                                        Collections.sort(it)
                                        friendRequestAdapter.setData(it)
                                    }
                                    txt_so_request_request_frag.setText(it.size.toString())
                                }
                        }
                    }
                    Status.ERROR -> {
                        swipe_refresh_request_frag.isRefreshing = false
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.txt_ketnoimang),
                            Toast.LENGTH_SHORT
                        ).show()
                        friendRequestViewModel.getAllFriendRequest().observe(viewLifecycleOwner) {
                            if (it.size == 0) {
                                txt_trong_request_frag.visibility = View.VISIBLE
                                Collections.sort(it)
                                friendRequestAdapter.setData(it)
                            } else {
                                txt_trong_request_frag.visibility = View.GONE
                                Collections.sort(it)
                                friendRequestAdapter.setData(it)
                            }
                            txt_so_request_request_frag.setText(it.size.toString())
                        }
                        Log.d("AAA", resource.message.toString())
                    }
                    Status.LOADING -> {
                        swipe_refresh_request_frag.isRefreshing = true
                    }
                }
            }
        }
    }

    private val onItemClick: (FriendRequest) -> Unit = {

    }
    private val onItemXoa: (FriendRequest) -> Unit = {
        ApiUtils.getData().deleteFriendRequest(it.idusersend, iduser)
            .enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    friendRequestViewModel.deleteFriendRequest(it)
                    ApiUtils.getData().updateStateFriendByIdMainAndIdList(iduser, it.idusersend, 0)
                        .enqueue(object : Callback<String> {
                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {

                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {

                            }
                        })
                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }
            })
    }
    private val onItemDongY: (FriendRequest) -> Unit = {
        ApiUtils.getData()
            .insertFriend(
                iduser,
                it.idusersend,
                it.tenusersend,
                it.anhusersend,
                tenuser!!,
                anhuser!!
            )
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val stringResponse = response.body()
                        if (stringResponse!!.trim().equals("success")) {
                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.txt_da_thanh_ban_be),
                                Toast.LENGTH_SHORT
                            ).show()
                            ApiUtils.getData().deleteFriendRequest(it.idusersend, iduser)
                                .enqueue(object : Callback<String> {
                                    override fun onResponse(
                                        call: Call<String>,
                                        response: Response<String>
                                    ) {
                                        friendRequestViewModel.deleteFriendRequest(it)
                                        ApiUtils.getData().updateStateFriendByIdMainAndIdList(
                                            iduser,
                                            it.idusersend,
                                            1
                                        ).enqueue(object : Callback<String> {
                                            override fun onResponse(
                                                call: Call<String>,
                                                response: Response<String>
                                            ) {
                                                friendRequestViewModel.updateListStatusWhenAddFriend(
                                                    iduser,
                                                    it.idusersend
                                                ).observe(viewLifecycleOwner) {
                                                    it?.let { resource ->
                                                        when (resource.status) {
                                                            Status.SUCCESS -> {

                                                            }
                                                            Status.ERROR -> {

                                                            }
                                                            Status.LOADING -> {

                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<String>,
                                                t: Throwable
                                            ) {

                                            }
                                        })
                                    }

                                    override fun onFailure(call: Call<String>, t: Throwable) {

                                    }
                                })
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.txt_ketnoimang),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
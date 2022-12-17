package com.mth.example.socialmediaapp.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.adapter.StatusAdapter
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.ui.activities.*
import com.mth.example.socialmediaapp.utils.ApiUtils
import com.mth.example.socialmediaapp.utils.Status.*
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val statusViewModel: StatusViewModel by lazy {
        ViewModelProvider(
            this,
            StatusViewModel.StatusViewModelFactory(requireActivity().application)
        )[StatusViewModel::class.java]
    }

    private val controller: NavController by lazy {
        findNavController()
    }

    private var statusAdapter: StatusAdapter? = null
    private fun contructStatusAdapter() {
        statusAdapter = StatusAdapter(
            requireContext(),
            onItemStatusDelete,
            onItemStatusEdit,
            onItemStatusLike,
            onItemStatusComment
        )
    }

    private fun getDataFromSharePreference() {
        anhuser = sharedPreferences.getString("anhuser", "")
    }

    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    }
    private val iduser: Int by lazy {
        sharedPreferences.getInt("iduser", 1)
    }

    private var anhuser: String? = ""

    private val tenuser: String? by lazy {
        sharedPreferences.getString("tenuser", "")
    }

    private val moveToCreateContent: MoveToCreateContent by lazy {
        activity as MoveToCreateContent
    }

    private val moveToCreateImgContent: MoveToCreateImgContent by lazy {
        activity as MoveToCreateImgContent
    }
    private val moveToComment: MoveToComment by lazy {
        activity as MoveToComment
    }
    private val moveToEditContent: MoveToEditContent by lazy {
        activity as MoveToEditContent
    }
    private val moveToEditImgContent: MoveToEditImgContent by lazy {
        activity as MoveToEditImgContent
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        swipe_refresh_home_frag.setOnRefreshListener {
            refreshData()
        }
        edt_your_think.setOnClickListener {
            moveToCreateContent.initCreateContent()
        }

        but_up_img.setOnClickListener {
            moveToCreateImgContent.initCreateImgContent()
        }
    }

    private fun initControls() {
        getDataFromSharePreference()
        if (anhuser!!.length > 0) {
            Glide.with(requireContext()).load(anhuser).into(img_avt_home_frag)
        } else {
            img_avt_home_frag.setImageResource(R.drawable.default_avatar)
        }

        (rcv_bang_tin?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        rcv_bang_tin.setHasFixedSize(true)
        rcv_bang_tin.layoutManager = LinearLayoutManager(requireContext())
        rcv_bang_tin.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
//        refreshData()
        setUpData()
    }

    private fun refreshData() {
        contructStatusAdapter()
//        (rcv_bang_tin?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
//        rcv_bang_tin.setHasFixedSize(true)
//        rcv_bang_tin.layoutManager = LinearLayoutManager(requireContext())
//        rcv_bang_tin.addItemDecoration(
//            DividerItemDecoration(
//                requireContext(),
//                RecyclerView.VERTICAL
//            )
//        )
        rcv_bang_tin.adapter = statusAdapter
        statusViewModel.getStatusFromApi(iduser).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        swipe_refresh_home_frag.isRefreshing = false
                        resource.data?.let { statuses ->
                            statusViewModel.insertStatus(statuses)
                            statusViewModel.getAllStatus().observe(viewLifecycleOwner) {
                                Collections.sort(it)
                                statusAdapter?.setData(it)
                                for (item in it) {
                                    var x = 0
                                    for (item2 in statuses) {
                                        if (item.id != item2.id) {
                                            x++
                                        }
                                    }
                                    if (x == statuses.size) {
                                        statusViewModel.deleteStatus(item)
                                    }
                                }
                            }
                        }
                    }
                    ERROR -> {
                        swipe_refresh_home_frag.isRefreshing = false
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.txt_ketnoimang),
                            Toast.LENGTH_SHORT
                        ).show()
                        statusViewModel.getAllStatus().observe(viewLifecycleOwner) {
                            Collections.sort(it)
                            statusAdapter?.setData(it)
                        }
                        Log.d("AAA", it.message.toString())
                    }
                    LOADING -> {
                        swipe_refresh_home_frag.isRefreshing = true
                    }
                }
            }
        }
    }

    private fun setUpData() {
        contructStatusAdapter()
        rcv_bang_tin.adapter = statusAdapter
        statusViewModel.getAllStatus().observe(viewLifecycleOwner) {
            if (it.size == 0) {
                txt_trong_home_frag.visibility = View.VISIBLE
                Collections.sort(it)
                statusAdapter?.setData(it)
            } else {
                txt_trong_home_frag.visibility = View.GONE
                Collections.sort(it)
                statusAdapter?.setData(it)
            }
        }
    }

    private val onItemStatusDelete: (Status) -> Unit = { status ->
        val alertDialog = AlertDialog.Builder(requireContext())
        if (status.iduserpost == iduser) {
            alertDialog.setTitle(resources.getString(R.string.txt_thong_bao))
            alertDialog.setMessage(resources.getString(R.string.txt_xoa_status))
            alertDialog.setPositiveButton(resources.getString(R.string.txt_co)) { dialogInterface, i ->
                statusViewModel.deleteStatusInApi(0, status.idstatus)
                    .observe(viewLifecycleOwner) {
                        it?.let { resource ->
                            when (resource.status) {
                                SUCCESS -> {
                                    statusViewModel.deleteStatus(status)
                                }
                                ERROR -> {
                                    Toast.makeText(
                                        requireContext(),
                                        resources.getString(R.string.txt_ket_noi_mang_yeu),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LOADING -> {

                                }
                            }
                        }
                    }
            }
            alertDialog.setNegativeButton(resources.getString(R.string.txt_ko)) { dialogInterface, i ->

            }
            alertDialog.show()
        } else {
            alertDialog.setTitle(resources.getString(R.string.txt_thong_bao))
            alertDialog.setMessage(resources.getString(R.string.txt_an_status))
            alertDialog.setPositiveButton(resources.getString(R.string.txt_co)) { dialogInterface, i ->
                statusViewModel.deleteStatusInApi(status.id, 0)
                    .observe(viewLifecycleOwner) {
                        it?.let { resource ->
                            when (resource.status) {
                                SUCCESS -> {
                                    statusViewModel.deleteStatus(status)
                                }
                                ERROR -> {
                                    Toast.makeText(
                                        requireContext(),
                                        resources.getString(R.string.txt_ket_noi_mang_yeu),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LOADING -> {

                                }
                            }
                        }
                    }
            }
            alertDialog.setNegativeButton(resources.getString(R.string.txt_ko)) { dialogInterface, i ->

            }
            alertDialog.show()
        }
    }
    private val onItemStatusEdit: (Status) -> Unit = {
        if (it.imgcontent.length > 0) {
            moveToEditImgContent.initEditImgContent(it)
        } else {
            moveToEditContent.initEditContent(it)
        }
    }
    private val onItemStatusLike: (Status) -> Unit = { status ->
        val cal: Calendar = Calendar.getInstance()
        val t: Date = cal.time
        val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm")
        if (status.stateliked == 1) {
            val stateLiked = 0
            val soLike = status.solike - 1
            statusViewModel.likeStatus(status.id, status.idstatus, iduser, stateLiked, soLike)
                .observe(viewLifecycleOwner) {
                    it?.let { resource ->
                        when (resource.status) {
                            SUCCESS -> {

                            }
                            ERROR -> {
                                Toast.makeText(
                                    requireContext(),
                                    resources.getString(R.string.txt_ket_noi_mang_yeu),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            LOADING -> {

                            }
                        }
                    }
                }
        } else {
            val stateLiked = 1
            val soLike = status.solike + 1
            statusViewModel.likeStatus(status.id, status.idstatus, iduser, stateLiked, soLike)
                .observe(viewLifecycleOwner) {
                    it?.let { resource ->
                        when (resource.status) {
                            SUCCESS -> {
                                if (status.iduserpost2 != iduser) {
                                    ApiUtils.getData().insertNotificationToApi(
                                        status.id, status.idstatus, iduser,
                                        status.iduserpost2, tenuser!!, anhuser!!, sdf.format(t), 1
                                    ).enqueue(object : Callback<String> {
                                        override fun onResponse(
                                            call: Call<String>,
                                            response: Response<String>
                                        ) {

                                        }

                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Toast.makeText(
                                                requireContext(),
                                                resources.getString(R.string.txt_ket_noi_mang_yeu),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                                }
                            }
                            ERROR -> {
                                Toast.makeText(
                                    requireContext(),
                                    resources.getString(R.string.txt_ket_noi_mang_yeu),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            LOADING -> {

                            }
                        }
                    }
                }
        }
    }
    private val onItemStatusComment: (Status) -> Unit = {
        moveToComment.initComentEvent(it)
    }
}
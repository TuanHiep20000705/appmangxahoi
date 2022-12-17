package com.mth.example.socialmediaapp.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.adapter.StatusAdapter
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.ui.activities.*
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.Collections

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val statusViewModel: StatusViewModel by lazy {
        ViewModelProvider(
            this,
            StatusViewModel.StatusViewModelFactory(requireActivity().application)
        )[StatusViewModel::class.java]
    }
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    }

    private val statusAdapter: StatusAdapter by lazy {
        StatusAdapter(
            requireContext(),
            onItemStatusDelete,
            onItemStatusEdit,
            onItemStatusLike,
            onItemStatusComment
        )
    }

    fun getDataFromSharePreference() {
        iduser = sharedPreferences.getInt("iduser", 1)
        tenuser = sharedPreferences.getString("tenuser", "")
        anhuser = sharedPreferences.getString("anhuser", "")
        anhbiauser = sharedPreferences.getString("anhbiauser", "")
        motauser = sharedPreferences.getString("motauser", "")
        quequanuser = sharedPreferences.getString("quequanuser", "")
        ngaysinhuser = sharedPreferences.getString("ngaysinhuser", "")
        sdtuser = sharedPreferences.getInt("sdtuser", 0)
    }

    private var iduser: Int = 0
    private var tenuser: String? = ""
    private var anhuser: String? =""
    private var anhbiauser: String? = ""
    private var motauser: String? = ""
    private var quequanuser: String? = ""
    private var ngaysinhuser: String? = ""
    private var sdtuser: Int = 0

    private val moveToComment: MoveToComment by lazy {
        activity as MoveToComment
    }
    private val moveToEditContent: MoveToEditContent by lazy {
        activity as MoveToEditContent
    }
    private val moveToEditImgContent: MoveToEditImgContent by lazy {
        activity as MoveToEditImgContent
    }
    private val logOutToLoginActivity: LogOutToLoginActivity by lazy {
        activity as LogOutToLoginActivity
    }
    private val moveToFixProfile: MoveToFixProfile by lazy {
        activity as MoveToFixProfile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        but_dang_xuat_profile_frag.setOnClickListener {
            logOutToLoginActivity.initLogOutEvent()
        }
        but_sua_thong_tin_profile_frag.setOnClickListener {
            moveToFixProfile.initFixProfile()
        }
        swipe_refresh_profile_frag.setOnRefreshListener {
            refreshData()
        }
    }

    private fun initControls() {
        (rcv_bang_tin_profile_frag?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        rcv_bang_tin_profile_frag.setHasFixedSize(true)
        rcv_bang_tin_profile_frag.layoutManager = LinearLayoutManager(requireContext())
        rcv_bang_tin_profile_frag.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
        rcv_bang_tin_profile_frag.adapter = statusAdapter
        refreshData()
    }

    private fun refreshData() {
        getDataFromSharePreference()
        swipe_refresh_profile_frag.isRefreshing = true
        if (anhbiauser!!.length > 0) {
            Glide.with(requireContext()).load(anhbiauser).into(img_anh_bia_profile_frag)
        }
        if (anhuser!!.length > 0) {
            Glide.with(requireContext()).load(anhuser).into(img_avt_user_profile_frag)
        }
        txt_ten_user_profile_frag.setText(tenuser)
        if (motauser!!.length > 0) {
            txt_mo_ta_profile_frag.setText(motauser)
        }
        txt_address_profile_frag.setText(quequanuser)
        txt_birthday_profile_frag.setText(ngaysinhuser)
        txt_phone_profile_frag.setText(sdtuser.toString())

        statusViewModel.getAllStatus().observe(viewLifecycleOwner) {
            val listStatusProfile = mutableListOf<Status>()
            for (item in it) {
                if (item.iduserpost == iduser) {
                    item.avatuserpost = anhuser!!
                    listStatusProfile.add(item)
                }
            }
            Collections.sort(listStatusProfile)
            statusAdapter.setData(listStatusProfile)
        }
        swipe_refresh_profile_frag.isRefreshing = false
    }

    private val onItemStatusDelete: (Status) -> Unit = { status ->
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(resources.getString(R.string.txt_thong_bao))
        alertDialog.setMessage(resources.getString(R.string.txt_xoa_status))
        alertDialog.setPositiveButton(resources.getString(R.string.txt_co)) { dialogInterface, i ->
            statusViewModel.deleteStatusInApi(0, status.idstatus)
                .observe(viewLifecycleOwner) {
                    it?.let { resource ->
                        when (resource.status) {
                            com.mth.example.socialmediaapp.utils.Status.SUCCESS -> {
                                statusViewModel.deleteStatus(status)
                            }
                            com.mth.example.socialmediaapp.utils.Status.ERROR -> {
                                Toast.makeText(
                                    requireContext(),
                                    resources.getString(R.string.txt_ket_noi_mang_yeu),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            com.mth.example.socialmediaapp.utils.Status.LOADING -> {

                            }
                        }
                    }
                }
        }
        alertDialog.setNegativeButton(resources.getString(R.string.txt_ko)) { dialogInterface, i ->

        }
        alertDialog.show()
    }
    private val onItemStatusEdit: (Status) -> Unit = {
        if (it.imgcontent.length > 0) {
            moveToEditImgContent.initEditImgContent(it)
        } else {
            moveToEditContent.initEditContent(it)
        }
    }
    private val onItemStatusLike: (Status) -> Unit = { status ->
        if (status.stateliked == 1) {
            val stateLiked = 0
            val soLike = status.solike - 1
            statusViewModel.likeStatus(status.id, status.idstatus, iduser, stateLiked, soLike)
                .observe(viewLifecycleOwner) {
                    it?.let { resource ->
                        when (resource.status) {
                            com.mth.example.socialmediaapp.utils.Status.SUCCESS -> {

                            }
                            com.mth.example.socialmediaapp.utils.Status.ERROR -> {
                                Toast.makeText(
                                    requireContext(),
                                    resources.getString(R.string.txt_ket_noi_mang_yeu),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            com.mth.example.socialmediaapp.utils.Status.LOADING -> {

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
                            com.mth.example.socialmediaapp.utils.Status.SUCCESS -> {

                            }
                            com.mth.example.socialmediaapp.utils.Status.ERROR -> {
                                Toast.makeText(
                                    requireContext(),
                                    resources.getString(R.string.txt_ket_noi_mang_yeu),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            com.mth.example.socialmediaapp.utils.Status.LOADING -> {

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
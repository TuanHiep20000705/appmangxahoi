package com.mth.example.socialmediaapp.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.adapter.CommentAdapter
import com.mth.example.socialmediaapp.model.Comment
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.utils.Status.SUCCESS
import com.mth.example.socialmediaapp.utils.Status.ERROR
import com.mth.example.socialmediaapp.utils.Status.LOADING
import com.mth.example.socialmediaapp.utils.ApiUtils
import com.mth.example.socialmediaapp.viewmodel.CommentViewModel
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_create_content.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CommentActivity : AppCompatActivity() {
    private val statusViewModel: StatusViewModel by lazy {
        ViewModelProvider(
            this,
            StatusViewModel.StatusViewModelFactory(this.application)
        )[StatusViewModel::class.java]
    }

    private val commentViewModel: CommentViewModel by lazy {
        ViewModelProvider(
            this,
            CommentViewModel.CommentViewHolderFactory(this.application)
        )[CommentViewModel::class.java]
    }

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter(this, onItemDelete)
    }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("datalogin", Context.MODE_PRIVATE)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        initControls()
    }

    private var stateliked: Boolean = false
    private var soLike: Int = 0
    private var soComment: Int = 0
    private fun initEvents(status: Status) {
        but_back_comment.setOnClickListener {
            onBackPressed()
        }
        but_like_comment.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val t: Date = cal.time
            val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm")
            if (!stateliked) {
                but_like_comment.setImageResource(R.drawable.icon_like)
                if (soLike >= 1) {
                    txt_so_like_comment.setText("${soLike + 1} Likes")
                } else {
                    txt_so_like_comment.setText("${soLike + 1} Like")
                }
                soLike += 1
                statusViewModel.likeStatus(status.id, status.idstatus, iduser, 1, soLike)
                    .observe(this) {
                        it?.let { resource ->
                            when (resource.status) {
                                SUCCESS -> {
                                    if (status.iduserpost2 != iduser) {
                                        ApiUtils.getData().insertNotificationToApi(
                                            status.id,
                                            status.idstatus,
                                            iduser,
                                            status.iduserpost2,
                                            tenuser!!,
                                            anhuser!!,
                                            sdf.format(t),
                                            1
                                        ).enqueue(object : Callback<String> {
                                            override fun onResponse(
                                                call: Call<String>,
                                                response: Response<String>
                                            ) {

                                            }

                                            override fun onFailure(
                                                call: Call<String>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(
                                                    this@CommentActivity,
                                                    resources.getString(R.string.txt_ket_noi_mang_yeu),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        })
                                    }
                                }
                                ERROR -> {
                                    Toast.makeText(
                                        this,
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
                but_like_comment.setImageResource(R.drawable.icon_unlike)
                if (soLike <= 2) {
                    txt_so_like_comment.setText("${soLike - 1} Like")
                } else {
                    txt_so_like_comment.setText("${soLike - 1} Likes")
                }
                soLike -= 1
                statusViewModel.likeStatus(status.id, status.idstatus, iduser, 0, soLike)
                    .observe(this) {
                        it?.let { resource ->
                            when (resource.status) {
                                SUCCESS -> {

                                }
                                ERROR -> {
                                    Toast.makeText(
                                        this,
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
            stateliked = !stateliked
        }
        swipe_refresh_comment.setOnRefreshListener { refreshData(status) }
        but_send_comment.setOnClickListener {
            val noiDung: String = edt_comment_coment.text.toString()
            val cal: Calendar = Calendar.getInstance()
            val t: Date = cal.time
            val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm")
            if (noiDung.length > 0) {
                if (soComment >= 1) {
                    txt_so_comment_comment.setText("${soComment + 1} Comments")
                } else {
                    txt_so_comment_comment.setText("${soComment + 1} Comment")
                }
                soComment += 1
                edt_comment_coment.setText("")
                ApiUtils.getData().insertComment(
                    status.idstatus,
                    iduser,
                    tenuser!!,
                    anhuser!!,
                    sdf.format(t),
                    noiDung
                ).enqueue(object : Callback<kotlin.collections.List<Comment>> {
                    override fun onResponse(
                        call: Call<List<Comment>>,
                        response: Response<List<Comment>>
                    ) {
                        if (response.isSuccessful) {
                            val commentList: kotlin.collections.List<Comment> = response.body()!!
                            Collections.sort(commentList)
                            commentAdapter.setData(commentList)
                            if (status.iduserpost2 != iduser) {
                                ApiUtils.getData().insertNotificationToApi(
                                    status.id, status.idstatus, iduser,
                                    status.iduserpost2, tenuser!!, anhuser!!, sdf.format(t), 0
                                ).enqueue(object : Callback<String> {
                                    override fun onResponse(
                                        call: Call<String>,
                                        response: Response<String>
                                    ) {

                                    }

                                    override fun onFailure(call: Call<String>, t: Throwable) {
                                        Toast.makeText(
                                            this@CommentActivity,
                                            resources.getString(R.string.txt_ket_noi_mang_yeu),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                        Toast.makeText(
                            this@CommentActivity,
                            resources.getString(R.string.txt_ket_noi_mang_yeu),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(
                    this@CommentActivity,
                    resources.getString(R.string.txt_chua_nhap_binh_luan),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initControls() {
        val status: Status = intent.getSerializableExtra("dulieustatus") as Status
        stateliked = status.stateliked == 1
        soLike = status.solike
        soComment = status.socomment
        if (status.avatuserpost.length > 0) {
            Glide.with(this).load(status.avatuserpost).into(img_avt_user_comment)
        }
        if (status.imgcontent.length > 0) {
            Glide.with(this).load(status.imgcontent).into(img_hinh_status_comment)
        } else {
            img_hinh_status_comment.visibility = View.GONE
        }
        txt_ten_user_comment.setText(status.tenuserpost)
        txt_ngay_post_comment.setText(status.ngaypost)
        txt_content_comment.setText(status.content)
        if (status.solike >= 2) {
            txt_so_like_comment.setText("${status.solike} Likes")
        } else {
            txt_so_like_comment.setText("${status.solike} Like")
        }
        if (status.socomment >= 2) {
            txt_so_comment_comment.setText("${status.socomment} Comments")
        } else {
            txt_so_comment_comment.setText("${status.socomment} Comment")
        }
        if (status.stateliked == 1) {
            but_like_comment.setImageResource(R.drawable.icon_like)
        } else {
            but_like_comment.setImageResource(R.drawable.icon_unlike)
        }

        refreshData(status)
        initEvents(status)
    }

    private fun refreshData(status: Status) {
        rcv_comment.setHasFixedSize(true)
        rcv_comment.layoutManager = LinearLayoutManager(this)
        rcv_comment.adapter = commentAdapter
        commentViewModel.getCommentFromApi(status.idstatus).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        swipe_refresh_comment.isRefreshing = false
                        resource.data?.let { comments ->
                            Collections.sort(comments)
                            commentAdapter.setData(comments)
                        }
                    }
                    ERROR -> {
                        swipe_refresh_comment.isRefreshing = false
                        Toast.makeText(
                            this@CommentActivity,
                            resources.getString(R.string.txt_ket_noi_mang_yeu),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    LOADING -> {
                        swipe_refresh_comment.isRefreshing = true
                    }
                }
            }
        }
    }

    private val onItemDelete: (Comment) -> Unit = {
        if (soComment >= 3) {
            txt_so_comment_comment.setText("${soComment - 1} Comments")
        } else {
            txt_so_comment_comment.setText("${soComment - 1} Comment")
        }
        soComment -= 1
        ApiUtils.getData().deleteComment(it.idcmt, it.idstatuscmt)
            .enqueue(object : Callback<kotlin.collections.List<Comment>> {
                override fun onResponse(
                    call: Call<List<Comment>>,
                    response: Response<List<Comment>>
                ) {
                    if (response.isSuccessful) {
                        val listComment: kotlin.collections.List<Comment> = response.body()!!
                        Collections.sort(listComment)
                        commentAdapter.setData(listComment)
                        Toast.makeText(
                            this@CommentActivity,
                            resources.getString(R.string.txt_da_xoa_binh_luan),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                    Toast.makeText(
                        this@CommentActivity,
                        resources.getString(R.string.txt_ket_noi_mang_yeu),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
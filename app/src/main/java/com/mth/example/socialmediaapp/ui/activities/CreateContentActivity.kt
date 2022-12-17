@file:Suppress("DEPRECATION")

package com.mth.example.socialmediaapp.ui.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.utils.Status
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import kotlinx.android.synthetic.main.activity_create_content.*
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class CreateContentActivity : AppCompatActivity() {
    private val statusViewModel: StatusViewModel by lazy {
        ViewModelProvider(
            this,
            StatusViewModel.StatusViewModelFactory(this.application)
        )[StatusViewModel::class.java]
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
    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_content)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        but_back_create_content.setOnClickListener {
            finish()
        }
        but_dang_bai_create_content.setOnClickListener {
            progressDialog.setMessage(resources.getString(R.string.txt_please_wait))
            val content: String = edt_your_think_create_content.text.toString()
            val cal: Calendar = Calendar.getInstance()
            val t: Date = cal.time
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            if (content.length == 0) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.txt_chua_ghi_noi_dung),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                progressDialog.show()
                statusViewModel.postStatus(iduser, tenuser!!, anhuser!!, content, "", sdf.format(t))
                    .observe(this) {
                        it?.let { resource ->
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    resource.data?.let { response ->
                                        progressDialog.dismiss()
                                        if (response.trim().equals("success")) {
                                            Toast.makeText(
                                                this,
                                                resources.getString(R.string.txt_dang_bai_thanh_cong),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finish()
                                        }
                                    }
                                }
                                Status.ERROR -> {
                                    progressDialog.dismiss()
                                    Toast.makeText(
                                        this,
                                        resources.getString(com.mth.example.socialmediaapp.R.string.txt_ketnoimang),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                Status.LOADING -> {

                                }
                            }
                        }
                    }
            }
        }
    }

    private fun initControls() {
        txt_ten_user_create_content.setText(tenuser)
        if (anhuser!!.length > 0) {
            Glide.with(this).load(anhuser).into(img_avt_user_create_content)
        }
    }
}
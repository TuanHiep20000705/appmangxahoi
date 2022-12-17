@file:Suppress("DEPRECATION")

package com.mth.example.socialmediaapp.ui.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.utils.Status.SUCCESS
import com.mth.example.socialmediaapp.utils.Status.ERROR
import com.mth.example.socialmediaapp.utils.Status.LOADING
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import kotlinx.android.synthetic.main.activity_create_content.*
import kotlinx.android.synthetic.main.activity_edit_content.*

@Suppress("DEPRECATION")
class EditContentActivity : AppCompatActivity() {
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
    lateinit var status: Status
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_content)
        status = intent.getSerializableExtra("dulieueditcontent") as Status
        initControls()
        initEvents()
    }

    private fun initEvents() {
        but_back_edit_content.setOnClickListener { finish() }
        but_dang_bai_edit_content.setOnClickListener {
            progressDialog.setMessage(resources.getString(R.string.txt_please_wait))
            val content: String = edt_your_think_edit_content.text.toString()
            if (content.length == 0) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.txt_chua_ghi_noi_dung),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                progressDialog.show()
                statusViewModel.updateContentStatusInApi(status.idstatus, content).observe(this) {
                    it?.let { resource ->
                        when (resource.status) {
                            SUCCESS -> {
                                resource.data?.let { response ->
                                    progressDialog.dismiss()
                                    if (response.equals("success")){
                                        Toast.makeText(
                                            this,
                                            resources.getString(R.string.txt_sua_bai_thanh_cong),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    }
                                }
                            }
                            ERROR -> {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    this,
                                    resources.getString(com.mth.example.socialmediaapp.R.string.txt_ketnoimang),
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
    }

    private fun initControls() {
        txt_ten_user_edit_content.setText(tenuser)
        if (anhuser!!.length > 0) {
            Glide.with(this).load(anhuser).into(img_avt_user_edit_content)
        }
        edt_your_think_edit_content.setText(status.content)
    }
}
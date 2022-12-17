@file:Suppress("DEPRECATION")

package com.mth.example.socialmediaapp.ui.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.utils.ApiUtils
import com.mth.example.socialmediaapp.utils.RealPathUtil
import com.mth.example.socialmediaapp.utils.Status.SUCCESS
import com.mth.example.socialmediaapp.utils.Status.ERROR
import com.mth.example.socialmediaapp.utils.Status.LOADING
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import kotlinx.android.synthetic.main.activity_create_img_content.*
import kotlinx.android.synthetic.main.activity_edit_img_content.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

@Suppress("DEPRECATION")
class EditImgContentActivity : AppCompatActivity() {
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

    private val myRequestCode = 10

    private var mUri: Uri? = null

    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    private val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data == null) {
                    return@ActivityResultCallback
                } else {
                    val uri = data.data
                    mUri = uri!!
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        img_add_img_edit_img_content.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    )

    lateinit var status: Status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_img_content)
        status = intent.getSerializableExtra("dulieueditimgcontent") as Status
        initControls()
        initEvents()
    }

    private fun initEvents() {
        but_back_edit_img_content.setOnClickListener { finish() }
        img_add_img_edit_img_content.setOnClickListener {
            onClickRequestPermission()
        }
        but_dang_bai_edit_img_content.setOnClickListener {
            uploadImgStatus()
        }
    }

    private fun uploadImgStatus() {
        val content: String = edt_your_think_edit_img_content.text.toString()
        progressDialog.setMessage(resources.getString(R.string.txt_please_wait))
        if (mUri == null) {
            progressDialog.show()
            statusViewModel.updateContentStatusInApi(status.idstatus, content).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        SUCCESS -> {
                            resource.data?.let { response ->
                                progressDialog.dismiss()
                                if (response.equals("success")) {
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
        } else {
            progressDialog.show()
            val realPath: String? = RealPathUtil.getRealPath(this, mUri!!)
            val file = File(realPath)
            var file_path = file.absolutePath
            val mangTenFile = file_path.split(".").toTypedArray()
            file_path = mangTenFile[0] + System.currentTimeMillis() + "." + mangTenFile[1]
            val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val photo = MultipartBody.Part.createFormData("hinhanh", file_path, requestBody)
            statusViewModel.uploadImgStatus(photo).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        SUCCESS -> {
                            resource.data?.let { response ->
                                val imgContent: String = ApiUtils.baseUrl + "image/" + response
                                statusViewModel.updateImgContentStatusInApi(
                                    status.idstatus,
                                    content,
                                    imgContent
                                ).observe(this) {
                                    it?.let { resource ->
                                        when (resource.status) {
                                            SUCCESS -> {
                                                progressDialog.dismiss()
                                                resource.data?.let { response ->
                                                    if (response.trim().equals("success")) {
                                                        Toast.makeText(
                                                            this,
                                                            resources.getString(R.string.txt_sua_bai_thanh_cong),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        startActivity(
                                                            Intent(
                                                                this,
                                                                MainActivity::class.java
                                                            )
                                                        )
                                                        finish()
                                                    }
                                                }
                                            }
                                            ERROR -> {
                                                progressDialog.dismiss()
                                                Toast.makeText(
                                                    this,
                                                    resources.getString(R.string.txt_ketnoimang),
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

    private fun initControls() {
        txt_ten_user_edit_img_content.setText(tenuser)
        if (anhuser!!.length > 0) {
            Glide.with(this).load(anhuser).into(img_avt_user_edit_img_content)
        }
        edt_your_think_edit_img_content.setText(status.content)
        Glide.with(this).load(status.imgcontent).into(img_add_img_edit_img_content)
    }

    private fun onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery()
            return
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, myRequestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == myRequestCode && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        }
    }

    fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }
}
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
import com.mth.example.socialmediaapp.utils.ApiUtils
import com.mth.example.socialmediaapp.utils.RealPathUtil
import com.mth.example.socialmediaapp.utils.Status
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import kotlinx.android.synthetic.main.activity_edit_img_content.*
import kotlinx.android.synthetic.main.activity_fix_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

@Suppress("DEPRECATION")
class FixProfileActivity : AppCompatActivity() {
    private val statusViewModel: StatusViewModel by lazy {
        ViewModelProvider(
            this,
            StatusViewModel.StatusViewModelFactory(this.application)
        )[StatusViewModel::class.java]
    }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    }

    private val editor: SharedPreferences.Editor by lazy {
        sharedPreferences.edit()
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
    private val anhbiauser: String? by lazy {
        sharedPreferences.getString("anhbiauser", "")
    }
    private val motauser: String? by lazy {
        sharedPreferences.getString("motauser", "")
    }
    private val quequanuser: String? by lazy {
        sharedPreferences.getString("quequanuser", "")
    }
    private val ngaysinhuser: String? by lazy {
        sharedPreferences.getString("ngaysinhuser", "")
    }
    private val sdtuser: Int by lazy {
        sharedPreferences.getInt("sdtuser", 0)
    }

    private val myRequestCode = 10

    private var mUri: Uri? = null

    private var mUri2: Uri? = null

    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    private var key: Boolean = false

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
                        img_anh_bia_fix_profile.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    )

    private val activityResultLauncher2 = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data == null) {
                    return@ActivityResultCallback
                } else {
                    val uri = data.data
                    mUri2 = uri!!
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        img_avt_user_fix_profile.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fix_profile)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        but_back_fix_profile.setOnClickListener {
            finish()
        }
        img_anh_bia_fix_profile.setOnClickListener {
            key = true
            onClickRequestPermission()
        }
        img_avt_user_fix_profile.setOnClickListener {
            key = false
            onClickRequestPermission()
        }
        but_sua_fix_pofile.setOnClickListener {
            val tenUser: String = edt_ten_fix_profile.text.toString()
            val moTa: String = edt_mo_ta_fix_profile.text.toString()
            val diaChi: String = edt_dia_chi_fix_profile.text.toString()
            val ngaySinh: String = edt_ngay_sinh_fix_profile.text.toString()
            val sdt: Int = edt_sdt_fix_profile.text.toString().toInt()
            progressDialog.setMessage(resources.getString(R.string.txt_please_wait))
            if (mUri != null && mUri2 == null) {
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
                            Status.SUCCESS -> {
                                resource.data?.let { response ->
                                    val anhBia: String = ApiUtils.baseUrl + "image/" + response
                                    ApiUtils.getData().updateProfile(
                                        iduser,
                                        tenUser,
                                        ngaySinh,
                                        diaChi,
                                        moTa,
                                        sdt,
                                        anhuser!!,
                                        anhBia
                                    ).enqueue(object : Callback<String> {
                                        override fun onResponse(
                                            call: Call<String>,
                                            response: Response<String>
                                        ) {
                                            if (response.isSuccessful) {
                                                progressDialog.dismiss()
                                                editor.putString("tenuser", tenUser)
                                                editor.putString("anhbiauser", anhBia)
                                                editor.putString("motauser", moTa)
                                                editor.putString("quequanuser", diaChi)
                                                editor.putString("ngaysinhuser", ngaySinh)
                                                editor.putInt("sdtuser", sdt)
                                                editor.commit()
//                                                startActivity(Intent(this@FixProfileActivity,MainActivity::class.java))
                                                finish()
                                            }
                                        }

                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            progressDialog.dismiss()
                                            Toast.makeText(
                                                this@FixProfileActivity,
                                                resources.getString(com.mth.example.socialmediaapp.R.string.txt_ketnoimang),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
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
            } else if (mUri2 != null && mUri == null) {
                progressDialog.show()
                val realPath: String? = RealPathUtil.getRealPath(this, mUri2!!)
                val file = File(realPath)
                var file_path = file.absolutePath
                val mangTenFile = file_path.split(".").toTypedArray()
                file_path = mangTenFile[0] + System.currentTimeMillis() + "." + mangTenFile[1]
                val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val photo = MultipartBody.Part.createFormData("hinhanh", file_path, requestBody)
                statusViewModel.uploadImgStatus(photo).observe(this) {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                resource.data?.let { response ->
                                    val hinhAnh: String = ApiUtils.baseUrl + "image/" + response
                                    ApiUtils.getData().updateProfile(
                                        iduser,
                                        tenUser,
                                        ngaySinh,
                                        diaChi,
                                        moTa,
                                        sdt,
                                        hinhAnh,
                                        anhbiauser!!
                                    ).enqueue(object : Callback<String> {
                                        override fun onResponse(
                                            call: Call<String>,
                                            response: Response<String>
                                        ) {
                                            if (response.isSuccessful) {
                                                progressDialog.dismiss()
                                                editor.putString("tenuser", tenUser)
                                                editor.putString("anhuser", hinhAnh)
                                                editor.putString("motauser", moTa)
                                                editor.putString("quequanuser", diaChi)
                                                editor.putString("ngaysinhuser", ngaySinh)
                                                editor.putInt("sdtuser", sdt)
                                                editor.commit()
//                                                startActivity(Intent(this@FixProfileActivity,MainActivity::class.java))
                                                finish()
                                            }
                                        }

                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            progressDialog.dismiss()
                                            Toast.makeText(
                                                this@FixProfileActivity,
                                                resources.getString(com.mth.example.socialmediaapp.R.string.txt_ketnoimang),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
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
            } else if(mUri2 != null && mUri != null){
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
                            Status.SUCCESS -> {
                                resource.data?.let { response ->
                                    val anhBia: String = ApiUtils.baseUrl + "image/" + response
                                    val realPath: String? = RealPathUtil.getRealPath(this, mUri2!!)
                                    val file = File(realPath)
                                    var file_path = file.absolutePath
                                    val mangTenFile = file_path.split(".").toTypedArray()
                                    file_path = mangTenFile[0] + System.currentTimeMillis() + "." + mangTenFile[1]
                                    val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                                    val photo = MultipartBody.Part.createFormData("hinhanh", file_path, requestBody)
                                    statusViewModel.uploadImgStatus(photo).observe(this) {
                                        it?.let { resource ->
                                            when (resource.status) {
                                                Status.SUCCESS -> {
                                                    resource.data?.let { response ->
                                                        val hinhAnh: String = ApiUtils.baseUrl + "image/" + response
                                                        ApiUtils.getData().updateProfile(
                                                            iduser,
                                                            tenUser,
                                                            ngaySinh,
                                                            diaChi,
                                                            moTa,
                                                            sdt,
                                                            hinhAnh,
                                                            anhBia
                                                        ).enqueue(object : Callback<String> {
                                                            override fun onResponse(
                                                                call: Call<String>,
                                                                response: Response<String>
                                                            ) {
                                                                if (response.isSuccessful) {
                                                                    progressDialog.dismiss()
                                                                    editor.putString("tenuser", tenUser)
                                                                    editor.putString("anhuser", hinhAnh)
                                                                    editor.putString("anhbiauser", anhBia)
                                                                    editor.putString("motauser", moTa)
                                                                    editor.putString("quequanuser", diaChi)
                                                                    editor.putString("ngaysinhuser", ngaySinh)
                                                                    editor.putInt("sdtuser", sdt)
                                                                    editor.commit()
//                                                                    startActivity(Intent(this@FixProfileActivity,MainActivity::class.java))
                                                                    finish()
                                                                }
                                                            }

                                                            override fun onFailure(call: Call<String>, t: Throwable) {
                                                                progressDialog.dismiss()
                                                                Toast.makeText(
                                                                    this@FixProfileActivity,
                                                                    resources.getString(com.mth.example.socialmediaapp.R.string.txt_ketnoimang),
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        })
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
            } else {
                ApiUtils.getData().updateProfile(
                    iduser,
                    tenUser,
                    ngaySinh,
                    diaChi,
                    moTa,
                    sdt,
                    anhuser!!,
                    anhbiauser!!
                ).enqueue(object : Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        if (response.isSuccessful) {
                            progressDialog.dismiss()
                            editor.putString("tenuser", tenUser)
                            editor.putString("motauser", moTa)
                            editor.putString("quequanuser", diaChi)
                            editor.putString("ngaysinhuser", ngaySinh)
                            editor.putInt("sdtuser", sdt)
                            editor.commit()
//                            startActivity(Intent(this@FixProfileActivity,MainActivity::class.java))
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@FixProfileActivity,
                            resources.getString(com.mth.example.socialmediaapp.R.string.txt_ketnoimang),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }

    private fun initControls() {
        if (anhbiauser!!.length > 0) {
            Glide.with(this).load(anhbiauser).into(img_anh_bia_fix_profile)
        }
        if (anhuser!!.length > 0) {
            Glide.with(this).load(anhuser).into(img_avt_user_fix_profile)
        }
        edt_ten_fix_profile.setText(tenuser)
        edt_mo_ta_fix_profile.setText(motauser)
        edt_dia_chi_fix_profile.setText(quequanuser)
        edt_ngay_sinh_fix_profile.setText(ngaysinhuser)
        edt_sdt_fix_profile.setText(sdtuser.toString())
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
        if (key) {
            activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        } else {
            activityResultLauncher2.launch(Intent.createChooser(intent, "Select Picture"))
        }
    }
}
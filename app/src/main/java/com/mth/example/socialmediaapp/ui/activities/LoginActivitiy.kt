@file:Suppress("DEPRECATION")

package com.mth.example.socialmediaapp.ui.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.utils.Status
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import com.mth.example.socialmediaapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login_activitiy.*

@Suppress("DEPRECATION")
class LoginActivitiy : AppCompatActivity() {
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(this.application)
        )[UserViewModel::class.java]
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
    private val statusViewModel: StatusViewModel by lazy {
        ViewModelProvider(
            this,
            StatusViewModel.StatusViewModelFactory(this.application)
        )[StatusViewModel::class.java]
    }
    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activitiy)
        progressDialog.setMessage(resources.getString(R.string.txt_please_wait))
        initEvents()
    }

    private fun initEvents() {
        but_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        but_login.setOnClickListener {
            val email: String = edt_taikhoan.text.toString().trim()
            val password: String = edt_matkhau.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.txt_messagehaynhapdu),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                login(email, password)
            }
        }
    }

    private fun login(email: String, password: String) {
        progressDialog.show()
        userViewModel.getUserFromApi().observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            var x: Int = 0
                            for (user in users) {
                                if (user.UserName.equals(email) && user.PassWord.equals(password)) {
                                    editor.putBoolean("tinhtrang", true)
                                    editor.putInt("iduser", user.Id)
                                    editor.putString("tenuser", user.TenUser)
                                    editor.putString("anhuser", user.HinhAnh)
                                    editor.putString("anhbiauser", user.AnhBia)
                                    editor.putString("motauser", user.MoTa)
                                    editor.putString("quequanuser", user.QueQuan)
                                    editor.putString("ngaysinhuser", user.Birthday)
                                    editor.putInt("sdtuser", user.Sdt)
                                    editor.commit()
                                    loadNewData()
//                                    startActivity(Intent(this, MainActivity::class.java))
//                                    finish()
                                    break
                                } else x++
                            }
                            if (x == users.size) {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    this,
                                    resources.getString(R.string.txt_loginfail),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    Status.ERROR -> {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this,
                            resources.getString(R.string.txt_ketnoimang),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }

    private fun loadNewData() {
        statusViewModel.getStatusFromApi(iduser).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { statuses ->
                            statusViewModel.insertStatus(statuses)
                            statusViewModel.getAllStatus().observe(this) {
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
                                progressDialog.dismiss()
                                Toast.makeText(
                                    this,
                                    resources.getString(R.string.txt_loginsuccess),
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
                            resources.getString(R.string.txt_ket_noi_mang_yeu),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("AAA", it.message.toString())
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }
}
package com.mth.example.socialmediaapp.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.model.Status
import com.mth.example.socialmediaapp.utils.CheckInternetUtils
import com.mth.example.socialmediaapp.utils.Status.SUCCESS
import com.mth.example.socialmediaapp.utils.Status.ERROR
import com.mth.example.socialmediaapp.utils.Status.LOADING
import com.mth.example.socialmediaapp.viewmodel.StatusViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("datalogin", Context.MODE_PRIVATE)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initControls()
    }

    private fun initControls() {
        val statusLogin = sharedPreferences.getBoolean("tinhtrang", false)
//        Handler().postDelayed(Runnable() {
//            if (statusLogin == true) {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            } else {
//                startActivity(Intent(this, LoginActivitiy::class.java))
//                finish()
//            }
//        }, 1200)
        if (statusLogin) {
            if (CheckInternetUtils.isNetWorkAvailable(this)) {
                loadNewData()
            } else {
                Handler().postDelayed(Runnable() {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.txt_ketnoimang_xemlai),
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 600)

            }
        } else {
            if (CheckInternetUtils.isNetWorkAvailable(this)) {
                Handler().postDelayed(Runnable() {
                    startActivity(Intent(this, LoginActivitiy::class.java))
                    finish()
                }, 600)

            } else {
                Handler().postDelayed(Runnable() {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.txt_ketnoimang_xemlai),
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this, LoginActivitiy::class.java))
                    finish()
                }, 600)
            }
        }
    }


    private fun loadNewData() {
        statusViewModel.getStatusFromApi(iduser).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
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
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                    ERROR -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.txt_ket_noi_mang_yeu),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("AAA", it.message.toString())
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    LOADING -> {
                    }
                }
            }
        }
    }
}
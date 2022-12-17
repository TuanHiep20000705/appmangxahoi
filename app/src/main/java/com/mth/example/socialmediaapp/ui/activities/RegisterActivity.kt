@file:Suppress("DEPRECATION")

package com.mth.example.socialmediaapp.ui.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.model.User
import com.mth.example.socialmediaapp.utils.ApiUtils
import com.mth.example.socialmediaapp.utils.Status
import com.mth.example.socialmediaapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(this.application)
        )[UserViewModel::class.java]
    }
    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        progressDialog.setMessage(resources.getString(R.string.txt_please_wait))
        initEvents()
    }

    private fun initEvents() {
        but_back_register.setOnClickListener {
            startActivity(Intent(this, LoginActivitiy::class.java))
        }

        edt_birthday_register.setOnFocusChangeListener { view, b -> chonNgay() }

        but_dangky_register.setOnClickListener {
            val tenUser = edt_name_register.text.toString().trim()
            val email = edt_email_register.text.toString().trim()
            val password = edt_matkhau_register.text.toString().trim()
            val confirmPassword = edt_matkhau_xacnhan_register.text.toString().trim()
            val address = edt_address_register.text.toString().trim()
            val phone = edt_phone_register.text.toString().trim()
            val birthday = edt_birthday_register.text.toString().trim()
            if (tenUser.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                || address.isEmpty() || phone.isEmpty() || birthday.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.txt_messagehaynhapdu),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (isValidEmail(email) && isValidPassword(password)) {
                    if (isCorrectPassword(password, confirmPassword)) {
                        progressDialog.show()
                        userViewModel.getUserFromApi().observe(this) {
                            it?.let { resource ->
                                when (resource.status) {
                                    Status.SUCCESS -> {
                                        resource.data?.let { users ->
                                            var x: Int = 0
                                            for (user in users) {
                                                if (user.UserName.equals(email)) {
                                                    progressDialog.show()
                                                    Toast.makeText(
                                                        this,
                                                        resources.getString(R.string.txt_emailexist),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    break
                                                } else x++
                                            }
                                            if (x == users.size) {
                                                val idNewUser: Int = users[users.size - 1].Id + 1
                                                registerUser(
                                                    idNewUser,
                                                    tenUser,
                                                    email,
                                                    password,
                                                    address,
                                                    phone,
                                                    birthday
                                                )
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
                    } else {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.txt_messagexacnhanpass),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.txt_messagevalid),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun registerUser(
        idNewUser: Int,
        tenUser: String,
        email: String,
        password: String,
        address: String,
        phone: String,
        birthday: String
    ) {
        val sdt = phone.toInt()
        val user: User = User(email, password, tenUser, birthday, address, "", sdt, "","")

        ApiUtils.getData().setDataSearchFriend(idNewUser, tenUser, address)
            .enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        val stringResponse = response.body()
                        if (stringResponse?.trim().equals("success")) {
                            userViewModel.registerUser(user).observe(this@RegisterActivity) {
                                it?.let { resource ->
                                    when (resource.status) {
                                        Status.SUCCESS -> {
                                            resource.data?.let { response ->
                                                if (response.trim().equals("success")) {
                                                    progressDialog.dismiss()
                                                    Toast.makeText(
                                                        this@RegisterActivity,
                                                        resources.getString(R.string.txt_registersuccess),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    startActivity(
                                                        Intent(
                                                            this@RegisterActivity,
                                                            LoginActivitiy::class.java
                                                        )
                                                    )
                                                    finish()
                                                }
                                            }
                                        }
                                        Status.ERROR -> {
                                            progressDialog.dismiss()
                                            Toast.makeText(
                                                this@RegisterActivity,
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
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@RegisterActivity,
                        resources.getString(R.string.txt_ketnoimang),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    private fun chonNgay() {
        val cal = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                cal[i, i1] = i2
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                edt_birthday_register.setText(sdf.format(cal.time))
            },
            cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE]
        )
        datePickerDialog.show()
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return !TextUtils.isEmpty(password) && password.length >= 6
    }

    private fun isCorrectPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}
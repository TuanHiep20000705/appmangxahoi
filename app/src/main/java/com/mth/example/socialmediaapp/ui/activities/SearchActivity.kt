package com.mth.example.socialmediaapp.ui.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.adapter.SearchFriendAdapter
import com.mth.example.socialmediaapp.model.SearchFriend
import com.mth.example.socialmediaapp.utils.ApiUtils
import com.mth.example.socialmediaapp.utils.Status
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : AppCompatActivity() {

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    }

    private val iduser: Int by lazy {
        sharedPreferences.getInt("iduser", 1)
    }
    private val anhuser: String? by lazy {
        sharedPreferences.getString("anhuser", "")
    }
    private val tenuser: String? by lazy {
        sharedPreferences.getString("tenuser", "")
    }

    private val searchFriendAdapter: SearchFriendAdapter by lazy {
        SearchFriendAdapter(this, onItemClick, onThemBanClick)
    }

    lateinit var listSearchFriend: kotlin.collections.List<SearchFriend>

    private var filterList = mutableListOf<SearchFriend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        but_back_search.setOnClickListener {
            finish()
        }
        edt_search_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filterList.clear()
                if (editable.toString().isEmpty()) {
                    val searchFriendAdapter1 =
                        SearchFriendAdapter(this@SearchActivity, onItemClick, onThemBanClick)
                    searchFriendAdapter1.setData(listSearchFriend)
                    rcv_search_friend.setAdapter(searchFriendAdapter1)
                    searchFriendAdapter.notifyDataSetChanged()
                } else {
                    searchFilter(editable.toString())
                }
            }
        })
    }

    private fun searchFilter(text: String) {
        for (searchFriend in listSearchFriend) {
            if (searchFriend.tenlistuser.lowercase()
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filterList.add(searchFriend)
            }
        }
        val searchFriendAdapter2 =
            SearchFriendAdapter(this@SearchActivity, onItemClick, onThemBanClick)
        searchFriendAdapter2.setData(filterList)
        rcv_search_friend.setAdapter(searchFriendAdapter2)
        searchFriendAdapter.notifyDataSetChanged()
    }

    private fun initControls() {
        rcv_search_friend.setHasFixedSize(true)
        rcv_search_friend.layoutManager = LinearLayoutManager(this)
        rcv_search_friend.adapter = searchFriendAdapter
        ApiUtils.getData().getDataSearchFriend(iduser)
            .enqueue(object : Callback<List<SearchFriend>> {
                override fun onResponse(
                    call: Call<List<SearchFriend>>,
                    response: Response<List<SearchFriend>>
                ) {
                    if (response.isSuccessful) {
                        listSearchFriend = response.body()!!
                        searchFriendAdapter.setData(listSearchFriend)
                    }
                }

                override fun onFailure(call: Call<List<SearchFriend>>, t: Throwable) {
                    Toast.makeText(
                        this@SearchActivity,
                        resources.getString(R.string.txt_ketnoimang),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private val onItemClick: (SearchFriend) -> Unit = {
        val intent = Intent(this, InfoProfileActivity::class.java)
        intent.putExtra("dulieuidusersearch", it.idlistuser)
        startActivity(intent)
    }
    private val onThemBanClick: (SearchFriend, Button) -> Unit = { searchFriend, button ->
        val cal: Calendar = Calendar.getInstance()
        val t: Date = cal.time
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        if (button.text.equals(resources.getString(R.string.but_ket_ban))) {
            button.setText(resources.getString(R.string.but_dang_gui_ket_ban))
            ApiUtils.getData().updateStateFriend(searchFriend.id, 2)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        ApiUtils.getData().insertFriendRequest(
                            iduser,
                            tenuser!!,
                            anhuser!!,
                            searchFriend.idlistuser,
                            sdf.format(t)
                        ).enqueue(object : Callback<String> {
                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {

                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {

                            }
                        })
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {

                    }
                })
        } else if (button.text.equals(resources.getString(R.string.but_ban_be))) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(resources.getString(R.string.txt_thong_bao))
            alertDialog.setMessage(resources.getString(R.string.txt_muon_huy_ket_ban))
            alertDialog.setPositiveButton(resources.getString(R.string.txt_co)) { dialogInterface, i ->
                button.setText(resources.getString(R.string.but_ket_ban))
                ApiUtils.getData().huyKetBan(iduser, searchFriend.idlistuser)
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful) {
                                if (response.body().equals("success")) {
                                    Toast.makeText(
                                        this@SearchActivity,
                                        resources.getString(R.string.txt_da_huy_ban_be),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(
                                this@SearchActivity,
                                resources.getString(R.string.txt_ketnoimang),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            alertDialog.setNegativeButton(resources.getString(R.string.txt_ko)) { dialogInterface, i ->

            }
            alertDialog.show()
        } else {
            button.setText(resources.getString(R.string.but_ket_ban))
            ApiUtils.getData().updateStateFriend(searchFriend.id, 0)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        ApiUtils.getData().deleteFriendRequest(iduser, searchFriend.idlistuser)
                            .enqueue(object : Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {

                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {

                                }
                            })
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {

                    }
                })
        }
    }
}
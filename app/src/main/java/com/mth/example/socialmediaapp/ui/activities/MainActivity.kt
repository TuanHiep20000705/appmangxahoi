package com.mth.example.socialmediaapp.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mth.example.socialmediaapp.R
import com.mth.example.socialmediaapp.model.Status
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MoveToCreateContent, MoveToCreateImgContent,
    MoveToComment, MoveToEditContent, MoveToEditImgContent, LogOutToLoginActivity,
    MoveToFixProfile, MoveFromNotifyToComment {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    }

    private val editor: SharedPreferences.Editor by lazy {
        sharedPreferences.edit()
    }
    private val controller: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initEvents()
    }

    private fun initEvents() {
        bottom_nav.setupWithNavController(controller)
        but_search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    override fun initCreateContent() {
        startActivity(Intent(this, CreateContentActivity::class.java))
    }

    override fun initCreateImgContent() {
        startActivity(Intent(this, CreateImgContentActivity::class.java))
    }

    override fun initComentEvent(status: Status) {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("dulieustatus", status)
        startActivity(intent)
    }

    override fun initEditContent(status: Status) {
        val intent = Intent(this, EditContentActivity::class.java)
        intent.putExtra("dulieueditcontent", status)
        startActivity(intent)
    }

    override fun initEditImgContent(status: Status) {
        val intent = Intent(this, EditImgContentActivity::class.java)
        intent.putExtra("dulieueditimgcontent", status)
        startActivity(intent)
    }

    override fun initLogOutEvent() {
        editor.clear()
        editor.commit()
        startActivity(Intent(this, LoginActivitiy::class.java))
        finish()
    }

    override fun initFixProfile() {
        startActivity(Intent(this, FixProfileActivity::class.java))
    }

    override fun initMoveFromNotyToComment(status: Status) {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("dulieustatus", status)
        startActivity(intent)
    }
}
package com.alfrosoft.firebaseimageupload

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun openUploadPage(view: View) {

        val intent = Intent(this, StorageActivity::class.java)
        startActivity(intent)
    }
}
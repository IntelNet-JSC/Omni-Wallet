package com.intelnet.omniwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.intelnet.mylibrary.ui.intro.IntroActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.btnStart).setOnClickListener {
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }
    }
}
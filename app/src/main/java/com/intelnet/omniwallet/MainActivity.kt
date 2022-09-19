package com.intelnet.omniwallet

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.intelnet.mylibrary.OmniWallet

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.btnStart).setOnClickListener {
            OmniWallet
                .with(this)
                .start()
        }
    }
}
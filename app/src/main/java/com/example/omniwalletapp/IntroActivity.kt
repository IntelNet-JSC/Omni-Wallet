package com.example.omniwalletapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.omniwalletapp.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
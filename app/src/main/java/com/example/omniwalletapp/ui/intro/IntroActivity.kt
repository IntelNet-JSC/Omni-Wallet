package com.example.omniwalletapp.ui.intro

import android.content.Intent
import android.os.Bundle
import com.example.omniwalletapp.base.BaseActivity
import com.example.omniwalletapp.databinding.ActivityIntroBinding
import com.example.omniwalletapp.ui.addWallet.AddWalletActivity
import com.example.omniwalletapp.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding

    private val keydir: File by lazy {
        File(filesDir, "keystore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            if (keydir.exists() && keydir.list()?.isNotEmpty() == true)
                startActivity(Intent(this, HomeActivity::class.java))
            else
                startActivity(Intent(this, AddWalletActivity::class.java))
            finish()

        }

    }
}
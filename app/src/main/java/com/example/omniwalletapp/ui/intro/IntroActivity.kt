package com.example.omniwalletapp.ui.intro

import android.content.Intent
import android.os.Bundle
import com.example.omniwalletapp.base.BaseActivity
import com.example.omniwalletapp.databinding.ActivityIntroBinding
import com.example.omniwalletapp.ui.addWallet.AddWalletActivity
import dagger.hilt.android.AndroidEntryPoint

class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, AddWalletActivity::class.java))
            finish()
        }
    }
}
package com.example.omniwalletapp.ui.home

import android.content.Intent
import android.os.Bundle
import com.example.omniwalletapp.base.BaseActivity
import com.example.omniwalletapp.databinding.ActivityHomeBinding
import com.example.omniwalletapp.ui.addWallet.AddWalletActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding

    private val keydir: File by lazy {
        File(filesDir, "keystore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        if(!keydir.exists()){
            navigateAddWalletActivity()
        }
    }

    fun navigateAddWalletActivity(){
        startActivity(Intent(this, AddWalletActivity::class.java))
        finish()
    }

    fun restart(){
        val mIntent = intent
        finish()
        startActivity(mIntent)
    }
}
package com.intelnet.mylibrary.ui.intro

import android.content.Intent
import android.os.Bundle
import com.intelnet.mylibrary.base.BaseActivity
import com.intelnet.mylibrary.databinding.ActivityIntroBinding
import com.intelnet.mylibrary.repository.PreferencesRepository
import com.intelnet.mylibrary.ui.addWallet.AddWalletActivity
import com.intelnet.mylibrary.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@AndroidEntryPoint
class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding

    @Singleton
    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    private val keydir: File by lazy {
        File(filesDir, "keystore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (keydir.exists() && keydir.list()?.isNotEmpty() == true&&preferencesRepository.getAddress().isNotEmpty())
            startActivity(Intent(this, HomeActivity::class.java))
        else
            startActivity(Intent(this, AddWalletActivity::class.java))
        finish()

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if(preferencesRepository.getStartIntro())
//        {
//            if (keydir.exists() && keydir.list()?.isNotEmpty() == true&&preferencesRepository.getAddress().isNotEmpty())
//                startActivity(Intent(this, HomeActivity::class.java))
//            else
//                startActivity(Intent(this, AddWalletActivity::class.java))
//            finish()
//        }
//
//        preferencesRepository.setStartIntro(true)
//
//        binding.btnStart.setOnClickListener {
//            if (keydir.exists() && keydir.list()?.isNotEmpty() == true&&preferencesRepository.getAddress().isNotEmpty())
//                startActivity(Intent(this, HomeActivity::class.java))
//            else
//                startActivity(Intent(this, AddWalletActivity::class.java))
//            finish()
//
//        }

    }
}
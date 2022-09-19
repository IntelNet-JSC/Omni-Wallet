package com.intelnet.mylibrary.ui.intro

import android.content.Intent
import android.os.Bundle
import com.intelnet.mylibrary.base.BaseActivity
import com.intelnet.mylibrary.databinding.ActivityIntroBinding
import com.intelnet.mylibrary.entity.Setting
import com.intelnet.mylibrary.repository.PreferencesRepository
import com.intelnet.mylibrary.ui.addWallet.AddWalletActivity
import com.intelnet.mylibrary.ui.home.HomeActivity
import com.intelnet.mylibrary.util.Constants
import com.intelnet.mylibrary.OmniWallet
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    private val keydir: File by lazy {
        File(filesDir, "keystore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.run {
            preferencesRepository.setSetting(
                Setting(
                    disableNetwork = this.getBoolean(OmniWallet.EXTRA_DISABLE_OPEN_NETWORK, false),
                    disableAddToken = this.getBoolean(OmniWallet.EXTRA_DISABLE_ADD_TOKEN, false)
                )
            )
            preferencesRepository.setDefaultNetwork(
                if(this.getBoolean(OmniWallet.EXTRA_BSC_TESTNET_IS_DEFAULT, false))
                    Constants.BSC_TESTNET
                else
                    Constants.ROPSTEN_NETWORK_NAME
            )
        }


        if (keydir.exists() && keydir.list()
                ?.isNotEmpty() == true && preferencesRepository.getAddress().isNotEmpty()
        )
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
package com.intelnet.omniwallet.ui.addWallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.intelnet.omniwallet.R
import com.intelnet.omniwallet.base.BaseActivity
import com.intelnet.omniwallet.databinding.ActivityAddWalletBinding
import com.intelnet.omniwallet.ui.addWallet.createWallet.ConfirmPhraseFragment
import com.intelnet.omniwallet.ui.addWallet.createWallet.MemorizePhraseFragment
import com.intelnet.omniwallet.ui.home.HomeActivity
import java.io.File

class AddWalletActivity : BaseActivity() {

    lateinit var binding: ActivityAddWalletBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentAddWallet) as NavHostFragment
        navController = navHostFragment.navController

        binding.toolbarAddWallet.imgBack.setOnClickListener {
            hideKeyboard()
            if (!navController.popBackStack()) {
                finish()
//                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            } else {
                val fragment = getForegroundFragment()
                if (fragment != null && (fragment is MemorizePhraseFragment || fragment is ConfirmPhraseFragment))
                    deleteDir(File(filesDir, ""))
            }
        }
    }

    private fun getForegroundFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentAddWallet)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    fun navigateHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val childrens = dir.list() ?: return false
            for (i in childrens.indices) {
                val success = deleteDir(File(dir, childrens[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }
}
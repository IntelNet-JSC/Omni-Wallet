package com.example.omniwalletapp.ui.addWallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseActivity
import com.example.omniwalletapp.databinding.ActivityAddWalletBinding
import com.example.omniwalletapp.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

class AddWalletActivity : BaseActivity() {

    lateinit var binding: ActivityAddWalletBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentAddWallet) as NavHostFragment
        navController = navHostFragment.navController

        binding.toolbarAddWallet.imgBack.setOnClickListener {
            hideKeyboard()
            if (!navController.popBackStack()) {
                finish()
//                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
        }
    }

    private fun getForegroundFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentAddWallet)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    fun navigateHomeActivity(){
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
}
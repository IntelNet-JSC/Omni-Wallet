package com.example.omniwalletapp.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentLoginLaterBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginLaterFragment : BaseFragment<FragmentLoginLaterBinding, LoginLaterViewModel>() {

    override val viewModel: LoginLaterViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginLaterBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.btnUnlock.setOnClickListener {
            showLoadingDialog()
        }
    }

    override fun initUI() {

    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

}
package com.example.omniwalletapp.ui.addWallet.createWallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentAddWalletBinding
import com.example.omniwalletapp.databinding.FragmentCreatePassBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePassFragment : BaseFragment<FragmentCreatePassBinding, CreateWalletViewModel>() {

    override val viewModel: CreateWalletViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCreatePassBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.btnCreatePass.setOnClickListener {
            navigate(
                CreatePassFragmentDirections.actionCreatePassFragmentToMemorizePhraseFragment()
            )
        }
    }

    override fun initUI() {

    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

}
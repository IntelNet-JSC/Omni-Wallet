package com.example.omniwalletapp.ui.addWallet.importWallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentAddWalletBinding
import com.example.omniwalletapp.databinding.FragmentImportKeyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImportKeyFragment : BaseFragment<FragmentImportKeyBinding, ImportWalletViewModel>() {

    override val viewModel: ImportWalletViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentImportKeyBinding.inflate(inflater, container, false)

    override fun initControl() {

    }

    override fun initUI() {

    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

}
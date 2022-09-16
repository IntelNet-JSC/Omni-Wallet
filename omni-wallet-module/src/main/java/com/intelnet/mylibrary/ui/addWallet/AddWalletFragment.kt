package com.intelnet.mylibrary.ui.addWallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.intelnet.mylibrary.base.BaseFragment
import com.intelnet.mylibrary.base.EmptyViewModel
import com.intelnet.mylibrary.databinding.FragmentAddWalletBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWalletFragment : BaseFragment<FragmentAddWalletBinding, EmptyViewModel>() {

    override val viewModel: EmptyViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddWalletBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.btnAddWallet.setOnClickListener {
            navigate(
                AddWalletFragmentDirections.actionAddWalletFragmentToCreatePassFragment()
            )
        }

        binding.btnImportPrivate.setOnClickListener {
            navigate(
                AddWalletFragmentDirections.actionAddWalletFragmentToImportKeyFragment()
            )
        }

        binding.btnImportPhrase.setOnClickListener {
            navigate(
                AddWalletFragmentDirections.actionAddWalletFragmentToImportPhraseFragment()
            )
        }

        binding.txtTAndC.setOnClickListener {
            openUrlTandC()
        }
    }

    override fun initUI() {

    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

}
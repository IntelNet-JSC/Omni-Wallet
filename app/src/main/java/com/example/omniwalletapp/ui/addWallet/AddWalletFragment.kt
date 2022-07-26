package com.example.omniwalletapp.ui.addWallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentAddWalletBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWalletFragment : BaseFragment<FragmentAddWalletBinding, AddWalletViewModel>() {

    override val viewModel: AddWalletViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddWalletBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.btnAddWallet.setOnClickListener {
            showLoadingDialog()
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
            showToast("Điều khoản ...")
        }
    }

    override fun initUI() {

    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

}
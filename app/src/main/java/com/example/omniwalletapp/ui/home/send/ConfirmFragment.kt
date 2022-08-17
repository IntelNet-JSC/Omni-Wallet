package com.example.omniwalletapp.ui.home.send

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentAmountBinding
import com.example.omniwalletapp.databinding.FragmentConfirmBinding
import com.example.omniwalletapp.util.formatAddressWallet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConfirmFragment : BaseFragment<FragmentConfirmBinding, SendTokenViewModel>() {

    override val viewModel: SendTokenViewModel by viewModels()


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentConfirmBinding.inflate(inflater, container, false)

    override fun initControl() {

        binding.imgBack.setOnClickListener {
            backToPrevious()
        }

        binding.txtCancel.setOnClickListener {
            navigate(
                ConfirmFragmentDirections.actionConfirmFragmentToHomeFragment()
            )
        }

        binding.btnSend.setOnClickListener {
            navigate(
                ConfirmFragmentDirections.actionConfirmFragmentToDetailTokenFragment()
            )
        }

    }

    override fun initUI() {
        val strAddressWallet = getString(R.string.address_demo)
        binding.txtAddressFill.text = strAddressWallet.formatAddressWallet()
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }


}
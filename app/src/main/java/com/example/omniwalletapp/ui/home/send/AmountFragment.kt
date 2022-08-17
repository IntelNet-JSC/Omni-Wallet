package com.example.omniwalletapp.ui.home.send

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentAmountBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AmountFragment : BaseFragment<FragmentAmountBinding, SendTokenViewModel>() {

    override val viewModel: SendTokenViewModel by viewModels()


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAmountBinding.inflate(inflater, container, false)

    override fun initControl() {

        binding.imgBack.setOnClickListener {
            backToPrevious()
        }

        binding.txtCancel.setOnClickListener {
            navigate(
                AmountFragmentDirections.actionAmountFragmentToHomeFragment()
            )
        }

        binding.txtSymbolToken.setOnClickListener {
            ChooseTokenDialogFragment.newInstance(
                fManager,
                mutableListOf(),
                chooseTokenListener = {
                    showToast("Item Token Click")
                }
            )
        }

        binding.btnContinue.setOnClickListener {
            navigate(
                AmountFragmentDirections.actionAmountFragmentToConfirmFragment()
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
package com.example.omniwalletapp.ui.home.detailToken

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentInforSmartContractBinding
import com.example.omniwalletapp.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SmartContractDetailFragment :
    BaseFragment<FragmentInforSmartContractBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by activityViewModels()

    private val args: SmartContractDetailFragmentArgs by navArgs()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentInforSmartContractBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.imgBack.setOnClickListener {
            backToPrevious()
        }

    }

    override fun initUI() {
        initDefaultNetwork()
    }

    private fun initDefaultNetwork() {
        val itemNetwork = viewModel.getItemNetworkDefault()
        binding.txtSymbolToken.text = viewModel.lstToken[args.indexToken].symbol
        itemNetwork?.let {
            binding.txtDot.setBackgroundColor(it.color)
            binding.txtNet.text = it.name
        }
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

}
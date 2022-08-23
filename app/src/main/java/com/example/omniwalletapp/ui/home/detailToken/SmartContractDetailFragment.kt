package com.example.omniwalletapp.ui.home.detailToken

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mylibrary.utils.identicon.Identicon
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentInforSmartContractBinding
import com.example.omniwalletapp.repository.PreferencesRepository
import com.example.omniwalletapp.ui.home.HomeViewModel
import com.example.omniwalletapp.ui.home.adapter.ItemToken
import com.example.omniwalletapp.util.formatAddressWallet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SmartContractDetailFragment :
    BaseFragment<FragmentInforSmartContractBinding, HomeViewModel>() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override val viewModel: HomeViewModel by activityViewModels()

    private val args: SmartContractDetailFragmentArgs by navArgs()

    private val itemToken: ItemToken by lazy {
        viewModel.lstToken[args.indexToken]
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentInforSmartContractBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.imgBack.setOnClickListener {
            backToPrevious()
        }

        binding.txtContractAddress.setOnClickListener {
            copyToClipboard(itemToken.address)
            showToast(getString(R.string.toast_address_smart_contract_copied))
        }

        binding.txtHideToken.setOnClickListener {
            preferencesRepository.hideTokenAddress(
                args.indexToken - 1,
                viewModel.getSymbolNetworkDefault()
            )

            val navOptions =
                NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
            findNavController().navigate(R.id.homeFragment, null, navOptions)
        }
    }

    override fun initUI() {
        initDefaultNetwork()
        iniTokenDefault()
    }

    private fun initDefaultNetwork() {
        val itemNetwork = viewModel.getItemNetworkDefault()
        itemNetwork?.let {
            binding.txtDot.setBackgroundColor(it.color)
            binding.txtNet.text = it.name
            binding.txtNetwork.text = it.name
        }
    }

    private fun iniTokenDefault() {
        if (args.indexToken == 0)
            return
        itemToken.run {
            Identicon(binding.imgToken, address)
            binding.txtNameToken.text = symbol
            binding.txtTokenAmount.text = amount
            binding.txtContractAddress.text = address.formatAddressWallet()
        }
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

}
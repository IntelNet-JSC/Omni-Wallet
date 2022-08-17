package com.example.omniwalletapp.ui.home.send

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.mylibrary.utils.identicon.Identicon
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentConfirmBinding
import com.example.omniwalletapp.ui.home.HomeViewModel
import com.example.omniwalletapp.ui.home.adapter.ItemToken
import com.example.omniwalletapp.util.BalanceUtil
import com.example.omniwalletapp.util.Status
import com.example.omniwalletapp.util.formatAddressWallet
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal


@AndroidEntryPoint
class ConfirmFragment : BaseFragment<FragmentConfirmBinding, HomeViewModel>() {

    //    override val viewModel: SendTokenViewModel by viewModels()
    override val viewModel: HomeViewModel by activityViewModels()

    private val args: ConfirmFragmentArgs by navArgs()


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentConfirmBinding.inflate(inflater, container, false)

    override fun initControl() {

        binding.imgBack.setOnClickListener {
            backToPrevious()
        }

        binding.txtCancel.setOnClickListener {
            backToPrevious(R.id.homeFragment)
        }

        binding.btnSend.setOnClickListener {
            navigate(
                ConfirmFragmentDirections.actionConfirmFragmentToDetailTokenFragment(args.indexToken)
            )
        }

    }

    override fun initUI() {
        initDefaultNetwork()
        initUiToAddress()
        initUiFromAddress(viewModel.lstToken[args.indexToken])
    }

    private fun initDefaultNetwork() {
        val itemNetwork = viewModel.getItemNetworkDefault()
        itemNetwork?.let {
            binding.txtDot.setBackgroundColor(it.color)
            binding.txtNet.text = it.name
        }
    }

    private fun initUiFromAddress(item: ItemToken) {
        val balanceFormat = BalanceUtil.formatBalanceWithSymbol(item.amount, item.symbol)
        binding.txtBalance.text =
            getString(R.string.content_balance_from_send, balanceFormat) // layout from address
        Identicon(binding.imgAvatarFrom, viewModel.credentials?.address)

        // amount input
        binding.txtAmount.text = BalanceUtil.formatBalanceWithSymbol(args.amount, item.symbol)
    }

    private fun initUiToAddress() {
        args.toAddress.run {
            Identicon(binding.imgAvatarTo, this)
            binding.txtAddressFill.text = this.formatAddressWallet()
        }
    }

    private fun initAmountEstimate(amountEstimateEth: BigDecimal, item: ItemToken) {
        val estimateAmountEth = BalanceUtil.formatBalanceWithSymbol(
            amountEstimateEth.toString(),
            viewModel.getSymbolNetworkDefault()
        )
        binding.txtGas.text = estimateAmountEth

        if (args.indexToken == 0) { // native token
            val total = BigDecimal(args.amount) + amountEstimateEth
            binding.txtTotal.text = BalanceUtil.formatBalanceWithSymbol(
                total.toString(),
                viewModel.getSymbolNetworkDefault()
            )
        } else {
            val amountTokenFormat = BalanceUtil.formatBalanceWithSymbol(args.amount, item.symbol)
            binding.txtTotal.text =
                StringBuilder(amountTokenFormat).append(" + ").append(estimateAmountEth)
        }
    }

    override fun initEvent() {
        viewModel.estimateLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                when (data.responseType) {
                    Status.LOADING -> {
                        showLoadingDialog()
                    }
                    Status.SUCCESSFUL -> {
                        hideDialog()
                        data.data?.let {
                            initAmountEstimate(it, viewModel.lstToken[args.indexToken])
                        }
                    }
                    Status.ERROR -> {
                        hideDialog()
                        data.error?.message?.let {
                            showToast("Error: $it")
                        }
                    }
                }
            }
        }
    }

    override fun initConfig() {
        viewModel.getEstimateGas(
            fromAddress = viewModel.credentials!!.address,
            toAddress = if (args.indexToken == 0) args.toAddress else viewModel.lstToken[args.indexToken].address,
            amount = args.amount
        )

    }


}
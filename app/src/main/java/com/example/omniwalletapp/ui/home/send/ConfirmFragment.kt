package com.example.omniwalletapp.ui.home.send

import android.text.SpannableString
import android.text.style.UnderlineSpan
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
import com.example.omniwalletapp.ui.home.send.dialog.EditGasDialogFragment
import com.example.omniwalletapp.ui.home.send.dialog.InforDialogFragment
import com.example.omniwalletapp.util.BalanceUtil
import com.example.omniwalletapp.util.Status
import com.example.omniwalletapp.util.formatAddressWallet
import com.example.omniwalletapp.util.trimTrailingZero
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.math.BigDecimal


@AndroidEntryPoint
class ConfirmFragment : BaseFragment<FragmentConfirmBinding, HomeViewModel>() {

    //    override val viewModel: SendTokenViewModel by viewModels()
    override val viewModel: HomeViewModel by activityViewModels()

    private val args: ConfirmFragmentArgs by navArgs()

    private val isNativeToken: Boolean by lazy {
        args.indexToken == 0
    }

    private val itemToken: ItemToken by lazy {
        viewModel.lstToken[args.indexToken]
    }

    private val amount: String by lazy {
        args.amount
    }

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
            viewModel.transfer(args.toAddress, amount, itemToken.address)
        }

        binding.txtTextGas.setOnClickListener {
            InforDialogFragment.newInstance(
                fm = fManager,
                title = getString(R.string.what_is_gas),
                content = getString(R.string.gas_education_1)
            )
        }

        binding.txtGas.setOnClickListener {
            EditGasDialogFragment.newInstance(
                fManager,
                viewModel.getSymbolNetworkDefault(),
                viewModel.gasPrice,
                viewModel.gasLimit,
                callbackAction = { gasPrice, gasLimit ->

                    viewModel.gasPrice = gasPrice
                    viewModel.gasLimit = gasLimit

                    initAmountEstimate()
                }
            )
        }

    }


    override fun initUI() {
        initDefaultNetwork()
        initUiToAddress()
        initUiFromAddress(itemToken)
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
        Identicon(binding.imgAvatarFrom, addressWallet)

        // amount input
        binding.txtAmount.text = BalanceUtil.formatBalanceWithSymbol(amount, item.symbol)
    }

    private fun initUiToAddress() {
        args.toAddress.run {
            Identicon(binding.imgAvatarTo, this)
            binding.txtAddressFill.text = this.formatAddressWallet()
        }
    }

    private fun initAmountEstimate() {
        val amountEstimateEth =
            BalanceUtil.convertTogEstimateGasEth(viewModel.gasPrice, viewModel.gasLimit)

        val estimateAmountEth = BalanceUtil.formatBalanceWithSymbol(
            amountEstimateEth.toString().trimTrailingZero(),
            viewModel.getSymbolNetworkDefault()
        )

        val mSpannableString = SpannableString(estimateAmountEth)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        binding.txtGas.text = mSpannableString


        if (isNativeToken) {
            val total = BigDecimal(amount) + amountEstimateEth
            binding.txtTotal.text = BalanceUtil.formatBalanceWithSymbol(
                total.toString(),
                viewModel.getSymbolNetworkDefault()
            )
        } else {
            val amountTokenFormat = BalanceUtil.formatBalanceWithSymbol(amount, itemToken.symbol)
            binding.txtTotal.text =
                StringBuilder(amountTokenFormat).append(" + ").append(estimateAmountEth)
        }
    }

    override fun initEvent() {
        viewModel.estimateLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                when (data.responseType) {
                    Status.LOADING -> {
                        binding.btnSend.isEnabled = false
                        showLoadingDialog()
                    }
                    Status.SUCCESSFUL -> {
                        binding.btnSend.isEnabled = true
                        hideDialog()
                        data.data?.let {
                            initAmountEstimate()
                        }
                    }
                    Status.ERROR -> {
                        binding.btnSend.isEnabled = false
                        hideDialog()
                        data.error?.message?.let {
                            showToast("Error: $it")
                        }
                    }
                }
            }
        }

        viewModel.transferLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                when (data.responseType) {
                    Status.LOADING -> {
                        showLoadingDialog()
                    }
                    Status.SUCCESSFUL -> {
                        binding.btnSend.isEnabled = true
                        hideDialog()
                        data.data?.let {
                            Timber.d("$it")
                            navigate(
                                ConfirmFragmentDirections.actionConfirmFragmentToDetailTokenFragment(
                                    args.indexToken,
                                    true
                                )
                            )
                        }
                    }
                    Status.ERROR -> {
//                        binding.btnSend.isEnabled = false
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
            fromAddress = addressWallet,
            toAddress = args.toAddress,
            amount = amount,
            contractAddress = itemToken.address
        )

    }


}
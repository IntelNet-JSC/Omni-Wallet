package com.example.omniwalletapp.ui.home.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentAmountBinding
import com.example.omniwalletapp.ui.home.HomeViewModel
import com.example.omniwalletapp.ui.home.adapter.ItemToken
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AmountFragment : BaseFragment<FragmentAmountBinding, HomeViewModel>() {

//    override val viewModel: SendTokenViewModel by viewModels()
    override val viewModel: HomeViewModel by activityViewModels()

    private val args:AmountFragmentArgs by navArgs()

    private var indexToken:Int = 0

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAmountBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        indexToken = args.indexToken
    }

    override fun initControl() {

        binding.imgBack.setOnClickListener {
            backToPrevious()
        }

        binding.txtCancel.setOnClickListener {
            backToPrevious(R.id.homeFragment)
        }

        binding.txtSymbolToken.setOnClickListener {
            ChooseTokenDialogFragment.newInstance(
                fManager,
                viewModel.lstToken
            ) { index, item ->
                if(index==-1)
                    return@newInstance
                indexToken = index
                binding.edtAmountToken.setText("")
                initBalance(item)
            }
        }

        binding.txtUseMax.setOnClickListener {
            binding.edtAmountToken.setText(getDefaultItemToken().amount)
        }

        binding.btnContinue.setOnClickListener {
            val amount = binding.edtAmountToken.text.toString()
            if(amount.isNotEmpty())
                navigate(
                    AmountFragmentDirections.actionAmountFragmentToConfirmFragment(args.toAddress, indexToken, amount)
                )
            else
                showToast("Please Input Amount!")
        }
    }

    override fun initUI() {
        initDefaultNetwork()
        initBalance(getDefaultItemToken())
    }

    private fun getDefaultItemToken() = viewModel.lstToken[indexToken]

    private fun initDefaultNetwork() {
        val itemNetwork = viewModel.getItemNetworkDefault()
        itemNetwork?.let {
            binding.txtDot.setBackgroundColor(it.color)
            binding.txtNet.text = it.name
        }
    }

    private fun initBalance(item:ItemToken){
        binding.txtSymbolToken.text = item.symbol
        val balanceFormat = StringBuilder().append(item.amount)
            .append(" ${item.symbol}").toString()
        binding.txtBalance.text = getString(R.string.content_balance_from_send, balanceFormat)
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }


}
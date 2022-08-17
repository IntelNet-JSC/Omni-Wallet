package com.example.omniwalletapp.ui.home.addToken

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentAddTokenBinding
import com.example.omniwalletapp.ui.home.HomeFragmentDirections
import com.example.omniwalletapp.ui.home.HomeViewModel
import com.example.omniwalletapp.ui.home.network.NetDialogFragment
import com.example.omniwalletapp.util.Status
import com.example.omniwalletapp.util.setNavigationResult
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import org.web3j.crypto.WalletUtils


@AndroidEntryPoint
class AddTokenFragment : BaseFragment<FragmentAddTokenBinding, HomeViewModel>() {

    //    override val viewModel: AddTokenViewModel by viewModels()
    override val viewModel: HomeViewModel by activityViewModels()

    private var contractAddressTemp: String = ""

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddTokenBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.imgBack.setOnClickListener {
            binding.edtTokenAddress.onFocusChangeListener = null
            backToPrevious()
        }

        binding.btnImportToken.setOnClickListener {
            checkAndCall(true)
        }

        setEventListener(
            requireActivity(),
            viewLifecycleOwner,
            KeyboardVisibilityEventListener { open ->
                if (!open)
                    checkAndCall(false)
            })

        binding.viewClickImportToken.setOnClickListener {
            NetDialogFragment.newInstance(
                fManager,
                viewModel.lstItemNetwork,
                chooseNetworkListener = {
                    setNavigationResult("network_change", it)
                    viewModel.setDefaultNetworkInfo(it)
                    setUiDefaultNetWork()
                },
                addNetworkListener = {
                    navigate(
                        HomeFragmentDirections.actionHomeFragmentToAddNetworkFragment()
                    )
                }
            )
        }

    }

    override fun initUI() {
        setUiDefaultNetWork()
    }

    private fun setUiDefaultNetWork() {
        val itemNetwork = viewModel.getItemNetworkDefault()
        itemNetwork?.let {
            binding.txtDot.setBackgroundColor(it.color)
            binding.txtNet.text = it.name
        }
    }

    private fun updateUI(symbol: String, decimal: String) {
        binding.edtTokenSymbol.setText(symbol)
        binding.edtTokenDecimal.setText(decimal)
    }

    override fun initEvent() {
        viewModel.tokenLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                when (data.responseType) {
                    Status.LOADING -> {
                        showLoadingDialog()
                        binding.btnImportToken.isEnabled = false
                    }
                    Status.SUCCESSFUL -> {
                        hideDialog()
                        binding.btnImportToken.isEnabled = true
                        data.data?.let { map ->
                            setNavigationResult("network_change", 0)
                            contractAddressTemp = map["contractAddress"] ?: ""
                            if ((map["button_click"] ?: "") == "true") // button click = true
                                importTokenSuccess()
                            else
                                updateUI(map["symbol"] ?: "", map["decimals"] ?: "")
                        }
                    }
                    Status.ERROR -> {
                        hideDialog()
                        binding.btnImportToken.isEnabled = false
                        data.error?.message?.let { key ->
                            if (key == "token_address_existed")
                                showToast("Địa chỉ token đã tồn tại!")
                            else
                                showToast("Vui lòng nhập đúng địa chỉ token!")
                        }
                    }
                }
            }
        }
    }

    private fun checkAndCall(buttonClick: Boolean) {
        val contractAddress = binding.edtTokenAddress.text.toString().trim()
        if (contractAddress.isBlank())
            return
        if (WalletUtils.isValidAddress(contractAddress))
            if (contractAddressTemp != contractAddress)
                viewModel.loadInforToken(contractAddress, buttonClick)
            else
                importTokenSuccess()
        else {
            showToast("Vui lòng nhập đúng địa chỉ token!")
        }

    }

    private fun importTokenSuccess() {
        showToast("Thêm token thành công!")
        viewModel.addContractAddressToPref(contractAddressTemp)
        backToPrevious()
    }

    override fun initConfig() {

    }


}
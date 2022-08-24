package com.example.omniwalletapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylibrary.utils.identicon.Identicon
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentHomeBinding
import com.example.omniwalletapp.ui.AnyOrientationCaptureActivity
import com.example.omniwalletapp.ui.home.adapter.ItemToken
import com.example.omniwalletapp.ui.home.adapter.ItemTokenAdapter
import com.example.omniwalletapp.ui.home.network.NetDialogFragment
import com.example.omniwalletapp.util.Status
import com.example.omniwalletapp.util.formatAddressWallet
import com.example.omniwalletapp.util.getNavigationResult
import com.example.omniwalletapp.util.getStringAddressFromScan
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import org.web3j.crypto.WalletUtils
import timber.log.Timber
import java.math.BigDecimal


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by activityViewModels()

    private val args: HomeFragmentArgs by navArgs()

    private var address: String? = null

    private val callBackToken: (Int, ItemToken) -> Unit = { i, data ->
        navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailTokenFragment(i)
        )
    }
    private val callBackImportToken: () -> Unit = {
        navigate(
            HomeFragmentDirections.actionHomeFragmentToAddTokenFragment()
        )
    }

    private val tokenAdapter: ItemTokenAdapter by lazy {
        ItemTokenAdapter(
            mutableListOf(),
            callBackToken,
            callBackImportToken
        )
    }

    // Register the launcher and result handler
    private val qrcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            showToast("Cancelled")
        } else {
            Log.d("XXX", ": ${result.contents}")
            val addressFormat = result.contents.getStringAddressFromScan()
            Log.d("XXX", "format: $addressFormat")
            if (WalletUtils.isValidAddress(addressFormat))
                navigate(
                    HomeFragmentDirections.actionHomeFragmentToSendTokenFragment(0, addressFormat)
                )
            else
                showToast("Not Address")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        address = args.address ?: preferencesRepository.getAddress()
        address?.let {
            viewModel.loadCredentials(it)
            viewModel.loadBalance(it)
        }

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initControl() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.txtAddress.setOnClickListener {
            address?.let {
                copyToClipboard(it)
                showToast(getString(R.string.toast_address_copied))
            }
        }

        // fade click anim
/*        binding.txtAddress.setOnTouchListener { v, action ->
            when (action.action) {
                MotionEvent.ACTION_UP -> {
                    binding.txtAddress.alpha = 1f
                    false
                }
                MotionEvent.ACTION_DOWN -> {
                    binding.txtAddress.alpha = 0.5f
                    false
                }
                else -> false
            }
        }*/

        binding.viewClickNetWork.setOnClickListener {
            NetDialogFragment.newInstance(
                fManager,
                viewModel.lstItemNetwork,
                chooseNetworkListener = {
                    if(it==-1)
                        return@newInstance
                    viewModel.setDefaultNetworkInfo(it)
                    setUiDefaultNetWork()
                    viewModel.refresh()
                },
                addNetworkListener = {
                    navigate(
                        HomeFragmentDirections.actionHomeFragmentToAddNetworkFragment()
                    )
                }
            )
        }

        binding.viewReceive.setOnClickListener {
            navigate(
                HomeFragmentDirections.actionHomeFragmentToReceiveTokenDialogFragment(address ?: "")
            )
        }

        binding.viewSend.setOnClickListener {
            navigate(
                HomeFragmentDirections.actionHomeFragmentToSendTokenFragment(indexToken = 0)
            )
        }

        binding.viewSwap.setOnClickListener {
            showToast("Action Swap")
        }

        binding.imgScan.setOnClickListener {
            qrcodeLauncher.launch(ScanOptions().apply {
                captureActivity = AnyOrientationCaptureActivity::class.java
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                setPrompt("đang quét...")
                setBeepEnabled(false)
                setOrientationLocked(false)
            })
        }

        binding.txtLock.setOnClickListener {
            showAlertDialog(
                title = "",
                content = getString(R.string.lock_title),
                confirmButtonTitle = "CÓ",
                cancelButtonTitle = "KHÔNG",
                confirmCallback = {
                    preferencesRepository.clearDataAddressWallet()
                    (requireActivity() as HomeActivity).restart()
                },
                cancelCallback = {

                }
            )
        }
    }

    override fun initUI() {
        initAddressWallet()
        initBalanceWallet(viewModel.balanceETH)
        setUiDefaultNetWork()

        binding.rvToken.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tokenAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
        }

        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.blue500
            )
        )

    }

    private fun setUiDefaultNetWork() {
        val itemNetwork = viewModel.getItemNetworkDefault()
        itemNetwork?.let {
            binding.txtDot.setBackgroundColor(it.color)
            binding.txtNet.text = it.name
        }
    }

    private fun initAddressWallet() {
        address?.let { it ->
            Timber.d("Address home: $it")

            binding.txtAddress.text = it.formatAddressWallet()
            Identicon(binding.imgAvatarWallet, it)

        }
    }

    private fun initBalanceWallet(ethBalance: BigDecimal) {
        val balanceFormat = StringBuilder().append(ethBalance)
            .append(" ${viewModel.getSymbolNetworkDefault()}").toString()
        binding.txtAmount.text = balanceFormat
    }

    override fun initEvent() {
        viewModel.fetchLiveData.observe(viewLifecycleOwner) {
            when (it.responseType) {
                Status.ERROR -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                Status.LOADING -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }

                Status.SUCCESSFUL -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        viewModel.addressLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                when (data.responseType) {
                    Status.LOADING -> {
                        showLoadingDialog()
                    }
                    Status.SUCCESSFUL -> {
                        hideDialog()
//                        initAddressWallet()
                    }
                    Status.ERROR -> {
                        hideDialog()
                        data.error?.message?.let {
                            showToast(it)
                        }
                    }
                }
            }
        }

        viewModel.balanceLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                initBalanceWallet(data)
            }
        }

        viewModel.lstTokenLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { lstItemToken ->
                tokenAdapter.addAll(lstItemToken)
            }
        }
    }

    override fun initConfig() {
        getNavigationResult<Int>(
            R.id.homeFragment,
            "network_change"
        ) {
            tokenAdapter.clearAll()
            viewModel.refresh()
        }
    }

    override fun onDestroyView() {
        binding.rvToken.adapter = null
        super.onDestroyView()
    }

}
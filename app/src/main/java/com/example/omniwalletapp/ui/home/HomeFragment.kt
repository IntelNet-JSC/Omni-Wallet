package com.example.omniwalletapp.ui.home

import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentHomeBinding
import com.example.omniwalletapp.ui.AnyOrientationCaptureActivity
import com.example.omniwalletapp.ui.home.adapter.ItemToken
import com.example.omniwalletapp.ui.home.adapter.ItemTokenAdapter
import com.example.omniwalletapp.ui.home.network.NetDialogFragment
import com.example.omniwalletapp.ui.home.network.adapter.ItemNetwork
import com.example.omniwalletapp.util.Status
import com.example.omniwalletapp.util.formatAddressWallet
import com.example.omniwalletapp.util.getStringAddressFromScan
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    private val lstNet: List<ItemNetwork> by lazy {
        val random = Random()
        listOf(
            ItemNetwork(
                1, getString(R.string.main_net), Color.argb(
                    255, random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)
                ),
                true
            ),
            ItemNetwork(
                2, getString(R.string.ropsten_test_net), Color.argb(
                    255, random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)
                ),
                false
            ),
            ItemNetwork(
                3, getString(R.string.kovan_test_net), Color.argb(
                    255, random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)
                ),
                false
            ),
            ItemNetwork(
                4, getString(R.string.goerli_test_net), Color.argb(
                    255, random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)
                ),
                false
            )
        )
    }

    private val callBackToken: (ItemToken) -> Unit = {
        navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailTokenFragment()
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
            val address = result.contents.getStringAddressFromScan()
            Log.d("XXX", "format: $address")
            if (address.isNotEmpty())
                navigate(
                    HomeFragmentDirections.actionHomeFragmentToSendTokenFragment(address)
                )
            else
                showToast("Not Address")
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initControl() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed(
                {
                    binding.swipeRefreshLayout.isRefreshing = false
                }, 500
            )
        }

        binding.txtAddress.setOnClickListener {
            copyToClipboard(getString(R.string.address_demo))
            showToast(getString(R.string.toast_address_copied))
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
                lstNet,
                chooseNetworkListener = {
                    showToast(it.name)
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
                HomeFragmentDirections.actionHomeFragmentToReceiveTokenDialogFragment()
            )
        }

        binding.viewSend.setOnClickListener {
            navigate(
                HomeFragmentDirections.actionHomeFragmentToSendTokenFragment()
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
            showToast("Lock Wallet!")
            /*navigate(
                HomeFragmentDirections.actionHomeFragmentToLoginLaterFragment()
            )*/
//            viewModel.loadCredentials2("0x39fB0Ea8aAdc23683f2d237801e912f55536F5cF")
        }
    }

    override fun initUI() {
        initUiWallet()

        binding.txtDot.setBackgroundColor(lstNet[0].color)
        binding.txtNet.text = lstNet[0].name

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

    private fun initUiWallet() {
        viewModel.credentials?.let { credentials ->
            Timber.d("Address home: ${credentials.address}")
            binding.txtAddress.text = credentials.address.formatAddressWallet()
            binding.imgAvaterWallet.setAddress(credentials.address)
        }
    }

    override fun initEvent() {
        viewModel.addressLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                when (data.responseType) {
                    Status.LOADING -> {
                        showLoadingDialog()
                    }
                    Status.SUCCESSFUL -> {
                        hideDialog()
                        initUiWallet()
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
    }

    override fun initConfig() {

    }

    override fun onDestroyView() {
        binding.rvToken.adapter = null
        super.onDestroyView()
    }

}
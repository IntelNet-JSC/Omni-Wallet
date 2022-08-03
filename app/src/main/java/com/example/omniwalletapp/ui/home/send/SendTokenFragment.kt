package com.example.omniwalletapp.ui.home.send

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentSendTokenBinding
import com.example.omniwalletapp.ui.AnyOrientationCaptureActivity
import com.example.omniwalletapp.ui.home.HomeFragmentDirections
import com.example.omniwalletapp.ui.home.send.adapter.AddressRecentlyAdapter
import com.example.omniwalletapp.ui.home.send.adapter.ItemAddress
import com.example.omniwalletapp.util.formatAddressWallet
import com.example.omniwalletapp.util.getStringAddressFromScan
import com.google.android.material.textfield.TextInputLayout
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SendTokenFragment : BaseFragment<FragmentSendTokenBinding, SendTokenViewModel>(),
    View.OnClickListener {

    override val viewModel: SendTokenViewModel by viewModels()

    private val args: SendTokenFragmentArgs by navArgs()

    private var address: String? = null

    private val adapter: AddressRecentlyAdapter by lazy {
        AddressRecentlyAdapter(
            callBackItemClick = {
                showToast("Item Address Click")
            }
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
            if(address.isNotEmpty()) {
                this.address=address
                initFillFromAddress()
            }
            else
                showToast("Not Address")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        address = args.address
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSendTokenBinding.inflate(inflater, container, false)

    override fun initControl() {

        binding.imgBack.setOnClickListener {
            backToPrevious()
        }

        binding.btnContinue.setOnClickListener {
            navigate(
                SendTokenFragmentDirections.actionSendTokenFragmentToAmountFragment()
            )
        }

        binding.edtSearchAddress.addTextChangedListener { text: Editable? ->
            if (text == null)
                return@addTextChangedListener
            if (text.isEmpty()) {
                binding.tiySearchAddress.apply {
                    endIconMode = TextInputLayout.END_ICON_CUSTOM
                    endIconDrawable = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_qr_code_scanner_24
                    )
                    setEndIconOnClickListener(null)
                    setEndIconOnClickListener(this@SendTokenFragment)
                }
            } else
                binding.tiySearchAddress.apply {
                    endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                }
        }
    }

    override fun initUI() {
        initFillFromAddress()

        binding.tiySearchAddress.apply {
            endIconMode = TextInputLayout.END_ICON_CUSTOM
            endIconDrawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_qr_code_scanner_24
            )
            setEndIconOnClickListener(this@SendTokenFragment)
        }

        binding.rvRecently.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SendTokenFragment.adapter.also {
                it.addAll(ItemAddress.generateListAddressRecently())
            }
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
        }

        binding.imgDeleteFill.setOnClickListener {
            address=null
            initFillFromAddress()
        }
    }

    private fun initFillFromAddress(){
        address?.run {
            binding.layoutFill.isVisible = true
            binding.tiySearchAddress.visibility = View.INVISIBLE
            binding.txtAddressFill.text = this.formatAddressWallet(12)
        }?:kotlin.run {
            binding.layoutFill.isVisible = false
            binding.tiySearchAddress.visibility = View.VISIBLE
        }
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

    override fun onDestroyView() {
        binding.rvRecently.adapter = null
        super.onDestroyView()
    }

    override fun onClick(p0: View?) {
        qrcodeLauncher.launch(ScanOptions().apply {
            captureActivity = AnyOrientationCaptureActivity::class.java
            setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            setPrompt("đang quét...")
            setBeepEnabled(false)
            setOrientationLocked(false)
        })
    }

}
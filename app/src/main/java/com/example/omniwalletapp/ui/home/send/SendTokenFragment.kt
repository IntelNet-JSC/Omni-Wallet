package com.example.omniwalletapp.ui.home.send

import android.os.Bundle
import android.text.Editable
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
import com.example.omniwalletapp.ui.home.send.adapter.AddressRecentlyAdapter
import com.example.omniwalletapp.ui.home.send.adapter.ItemAddress
import com.example.omniwalletapp.util.formatAddressWallet
import com.google.android.material.textfield.TextInputLayout
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
        showToast("Action Scan")
    }

}
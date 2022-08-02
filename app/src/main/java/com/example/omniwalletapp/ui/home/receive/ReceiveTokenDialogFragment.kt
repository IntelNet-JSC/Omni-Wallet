package com.example.omniwalletapp.ui.home.receive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseBottomSheetFragment
import com.example.omniwalletapp.databinding.FragmentReceiveTokenBinding
import com.example.omniwalletapp.util.formatAddressWallet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReceiveTokenDialogFragment : BaseBottomSheetFragment<FragmentReceiveTokenBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentReceiveTokenBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val strAddressWallet = getString(R.string.address_demo)
        binding.txtAddress.text = strAddressWallet.formatAddressWallet()
    }
}
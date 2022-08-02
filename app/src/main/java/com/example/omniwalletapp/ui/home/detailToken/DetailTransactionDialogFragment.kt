package com.example.omniwalletapp.ui.home.detailToken

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseDialogFragment
import com.example.omniwalletapp.databinding.DialogTransactionDetailBinding
import com.example.omniwalletapp.ui.home.detailToken.adapter.ItemHistoryToken

class DetailTransactionDialogFragment(
    private var item: ItemHistoryToken,
    private var viewListener: () -> Unit = { }
) : BaseDialogFragment<DialogTransactionDetailBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DialogTransactionDetailBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme_transparent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnViewTestnet.setOnClickListener {
            viewListener.invoke()
            dismiss()
        }

        binding.imgClose.setOnClickListener {
            dismiss()
        }

    }

    companion object {
        fun newInstance(
            fm: FragmentManager,
            item: ItemHistoryToken,
            viewListener: () -> Unit = { }
        ) {
            val dialog = DetailTransactionDialogFragment(item, viewListener)
            dialog.show(fm, dialog.tag)
        }
    }
}
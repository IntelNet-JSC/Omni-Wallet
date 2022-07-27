package com.example.omniwalletapp.ui.addWallet.createWallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentMemorizePhraseBinding
import com.example.omniwalletapp.entity.WordItem
import com.example.omniwalletapp.ui.addWallet.createWallet.adapter.MemorizePhraseAdapter
import com.example.omniwalletapp.util.dpToPx
import com.example.omniwalletapp.view.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemorizePhraseFragment :
    BaseFragment<FragmentMemorizePhraseBinding, CreateWalletViewModel>() {

    override val viewModel: CreateWalletViewModel by viewModels()

    private val adapter = MemorizePhraseAdapter()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMemorizePhraseBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.btnContinue.setOnClickListener {
            navigate(
                MemorizePhraseFragmentDirections.actionMemorizePhraseFragmentToConfirmPhraseFragment()
            )
        }
    }

    override fun initUI() {
        binding.rvMemorizePhrase.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            if (itemDecorationCount == 0)
                addItemDecoration(GridSpacingItemDecoration(2, 16.dpToPx, true, 0))
            adapter = this@MemorizePhraseFragment.adapter.also {
                it.addAll(
                    WordItem.generateListWord()
                )
            }
        }
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

    override fun onDestroyView() {
        binding.rvMemorizePhrase.adapter=null
        super.onDestroyView()
    }

}
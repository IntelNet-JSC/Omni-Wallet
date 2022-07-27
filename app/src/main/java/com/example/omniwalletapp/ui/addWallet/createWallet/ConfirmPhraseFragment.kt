package com.example.omniwalletapp.ui.addWallet.createWallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentConfirmPhraseBinding
import com.example.omniwalletapp.entity.WordItem
import com.example.omniwalletapp.ui.addWallet.AddWalletActivity
import com.example.omniwalletapp.ui.addWallet.createWallet.adapter.ConfirmPhraseAdapter
import com.example.omniwalletapp.util.dpToPx
import com.example.omniwalletapp.view.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmPhraseFragment : BaseFragment<FragmentConfirmPhraseBinding, CreateWalletViewModel>() {

    override val viewModel: CreateWalletViewModel by viewModels()

    private val adapterPhrase = ConfirmPhraseAdapter()
    private val adapterBlank = ConfirmPhraseAdapter(isBlank = true)

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentConfirmPhraseBinding.inflate(inflater, container, false)

    override fun initControl() {

    }

    override fun initUI() {
        binding.rvBlankPhrase.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            if (itemDecorationCount == 0)
                addItemDecoration(GridSpacingItemDecoration(2, 16.dpToPx, true, 0))
            adapter = adapterBlank.also {
                it.addAll(
                    WordItem.generateListBlank()
                )
            }
        }

        binding.rvPhase.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            if (itemDecorationCount == 0)
                addItemDecoration(GridSpacingItemDecoration(3, 8.dpToPx, false, 0))
            adapter = adapterPhrase.also {
                it.addAll(
                    WordItem.generateListWord()
                )
            }
        }

        binding.btnCompleteBackup.setOnClickListener {
            (requireActivity() as AddWalletActivity).initToolbar(false)
            navigate(
                ConfirmPhraseFragmentDirections.actionConfirmPhraseFragmentToLoginLaterFragment()
            )
        }
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

    override fun onDestroyView() {
        binding.rvBlankPhrase.adapter=null
        binding.rvPhase.adapter=null
        super.onDestroyView()
    }

}
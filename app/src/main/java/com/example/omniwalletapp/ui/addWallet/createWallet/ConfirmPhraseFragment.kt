package com.example.omniwalletapp.ui.addWallet.createWallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.omniwalletapp.BuildConfig
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.base.EmptyViewModel
import com.example.omniwalletapp.databinding.FragmentConfirmPhraseBinding
import com.example.omniwalletapp.entity.WordItem
import com.example.omniwalletapp.ui.addWallet.AddWalletActivity
import com.example.omniwalletapp.ui.addWallet.createWallet.adapter.ConfirmPhraseAdapter
import com.example.omniwalletapp.ui.addWallet.createWallet.adapter.RandomPhraseAdapter
import com.example.omniwalletapp.util.dpToPx
import com.example.omniwalletapp.view.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.regex.Pattern

@AndroidEntryPoint
class ConfirmPhraseFragment : BaseFragment<FragmentConfirmPhraseBinding, EmptyViewModel>() {

    private val args: ConfirmPhraseFragmentArgs by navArgs()

    override val viewModel: EmptyViewModel by viewModels()

    private val randomPhraseAdapter = RandomPhraseAdapter()
    private val confirmAdapter = ConfirmPhraseAdapter()

    private val lstSeedCode: List<String> by lazy {
        Pattern.compile(" ").split(args.seedCode).toList()
    }

    private val lstWordItem: List<WordItem> by lazy {
        Pattern.compile(" ").split(args.seedCode).map {
            WordItem(name = it)
        }
    }

    private val lstSeedCodeShuffle: List<String> by lazy {
        lstSeedCode.shuffled()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if((requireActivity() as AddWalletActivity).deleteDir(File(requireContext().filesDir, "")))
                {
                    backToPrevious()
                }
            }
        })
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentConfirmPhraseBinding.inflate(inflater, container, false)

    override fun initControl() {
        randomPhraseAdapter.callBackItemClick = {
            confirmAdapter.addWord(it)
        }
        confirmAdapter.callBackValidate = { pass, default ->
            binding.rvBlankPhrase.background = if(pass) {
                binding.btnCompleteBackup.isEnabled = true
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_rv_memorize_phrase_pass)
            }else{
                binding.btnCompleteBackup.isEnabled = false
                if(default)
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_rv_memorize_phrase)
                else
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_rv_memorize_phrase_error)
            }

        }
    }

    override fun initUI() {
        binding.rvBlankPhrase.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            if (itemDecorationCount == 0)
                addItemDecoration(GridSpacingItemDecoration(2, 16.dpToPx, true, 0))
            adapter = confirmAdapter.also {

                it.addAll(
                    lstWordItem
                )
            }
        }

        binding.rvPhase.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            if (itemDecorationCount == 0)
                addItemDecoration(GridSpacingItemDecoration(3, 8.dpToPx, false, 0))
            adapter = randomPhraseAdapter.also {
                it.addAll(
                    lstSeedCodeShuffle
                )
            }
        }

        binding.btnCompleteBackup.isEnabled = BuildConfig.DEBUG

        binding.btnCompleteBackup.setOnClickListener {
            (requireActivity() as AddWalletActivity).navigateHomeActivity()
        }
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

    override fun onDestroyView() {
        binding.rvBlankPhrase.adapter = null
        binding.rvPhase.adapter = null
        super.onDestroyView()
    }

}
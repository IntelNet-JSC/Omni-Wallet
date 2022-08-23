package com.example.omniwalletapp.ui.addWallet.createWallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.base.EmptyViewModel
import com.example.omniwalletapp.databinding.FragmentMemorizePhraseBinding
import com.example.omniwalletapp.ui.addWallet.createWallet.adapter.MemorizePhraseAdapter
import com.example.omniwalletapp.util.dpToPx
import com.example.omniwalletapp.view.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import org.web3j.crypto.MnemonicUtils
import java.security.SecureRandom
import java.util.regex.Pattern


@AndroidEntryPoint
class MemorizePhraseFragment :
    BaseFragment<FragmentMemorizePhraseBinding, EmptyViewModel>() {

    override val viewModel: EmptyViewModel by viewModels()

    private val args:MemorizePhraseFragmentArgs by navArgs()

    private val adapter = MemorizePhraseAdapter()

    private val lstWord: List<String> by lazy {
        Pattern.compile(" ").split(args.wordPhrase).toList()
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMemorizePhraseBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.btnContinue.setOnClickListener {
            navigate(
                MemorizePhraseFragmentDirections.actionMemorizePhraseFragmentToConfirmPhraseFragment(
                    args.wordPhrase
                )
            )
        }

        binding.txtCopyPhraseWord.setOnClickListener {
            copyToClipboard(args.wordPhrase)
            showToast(getString(R.string.toast_word_phrase_copied))
        }
    }

    override fun initUI() {
        binding.rvMemorizePhrase.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            if (itemDecorationCount == 0)
                addItemDecoration(GridSpacingItemDecoration(2, 16.dpToPx, true, 0))
            adapter = this@MemorizePhraseFragment.adapter.also {
                it.addAll(
                    lstWord
                )
            }
        }
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

    override fun onDestroyView() {
        binding.rvMemorizePhrase.adapter = null
        super.onDestroyView()
    }

}
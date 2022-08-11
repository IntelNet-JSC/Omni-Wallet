package com.example.omniwalletapp.ui.addWallet.importWallet

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentImportPhraseBinding
import com.example.omniwalletapp.ui.AnyOrientationCaptureActivity
import com.example.omniwalletapp.ui.addWallet.AddWalletActivity
import com.example.omniwalletapp.ui.addWallet.AddWalletViewModel
import com.example.omniwalletapp.util.Status
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.WalletUtils

@AndroidEntryPoint
class ImportPhraseFragment : BaseFragment<FragmentImportPhraseBinding, AddWalletViewModel>() {

    override val viewModel: AddWalletViewModel by viewModels()

    private val qrcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            showToast("Cancelled")
        } else {
            Log.d("XXX", "content: ${result.contents}")
            if (MnemonicUtils.validateMnemonic(result.contents))
                binding.edtPhrase.setText(result.contents)
            else
                showToast("Not Word Phrase")
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentImportPhraseBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.tiyPhrase.setEndIconOnClickListener {
            qrcodeLauncher.launch(ScanOptions().apply {
                captureActivity = AnyOrientationCaptureActivity::class.java
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                setPrompt("đang quét...")
                setBeepEnabled(false)
                setOrientationLocked(false)
            })
        }

        binding.btnAdd.setOnClickListener {
            /*val checkText = binding.txtTAndC.text.toString()
            if(checkText.contains("ue")){
                (requireActivity() as AddWalletActivity).navigateHomeActivity()
                return@setOnClickListener
            }*/
            val pass = binding.edtNewPass.text.toString().trim()
            val passConfirm = binding.edtConfirmPass.text.toString().trim()
            val wordPhrase = binding.edtPhrase.text.toString().trim()
            val remember = binding.swDefault.isChecked
            if (validateForm(wordPhrase, pass, passConfirm))
                viewModel.importWordPhrase(wordPhrase, passConfirm, remember)
        }
    }

    override fun initUI() {

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
                        data.data?.let { address ->
                            showToast(address)
//                            binding.txtTAndC.text = address
                            (requireActivity() as AddWalletActivity).navigateHomeActivity()
                        }
                    }
                    Status.ERROR -> {
                        hideDialog()
                    }
                }
            }
        }
    }

    override fun initConfig() {

    }

    private fun validateForm(wordPhrase:String, pass: String, passConfirm: String): Boolean {
        if(!MnemonicUtils.validateMnemonic(wordPhrase)){
            showToast("Vui lòng nhập đúng Cụm từ khôi phục bí mật!")
            return false
        }
        if(pass.isBlank() || passConfirm.isBlank()){
            showToast("Vui lòng nhập mật khẩu!")
            return false
        }
        if(pass.length<8){
            showToast("Mật khẩu phải ít nhất 8 ký tự!")
            return false
        }
        if (pass != passConfirm) {
            showToast("Xác nhận mật khẩu không đúng!")
            return false
        }

        return true
    }

}
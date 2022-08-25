package com.example.omniwalletapp.ui.addWallet.importWallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.BuildConfig
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentImportKeyBinding
import com.example.omniwalletapp.ui.AnyOrientationCaptureActivity
import com.example.omniwalletapp.ui.addWallet.AddWalletActivity
import com.example.omniwalletapp.ui.addWallet.AddWalletViewModel
import com.example.omniwalletapp.util.Status
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import org.web3j.crypto.WalletUtils
import timber.log.Timber

@AndroidEntryPoint
class ImportKeyFragment : BaseFragment<FragmentImportKeyBinding, AddWalletViewModel>() {

    override val viewModel: AddWalletViewModel by viewModels()

    private val qrcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            showToast("Cancelled")
        } else {
            Timber.d("content: ${result.contents}")
            if (WalletUtils.isValidPrivateKey(result.contents))
                binding.edtKeyPrivate.setText(result.contents)
            else
                showToast("Not Private Key")
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentImportKeyBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.tiyKeyPrivate.setEndIconOnClickListener {
            qrcodeLauncher.launch(ScanOptions().apply {
                captureActivity = AnyOrientationCaptureActivity::class.java
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                setPrompt("đang quét...")
                setBeepEnabled(false)
                setOrientationLocked(false)
            })
        }

        binding.btnAdd.setOnClickListener {
            val pass = binding.edtNewPass.text.toString().trim()
            val passConfirm = binding.edtConfirmPass.text.toString().trim()
            val privateKey = binding.edtKeyPrivate.text.toString().trim()
            val remember = binding.swDefault.isChecked
            if (validateForm(privateKey, pass, passConfirm))
                viewModel.importPrivateKey(privateKey, passConfirm, remember)
        }
    }

    override fun initUI() {
        if (BuildConfig.DEBUG) {
            binding.edtNewPass.setText(getString(R.string.password_demo))
            binding.edtConfirmPass.setText(getString(R.string.password_demo))
            binding.edtKeyPrivate.setText(getString(R.string.private_key_demo))
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
                        data.data?.let { address ->
                            Timber.d("Import private key, address: $address")
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

    private fun validateForm(prvKey: String, pass: String, passConfirm: String): Boolean {
        if (!WalletUtils.isValidPrivateKey(prvKey)) {
            showToast("Vui lòng nhập đúng private key!")
            return false
        }
        if (pass.isBlank() || passConfirm.isBlank()) {
            showToast("Vui lòng nhập mật khẩu!")
            return false
        }
        if (pass.length < 8) {
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
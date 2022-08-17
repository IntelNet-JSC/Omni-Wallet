package com.example.omniwalletapp.ui.addWallet.createWallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentCreatePassBinding
import com.example.omniwalletapp.ui.addWallet.AddWalletViewModel
import com.example.omniwalletapp.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePassFragment : BaseFragment<FragmentCreatePassBinding, AddWalletViewModel>() {

    override val viewModel: AddWalletViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCreatePassBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.btnCreatePass.setOnClickListener {
            val pass = binding.edtNewPass.text.toString().trim()
            val passConfirm = binding.edtConfirmPass.text.toString().trim()
            val remember = binding.swDefault.isChecked
            if (validateForm(pass, passConfirm))
                viewModel.createWallet(passConfirm, remember)
        }
    }

    override fun initUI() {

    }

    override fun initEvent() {
        viewModel.phraseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                when (data.responseType) {
                    Status.LOADING -> {
                        showLoadingDialog()
                    }
                    Status.SUCCESSFUL -> {
                        hideDialog()
                        data.data?.let { wordPhrase ->
                            showToast(wordPhrase)

                            navigate(
                                CreatePassFragmentDirections.actionCreatePassFragmentToMemorizePhraseFragment(wordPhrase)
                            )
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

    private fun validateForm(pass: String, passConfirm: String): Boolean {
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
package com.example.omniwalletapp.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentLoginLaterBinding
import com.example.omniwalletapp.repository.PreferencesRepository
import com.example.omniwalletapp.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginLaterFragment : BaseFragment<FragmentLoginLaterBinding, LoginLaterViewModel>() {

    override val viewModel: LoginLaterViewModel by viewModels()

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginLaterBinding.inflate(inflater, container, false)

    override fun onStart() {
        super.onStart()
        if (preferencesRepository.isRememberLogin())
            navigate(
                LoginLaterFragmentDirections.actionLoginLaterFragmentToHomeFragment()
            )
    }

    override fun initControl() {
        binding.btnUnlock.setOnClickListener {
            val pass = binding.edtPass.text.toString()
            if (validateForm(pass))
                viewModel.loadCredentials(pass)
        }

        binding.txtResetWallet.setOnClickListener {
            showToast("Reset Wallet")
        }
    }

    override fun initUI() {

    }

    override fun initEvent() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                when (data.responseType) {
                    Status.LOADING -> {
                        showLoadingDialog()
                    }
                    Status.SUCCESSFUL -> {
                        hideDialog()
                        data.data?.let { pass ->
                            showToast(pass.toString())
                            preferencesRepository.setRememberLogin(binding.swDefault.isChecked)
                            navigate(
                                LoginLaterFragmentDirections.actionLoginLaterFragmentToHomeFragment()
                            )
                        }
                    }
                    Status.ERROR -> {
                        hideDialog()
                        data.error?.message?.let {
                            showToast(it)
                        }
                    }
                }
            }
        }
    }

    override fun initConfig() {

    }

    private fun validateForm(pass: String): Boolean {
        if (pass.isBlank()) {
            showToast("Vui lòng nhập mật khẩu!")
            return false
        }
        if (pass.length < 8) {
            showToast("Mật khẩu phải ít nhất 8 ký tự!")
            return false
        }

        return true
    }

}
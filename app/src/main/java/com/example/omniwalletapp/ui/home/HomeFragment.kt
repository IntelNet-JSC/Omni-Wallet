package com.example.omniwalletapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initControl() {

    }

    override fun initUI() {

    }

    override fun initEvent() {
        /*viewModel.accountsLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { response ->
                when (response.responseType) {
                    Status.SUCCESSFUL -> {
                        response.data?.let { ethAccount ->
                            toast(ethAccount.accounts.size.toString())
                        }
                    }
                    Status.LOADING -> {
                        toast("show loading")
                    }
                    Status.ERROR -> {
                        toast("hide loading")
                    }
                }
            }
        }*/
    }

    override fun initConfig() {

    }

}
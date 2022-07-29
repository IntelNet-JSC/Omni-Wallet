package com.example.omniwalletapp.ui.home.token

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentAddTokenBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddTokenFragment : BaseFragment<FragmentAddTokenBinding, AddTokenViewModel>() {

    override val viewModel: AddTokenViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddTokenBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.imgBack.setOnClickListener {
            backToPrevious()
        }
    }

    override fun initUI() {

    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

}
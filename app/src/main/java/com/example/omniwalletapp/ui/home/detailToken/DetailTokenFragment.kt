package com.example.omniwalletapp.ui.home.detailToken

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentDetailTokenBinding
import com.example.omniwalletapp.ui.home.detailToken.adapter.ItemHistoryToken
import com.example.omniwalletapp.ui.home.detailToken.adapter.ItemHistoryTokenAdapter
import com.example.omniwalletapp.util.dpToPx
import com.example.omniwalletapp.view.SpacesItemVDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailTokenFragment : BaseFragment<FragmentDetailTokenBinding, DetailTokenViewModel>() {

    override val viewModel: DetailTokenViewModel by viewModels()

    private val callBackItemHistory: (ItemHistoryToken) -> Unit = {
        DetailTransactionDialogFragment.newInstance(fManager, it, viewListener = {
            showToast("XEM")
        })
    }
    private val callBackViewAll: () -> Unit = {
        showToast("Action View All")
    }

    private val itemHistoryAdapter: ItemHistoryTokenAdapter by lazy {
        ItemHistoryTokenAdapter(
            callBackItemHistory = callBackItemHistory,
            callBackViewAll = callBackViewAll
        )
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDetailTokenBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed(
                {
                    binding.swipeRefreshLayout.isRefreshing = false
                }, 500
            )
        }

        binding.viewClickNetWork.setOnClickListener {
            showToast("Action Network")
        }

        binding.viewReceive.setOnClickListener {
            showToast("Action Receive Token")
        }

        binding.viewSend.setOnClickListener {
            showToast("Action Send Token")
        }

        binding.viewSwap.setOnClickListener {
            showToast("Action Swap Token")
        }

        binding.imgBack.setOnClickListener {
            backToPrevious()
        }
    }

    override fun initUI() {
        binding.rvHistoryTransaction.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = itemHistoryAdapter.also {
                it.addAll(
                    ItemHistoryToken.generateHistoryTransaction()
                )
            }
            addItemDecoration(
                SpacesItemVDecoration(1.dpToPx)
            )
        }

        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.blue500))
    }

    override fun initEvent() {

    }

    override fun initConfig() {

    }

    override fun onDestroyView() {
        binding.rvHistoryTransaction.adapter = null
        super.onDestroyView()
    }

}
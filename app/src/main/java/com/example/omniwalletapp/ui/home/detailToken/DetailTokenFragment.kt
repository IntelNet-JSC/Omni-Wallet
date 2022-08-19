package com.example.omniwalletapp.ui.home.detailToken

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mylibrary.utils.identicon.Identicon
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentDetailTokenBinding
import com.example.omniwalletapp.ui.home.HomeViewModel
import com.example.omniwalletapp.ui.home.adapter.ItemToken
import com.example.omniwalletapp.ui.home.detailToken.adapter.ItemHistoryToken
import com.example.omniwalletapp.ui.home.detailToken.adapter.ItemHistoryTokenAdapter
import com.example.omniwalletapp.util.Status
import com.example.omniwalletapp.util.dpToPx
import com.example.omniwalletapp.view.SpacesItemVDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailTokenFragment : BaseFragment<FragmentDetailTokenBinding, HomeViewModel>() {

//    override val viewModel: DetailTokenViewModel by viewModels()
    override val viewModel: HomeViewModel by activityViewModels()

    private val args:DetailTokenFragmentArgs by navArgs()

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
            /*Handler().postDelayed(
                {
                    binding.swipeRefreshLayout.isRefreshing = false
                }, 500
            )*/
            viewModel.refresh()
        }

        binding.viewReceive.setOnClickListener {
            navigate(
                DetailTokenFragmentDirections.actionDetailTokenFragmentToReceiveTokenDialogFragment(viewModel.credentials!!.address)
            )
        }

        binding.viewSend.setOnClickListener {
            navigate(
                DetailTokenFragmentDirections.actionDetailTokenFragmentToSendTokenFragment(args.indexToken, null)
            )
        }

        binding.viewSwap.setOnClickListener {
            showToast("Action Swap Token")
        }

        binding.imgBack.setOnClickListener {
            backToPrevious()
        }
    }

    override fun initUI() {
        initDefaultNetwork()
        initBalanceUiFormat(viewModel.lstToken[args.indexToken])

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

    private fun initDefaultNetwork() {
        val itemNetwork = viewModel.getItemNetworkDefault()
        itemNetwork?.let {
            binding.txtDot.setBackgroundColor(it.color)
            binding.txtNet.text = it.name
        }
    }

    override fun initEvent() {
        viewModel.fetchLiveData.observe(viewLifecycleOwner) {
            when (it.responseType) {
                Status.ERROR -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                Status.LOADING -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }

                Status.SUCCESSFUL -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    initBalanceUiFormat(viewModel.lstToken[args.indexToken])
                }
            }
        }
    }

    private fun initBalanceUiFormat(item: ItemToken){
        val balanceFormat = StringBuilder().append(item.amount)
            .append(" ${item.symbol}").toString()
        binding.txtAmountToken.text = balanceFormat
        Identicon(binding.imgToken, item.address)
    }

    override fun initConfig() {
//        viewModel.refresh()
    }

    override fun onDestroyView() {
        binding.rvHistoryTransaction.adapter = null
        super.onDestroyView()
    }

}
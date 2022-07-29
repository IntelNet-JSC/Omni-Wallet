package com.example.omniwalletapp.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseFragment
import com.example.omniwalletapp.databinding.FragmentHomeBinding
import com.example.omniwalletapp.ui.home.adapter.ItemToken
import com.example.omniwalletapp.ui.home.adapter.ItemTokenAdapter
import com.example.omniwalletapp.ui.home.network.NetDialogFragment
import com.example.omniwalletapp.ui.home.network.adapter.ItemNetwork
import com.example.omniwalletapp.util.formatAddressWallet
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    private val lstNet: List<ItemNetwork> by lazy {
        val random = Random()
        listOf(
            ItemNetwork(
                1, getString(R.string.main_net), Color.argb(
                    255, random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)
                ),
                true
            ),
            ItemNetwork(
                2, getString(R.string.ropsten_test_net), Color.argb(
                    255, random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)
                ),
                false
            ),
            ItemNetwork(
                3, getString(R.string.kovan_test_net), Color.argb(
                    255, random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)
                ),
                false
            ),
            ItemNetwork(
                4, getString(R.string.goerli_test_net), Color.argb(
                    255, random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)
                ),
                false
            )
        )
    }

    private val callBackToken: (ItemToken) -> Unit = {
        showToast("Item Token Click")
    }
    private val callBackImportToken: () -> Unit = {
        navigate(
            HomeFragmentDirections.actionHomeFragmentToAddTokenFragment()
        )
    }

    private val tokenAdapter: ItemTokenAdapter by lazy {
        ItemTokenAdapter(
            mutableListOf(),
            callBackToken,
            callBackImportToken
        )
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initControl() {
        binding.viewClickNetWork.setOnClickListener {
            NetDialogFragment.newInstance(
                fManager,
                lstNet,
                chooseNetworkListener = {
                    showToast(it.name)
                },
                addNetworkListener = {
                    navigate(
                        HomeFragmentDirections.actionHomeFragmentToAddNetworkFragment()
                    )
                }
            )
        }
    }

    override fun initUI() {
        val strAddressWallet = getString(R.string.address_demo)
        binding.txtAddress.text = strAddressWallet.formatAddressWallet()
        binding.txtDot.setBackgroundColor(lstNet[0].color)
        binding.txtNet.text = lstNet[0].name

        binding.rvToken.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tokenAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
        }
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

    override fun onDestroyView() {
        binding.rvToken.adapter = null
        super.onDestroyView()
    }

}
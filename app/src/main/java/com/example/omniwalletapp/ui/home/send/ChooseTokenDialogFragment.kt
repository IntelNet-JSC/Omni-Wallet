package com.example.omniwalletapp.ui.home.send

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseBottomSheetFragment
import com.example.omniwalletapp.databinding.DialogChooseTokenBinding
import com.example.omniwalletapp.ui.home.adapter.ItemToken
import com.example.omniwalletapp.ui.home.adapter.ItemTokenAdapter

class ChooseTokenDialogFragment(
    private var tokenItems: List<ItemToken>,
    private var chooseTokenListener: (ItemToken) -> Unit = { }
) : BaseBottomSheetFragment<DialogChooseTokenBinding>() {

    private val adapter: ItemTokenAdapter by lazy {
        ItemTokenAdapter(
            callBackTokenClick = {
                chooseTokenListener.invoke(it)
                this.dismiss()
            }
        )
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DialogChooseTokenBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        adapter.addAll(netItems)
        initAdapter()

    }

    private fun initAdapter() {
        binding.rvChooseToken.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ChooseTokenDialogFragment.adapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    companion object {
        fun newInstance(
            fm: FragmentManager,
            items: List<ItemToken>,
            chooseTokenListener: (ItemToken) -> Unit = { }
        ) {
            val dialog = ChooseTokenDialogFragment(items, chooseTokenListener)
            dialog.show(fm, dialog.tag)
        }
    }
}
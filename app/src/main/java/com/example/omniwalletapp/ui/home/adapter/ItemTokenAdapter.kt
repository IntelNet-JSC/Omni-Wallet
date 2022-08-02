package com.example.omniwalletapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.databinding.ItemChooseTokenBinding
import com.example.omniwalletapp.databinding.ItemImportTokenBinding
import com.example.omniwalletapp.databinding.ItemTokenBinding

class ItemTokenAdapter(
    private val lstToken: MutableList<ItemToken> = mutableListOf(),
    private val callBackTokenClick: (ItemToken) -> Unit,
    private val callBackImportToken: (() -> Unit)? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addAll(listItem: List<ItemToken>) {
        lstToken.clear()
        lstToken.addAll(listItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_DATA ->
                ItemTokenViewHolder(
                    ItemTokenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            ITEM_FOOTER ->
                ItemFooterViewHolder(
                    ItemImportTokenBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            else ->
                ItemChooseViewHolder(
                    ItemChooseTokenBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_DATA -> (holder as ItemTokenViewHolder).bind(ItemToken(0, "BNB", "1000"))
            ITEM_FOOTER -> (holder as ItemFooterViewHolder).bind()
            else -> (holder as ItemChooseViewHolder).bind(ItemToken(0, "BNB", "1000"))
        }
    }

    override fun getItemCount(): Int {
        return if (callBackImportToken != null) 4 else 2
    }

    override fun getItemViewType(position: Int): Int {
//        return if (lstToken.size - 1 != position) ITEM_DATA else ITEM_FOOTER
        if (callBackImportToken == null)
            return ITEM_CHOOSE
        return if (3 != position) ITEM_DATA else ITEM_FOOTER
    }

    inner class ItemTokenViewHolder(val binding: ItemTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemToken) {
            itemView.setOnClickListener {
                callBackTokenClick.invoke(item)
            }
        }
    }

    inner class ItemFooterViewHolder(val binding: ItemImportTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            itemView.setOnClickListener {
                callBackImportToken?.invoke()
            }
        }
    }

    inner class ItemChooseViewHolder(val binding: ItemChooseTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemToken) {
            itemView.setOnClickListener {
                callBackTokenClick.invoke(item)
            }
        }
    }

    companion object {
        private const val ITEM_DATA = 0
        private const val ITEM_FOOTER = 1
        private const val ITEM_CHOOSE = 2
    }
}

data class ItemToken(
    val id: Int,
    var name: String,
    var amount: String
)

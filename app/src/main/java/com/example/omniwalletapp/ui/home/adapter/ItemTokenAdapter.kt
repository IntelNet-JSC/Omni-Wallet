package com.example.omniwalletapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.databinding.ItemImportTokenBinding
import com.example.omniwalletapp.databinding.ItemTokenBinding
import com.example.omniwalletapp.entity.WordItem

class ItemTokenAdapter(
    private val lstToken: MutableList<ItemToken> = mutableListOf(),
    private val callBackToken: (ItemToken) -> Unit,
    private val callBackImportToken: () -> Unit
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
            else ->
                ItemFooterViewHolder(
                    ItemImportTokenBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_DATA)
            (holder as ItemTokenViewHolder).bind()
        else
            (holder as ItemFooterViewHolder).bind()
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun getItemViewType(position: Int): Int {
//        return if (lstToken.size - 1 != position) ITEM_DATA else ITEM_FOOTER
        return if (3 != position) ITEM_DATA else ITEM_FOOTER
    }

    inner class ItemTokenViewHolder(val binding: ItemTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            itemView.setOnClickListener {
                Toast.makeText(binding.root.context, "Token Click", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ItemFooterViewHolder(val binding: ItemImportTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            itemView.setOnClickListener {
                callBackImportToken.invoke()
            }
        }
    }

    companion object {
        private const val ITEM_DATA = 0
        private const val ITEM_FOOTER = 1
    }
}

data class ItemToken(
    val id: Int,
    var name: String,
    var amount: String
)

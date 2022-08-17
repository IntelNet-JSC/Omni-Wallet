package com.example.omniwalletapp.ui.home.detailToken.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.R
import com.example.omniwalletapp.databinding.ItemHistoryTokenBinding
import com.example.omniwalletapp.databinding.ItemViewAllHistoryBinding

class ItemHistoryTokenAdapter(
    private val lstItems: MutableList<ItemHistoryToken> = mutableListOf(),
    private val callBackItemHistory: (ItemHistoryToken) -> Unit,
    private val callBackViewAll: (() -> Unit)? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addAll(listItem: List<ItemHistoryToken>) {
        lstItems.clear()
        lstItems.addAll(listItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_DATA ->
                ItemHistoryViewHolder(
                    ItemHistoryTokenBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            else ->
                ItemViewAllHolder(
                    ItemViewAllHistoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_DATA -> (holder as ItemHistoryViewHolder).bind(lstItems[position])
            else -> (holder as ItemViewAllHolder).bind()
        }
    }

    override fun getItemCount(): Int {
        return lstItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (lstItems.size - 1 != position) ITEM_DATA else ITEM_FOOTER
    }

    inner class ItemHistoryViewHolder(val binding: ItemHistoryTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemHistory: ItemHistoryToken) {
            binding.txtDateTime.text = "#${adapterPosition+1}-${binding.root.context.getString(R.string.format_date_demo)}"

            itemView.setOnClickListener {
                callBackItemHistory.invoke(itemHistory)
            }
        }
    }

    inner class ItemViewAllHolder(val binding: ItemViewAllHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            itemView.setOnClickListener {
                callBackViewAll?.invoke()
            }
        }
    }

    companion object {
        private const val ITEM_DATA = 0
        private const val ITEM_FOOTER = 1
    }
}

data class ItemHistoryToken(
    val id: Int = 0,
    var name: String = "BNB",
    var amount: String = "0.3",
    var formatDateTime: String = "Tháng 7 31 tại 10:22 pm",
    var action: String = "send",
    var status: String = "complete"
){
    companion object{
        fun generateHistoryTransaction()= listOf(
            ItemHistoryToken(),
            ItemHistoryToken(),
            ItemHistoryToken(),
            ItemHistoryToken()
        )
    }

}

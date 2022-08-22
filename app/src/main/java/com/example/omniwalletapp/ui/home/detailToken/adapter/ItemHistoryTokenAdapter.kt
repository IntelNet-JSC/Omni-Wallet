package com.example.omniwalletapp.ui.home.detailToken.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mylibrary.utils.identicon.Identicon
import com.example.omniwalletapp.R
import com.example.omniwalletapp.databinding.ItemHeaderTransactionBinding
import com.example.omniwalletapp.databinding.ItemHistoryTokenBinding
import com.example.omniwalletapp.databinding.ItemViewAllHistoryBinding
import com.example.omniwalletapp.ui.home.adapter.ItemToken
import com.example.omniwalletapp.util.BalanceUtil

class ItemHistoryTokenAdapter(
    private val lstItems: MutableList<ItemTransaction> = mutableListOf(),
    private val callBackItemHistory: (ItemTransaction) -> Unit,
    private val callBackViewAll: (() -> Unit)? = null,
    private val callBackReceive: (() -> Unit)? = null,
    private val callBackSend: (() -> Unit)? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addAll(listItem: List<ItemTransaction>) {
        lstItems.clear()
        lstItems.addAll(listItem)
        notifyDataSetChanged()
    }

    fun addHeader(item: ItemTransaction) {
        if(lstItems.isNotEmpty()){
            lstItems[0]=item
        }else{
            lstItems.add(item)
        }
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_HEADER ->
                ItemHeaderHolder(
                    ItemHeaderTransactionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            ITEM_DATA ->
                ItemHistoryViewHolder(
                    ItemHistoryTokenBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            else -> // ITEM_FOOTER
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
            ITEM_HEADER -> (holder as ItemHeaderHolder).bind(lstItems[position].itemToken)
            ITEM_DATA -> (holder as ItemHistoryViewHolder).bind(lstItems[position])
            else -> (holder as ItemViewAllHolder).bind() // ITEM_FOOTER
        }
    }

    override fun getItemCount(): Int {
        return lstItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return lstItems[position].type
    }

    inner class ItemHistoryViewHolder(val binding: ItemHistoryTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemTransaction) {
            binding.apply {
                val colorTint = if (item.status == CONFIRMED) R.color.blue500 else R.color.red1

                txtName.text = root.context.getString(R.string.sent_to, item.symbol)

                txtDateTime.text =
                    StringBuilder("#${item.nonce}").append(" - ").append(item.formatDateTime)

                txtAmount.text = BalanceUtil.formatBalanceWithSymbol(item.symbol, item.amount)

                txtStatus.text =
                    if (item.status == CONFIRMED) root.context.getString(R.string.confirm) else root.context.getString(
                        R.string.failed
                    )

                txtStatus.setTextColor(
                    ContextCompat.getColorStateList(
                        root.context,
                        if (item.status == CONFIRMED) R.color.green_1 else R.color.red1
                    )
                )

                imgAction.strokeColor = ContextCompat.getColorStateList(
                    root.context,
                    colorTint
                )

                imgAction.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        colorTint
                    )
                )
            }

            itemView.setOnClickListener {
                callBackItemHistory.invoke(item)
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

    inner class ItemHeaderHolder(val binding: ItemHeaderTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemToken?) {
            item?.let {
                val balanceFormat = StringBuilder().append(it.amount)
                    .append(" ${it.symbol}").toString()
                binding.txtAmountToken.text = balanceFormat
                Identicon(binding.imgToken, it.address)
            }
            binding.viewReceive.setOnClickListener {
                callBackReceive?.invoke()
            }

            binding.viewSend.setOnClickListener {
                callBackSend?.invoke()
            }
        }
    }

    companion object {
        const val ITEM_DATA = 0
        const val ITEM_FOOTER = 1
        const val ITEM_HEADER = 2

        const val CONFIRMED = 0
        const val FAILED = 1
    }
}

data class ItemTransaction(
    val hash: String = "",
    val nonce: String = "",
    val symbol: String = "",
    val symbolNative: String = "",
    val amount: String = "",
    val estimate: String = "",
    val from: String = "",
    val to: String = "",
    val formatDateTime: String = "",
    val action: String = "send",
    val status: Int = 0,
    val type: Int,

    val itemToken: ItemToken?=null
) {
    companion object {
        fun generateItemFooter() = ItemTransaction(
            type = ItemHistoryTokenAdapter.ITEM_FOOTER
        )

        fun generateItemHeader(item: ItemToken) = ItemTransaction(
            type = ItemHistoryTokenAdapter.ITEM_HEADER,
            itemToken = item
        )
    }

}

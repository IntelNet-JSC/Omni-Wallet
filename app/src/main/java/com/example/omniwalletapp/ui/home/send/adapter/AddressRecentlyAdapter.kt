package com.example.omniwalletapp.ui.home.send.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mylibrary.utils.identicon.Identicon
import com.example.omniwalletapp.databinding.ItemAddressRecentlyBinding
import com.example.omniwalletapp.util.formatAddressWallet

class AddressRecentlyAdapter(
    private val lstAddress: MutableList<ItemAddress> = mutableListOf(),
    private val callBackItemClick: (ItemAddress) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addAll(listItem: List<ItemAddress>) {
        lstAddress.clear()
        lstAddress.addAll(listItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemAddressViewHolder(
            ItemAddressRecentlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemAddressViewHolder).bind(lstAddress[position])
    }

    override fun getItemCount(): Int {
        return lstAddress.size
    }

    inner class ItemAddressViewHolder(val binding: ItemAddressRecentlyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemAddress) {
            binding.apply {
                txtAddress.text = item.name.formatAddressWallet()
                Identicon(imgAddress, item.name)
            }
            itemView.setOnClickListener {
                callBackItemClick.invoke(item)
            }
        }
    }

}

data class ItemAddress(
    val id: Int,
    var name: String
){
    companion object{
        fun generateListAddressRecently() = listOf(
            ItemAddress(0, "0x60Aef71878c7f0973DEb83C1b19Ca033DD271483"),
            ItemAddress(1, "0x7106c6e10f1d165320172d3089cfc6cbb27b03c4"),
            ItemAddress(2, "0x39fB0Ea8aAdc23683f2d237801e912f55536F5cF")
        )
    }
}

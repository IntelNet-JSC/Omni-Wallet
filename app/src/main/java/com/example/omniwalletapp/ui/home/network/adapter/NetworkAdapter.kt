package com.example.omniwalletapp.ui.home.network.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.R
import com.example.omniwalletapp.databinding.ItemNetBinding
import com.example.omniwalletapp.util.show

class NetworkAdapter(
    private val lstNet: MutableList<ItemNetwork> = mutableListOf(),
    private val callBackNetwork: (ItemNetwork) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addAll(listItem: List<ItemNetwork>) {
        lstNet.clear()
        lstNet.addAll(listItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemNetViewHolder(
            ItemNetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemNetViewHolder).bind(lstNet[position])
    }

    override fun getItemCount(): Int {
        return lstNet.size
    }

    inner class ItemNetViewHolder(val binding: ItemNetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemNetwork) {
            binding.apply {
                imgNetChecked.visibility = if(item.isChecked) View.VISIBLE else View.INVISIBLE
                txtNameNet.text=item.name
                imgDotNet.setBackgroundColor(item.color)
            }
            itemView.setOnClickListener {
                callBackNetwork.invoke(item)
            }
        }
    }

}

data class ItemNetwork(
    val id: Int,
    var name: String,
    var color: Int,
    var isChecked: Boolean
)

package com.example.omniwalletapp.ui.addWallet.createWallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.R
import com.example.omniwalletapp.databinding.ItemWordConfirmBinding

class ConfirmPhraseAdapter(
    private val lstWord: MutableList<String> = mutableListOf(), private val isBlank: Boolean = false
) :
    RecyclerView.Adapter<ConfirmPhraseAdapter.ConfirmPhraseViewHolder>() {

    var callBackItemClick: ((String) -> Unit)? = null

    fun addAll(listItem: List<String>) {
        lstWord.clear()
        lstWord.addAll(listItem)
        notifyDataSetChanged()
    }

    inner class ConfirmPhraseViewHolder(val binding: ItemWordConfirmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.txtWordNumber.isVisible = isBlank

            binding.txtNameWord.text = item
            binding.txtWordNumber.text = "${adapterPosition + 1}"

            if (isBlank) {
                binding.txtNameWord.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.bg_btn_wallet3)
            } else {
                binding.txtNameWord.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.bg_btn_wallet2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmPhraseViewHolder {
        return ConfirmPhraseViewHolder(
            ItemWordConfirmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ConfirmPhraseViewHolder, position: Int) {
        holder.bind(lstWord[position])
    }

    override fun getItemCount(): Int {
        return lstWord.size
    }
}
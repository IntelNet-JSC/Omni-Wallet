package com.example.omniwalletapp.ui.addWallet.createWallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omniwalletapp.databinding.ItemWordBinding
import com.example.omniwalletapp.entity.WordItem

class MemorizePhraseAdapter(
    private val lstWord: MutableList<WordItem> = mutableListOf()
) :
    RecyclerView.Adapter<MemorizePhraseAdapter.MemorizePhraseViewHolder>() {

    fun addAll(listItem: List<WordItem>) {
        lstWord.clear()
        lstWord.addAll(listItem)
        notifyDataSetChanged()
    }

    inner class MemorizePhraseViewHolder(val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WordItem) {
            binding.txtNameWord.text="${adapterPosition+1}. ${item.name}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemorizePhraseViewHolder {
        return MemorizePhraseViewHolder(
            ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MemorizePhraseViewHolder, position: Int) {
        holder.bind(lstWord[position])
    }

    override fun getItemCount(): Int {
        return lstWord.size
    }
}
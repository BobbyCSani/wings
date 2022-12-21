package com.store.wings.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.store.wings.R
import com.store.wings.model.TransactionModel
import com.store.wings.databinding.TransactionCardBinding
import com.store.wings.priceFormat

class TransactionAdapter(
    private val onQuantityChange: (TransactionModel, Int, List<TransactionModel>) -> Unit
): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private val list: MutableList<TransactionModel> = mutableListOf()

    inner class ViewHolder(private val binding: TransactionCardBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TransactionModel) = with(binding) {
            this.title.text = data.productName
            quantityInput.setText(data.quantity.toString())
            Glide.with(root.context).load(data.productImage).into(thumbnail)
            productPrice.text = root.context.getString(R.string.subtotal, data.subTotal.priceFormat)
            quantityInput.doAfterTextChanged {
                if (it.isNullOrEmpty().not()) {
                    val qty = quantityInput.text.toString().toInt()
                    val dataInList = list.find { item -> item.productCode == data.productCode }
                    dataInList?.apply {
                        this.quantity = qty
                        this.subTotal = productPrice.times(qty)
                        onQuantityChange(dataInList, qty, list)
                    }
                    productPrice.text = root.context.getString(
                        R.string.subtotal,
                        data.productPrice.times(qty).priceFormat
                    )
                }
            }
            unit.text = data.unit
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TransactionCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submitData(listData: List<TransactionModel>){
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }

}
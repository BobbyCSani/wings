package com.store.wings.product

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.store.wings.model.ProductModel
import com.store.wings.databinding.ProductCardBinding
import com.store.wings.discountPrice
import com.store.wings.priceFormat

class ProductAdapter(
    private val onProductClick: (ProductModel) -> Unit,
    private val onBuyClick: (ProductModel) -> Unit
): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val list: MutableList<ProductModel> = mutableListOf()

    inner class ViewHolder(private val binding: ProductCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: ProductModel) = with(binding){
            this.title.text = data.name
            Glide.with(root.context).load(data.image).into(thumbnail)
            productPrice.text = data.price.discountPrice(data.discount).priceFormat
            if(data.discount > 0) {
                strikePrice.visibility = View.VISIBLE
                strikePrice.text = data.price.priceFormat
                strikePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            else strikePrice.visibility = View.GONE
            itemView.setOnClickListener { onProductClick(data) }
            buy.setOnClickListener { onBuyClick(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submitData(listData: List<ProductModel>){
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }

}
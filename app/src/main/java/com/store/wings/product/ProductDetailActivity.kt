package com.store.wings.product

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.store.wings.model.ProductModel
import com.store.wings.databinding.ActivityProductDetailBinding
import com.store.wings.discountPrice
import com.store.wings.parcelable
import com.store.wings.priceFormat

class ProductDetailActivity: AppCompatActivity() {

    companion object{
        private const val PRODUCT_KEY = "PRODUCT_KEY"
        fun start(context: Context, data: ProductModel){
            context.startActivity(Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_KEY, data)
            })
        }
    }

    private lateinit var binding: ActivityProductDetailBinding
    private val product by lazy { intent?.parcelable<ProductModel>(PRODUCT_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() = with(binding){
        product?.let { data ->
            Glide.with(this@ProductDetailActivity).load(data.image).into(ivProduct)
            title.text = data.name
            productPrice.text = data.price.discountPrice(data.discount).priceFormat
            if(data.discount > 0) {
                strikePrice.visibility = View.VISIBLE
                strikePrice.text = data.price.priceFormat
                strikePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            else strikePrice.visibility = View.GONE
            dimension.text = data.dimension
            unit.text = data.unit
        }
    }
}
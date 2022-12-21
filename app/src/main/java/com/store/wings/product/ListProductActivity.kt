package com.store.wings.product

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.store.wings.model.ProductModel
import com.store.wings.model.TransactionModel
import com.store.wings.databinding.ActivityProductListBinding
import com.store.wings.discountPrice
import com.store.wings.transaction.ListTransactionActivity
import com.store.wings.username
import com.store.wings.utility.StaticValues
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date

@AndroidEntryPoint
class ListProductActivity: AppCompatActivity() {

    private val viewModel by viewModels<ProductViewModel>()
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        setupObserver()
        viewModel.getAllProduct()
    }

    private fun setupAdapter(){
        productAdapter = ProductAdapter(
            onProductClick = {
                ProductDetailActivity.start(this, it)
            },
            onBuyClick = { data ->
                username?.let { name -> viewModel.buyProduct(name, data) }
            }
        )
        binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(this@ListProductActivity)
            adapter = productAdapter
        }

        binding.checkout.setOnClickListener {
            startActivity(Intent(this, ListTransactionActivity::class.java))
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObserver(){
        viewModel.productLiveData.observe(this){ data ->
            productAdapter.submitData(data)
        }

        viewModel.transactionLiveData.observe(this){ isSuccess ->
            Toast.makeText(this, if (isSuccess) "Success add product to cart"
                else "Sorry, something wrong", Toast.LENGTH_LONG).show()
        }
    }
}
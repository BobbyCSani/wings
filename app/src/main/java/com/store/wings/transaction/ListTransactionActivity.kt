package com.store.wings.transaction

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.store.wings.*
import com.store.wings.R
import com.store.wings.databinding.ActivityListTransactionBinding
import com.store.wings.model.TransactionHeader
import com.store.wings.model.TransactionModel
import com.store.wings.utility.StaticValues
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ListTransactionActivity: AppCompatActivity() {

    private val viewModel by viewModels<TransactionViewModel>()
    private lateinit var binding: ActivityListTransactionBinding
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        setupObserver()
        getAllTransaction()
    }

    private fun getAllTransaction(){
        username?.let { name ->
            viewModel.getTransactions(name)
            viewModel.getTransactionHeader(name)
        }
    }

    private fun setupAdapter() {
        transactionAdapter = TransactionAdapter{ data, qty, listTransaction ->
            username?.let {
                viewModel.changeQty(it, data, qty)
                viewModel.setTransactionHeader(it, listTransaction)
            }
        }
        binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(this@ListTransactionActivity)
            adapter = transactionAdapter
        }
    }

    private fun setupObserver() = with(viewModel){
        cartLiveData.observe(this@ListTransactionActivity){ isSuccess ->
            if (isSuccess) Toast.makeText(this@ListTransactionActivity, "Success update cart", Toast.LENGTH_LONG).show()
        }

        transactionLiveData.observe(this@ListTransactionActivity){ data ->
            transactionAdapter.submitData(data)
        }

        transactionHeaderLiveData.observe(this@ListTransactionActivity){ price ->
            binding.totalPrice.text = getString(R.string.totalPrice, price)
        }
    }
}
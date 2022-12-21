package com.store.wings.repository

import com.store.wings.model.ProductModel
import com.store.wings.model.TransactionHeader
import com.store.wings.model.TransactionModel
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun buyProduct(username: String, product: ProductModel): Flow<Boolean>
    suspend fun getTransactions(username: String): Flow<List<TransactionModel>>
    suspend fun getTransactionHeader(username: String): Flow<String>
    suspend fun changeQty(username: String, data: TransactionModel, quantity: Int): Flow<Boolean>
    fun setTransactionHeader(name: String, data: List<TransactionModel>)
}
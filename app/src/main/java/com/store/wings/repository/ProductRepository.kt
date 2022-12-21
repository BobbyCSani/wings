package com.store.wings.repository

import com.store.wings.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getAllProduct(): Flow<List<ProductModel>>

}
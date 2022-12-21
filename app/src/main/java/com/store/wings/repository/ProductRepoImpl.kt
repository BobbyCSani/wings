package com.store.wings.repository

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.store.wings.model.ProductModel
import com.store.wings.utility.StaticValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepoImpl @Inject constructor(private val database: DatabaseReference): ProductRepository {

    override suspend fun getAllProduct(): Flow<List<ProductModel>> =
        callbackFlow {
            database.child(StaticValues.productTable).get().addOnSuccessListener { snapshot ->
                val data = snapshot.children.mapNotNull {
                    it.getValue(ProductModel::class.java)
                }
                trySend(data)
            }
            awaitClose {}
        }.flowOn(Dispatchers.IO)
}
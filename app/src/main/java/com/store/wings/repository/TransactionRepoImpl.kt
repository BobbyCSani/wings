package com.store.wings.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.store.wings.R
import com.store.wings.discountPrice
import com.store.wings.model.ProductModel
import com.store.wings.model.TransactionHeader
import com.store.wings.model.TransactionModel
import com.store.wings.priceFormat
import com.store.wings.username
import com.store.wings.utility.StaticValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class TransactionRepoImpl @Inject constructor(private val database: DatabaseReference): TransactionRepository {
    override suspend fun buyProduct(username: String, product: ProductModel): Flow<Boolean> {
        return callbackFlow {
            val listener = object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val snapshotItem = snapshot.child(snapshot.children.first().key ?: "")
                        val currentItem = snapshotItem.getValue(TransactionModel::class.java)
                        currentItem?.let { transactionModel ->
                            val qty = transactionModel.quantity.plus(1)
                            setTransaction(username, transactionModel, qty)
                            trySend(true)
                        }
                    }
                    else {
                        val data = TransactionModel(
                            code = "TRX",
                            number = Date().time,
                            productCode = product.code,
                            productImage = product.image,
                            productPrice = product.price.discountPrice(product.discount),
                            productName = product.name,
                            quantity = 1,
                            unit = "PCS",
                            subTotal = 0
                        )
                        setTransaction(username, data, 1)
                        trySend(true)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(false)
                }

            }
            database.child(StaticValues.transactionTable)
                .child(username)
                .orderByChild("productCode")
                .equalTo(product.code)
                .addListenerForSingleValueEvent(listener)
            awaitClose { database.removeEventListener(listener) }
        }.flowOn(Dispatchers.IO)
    }

    private fun setTransaction(name: String, data: TransactionModel, quantity: Int) {
        data.subTotal = data.productPrice.times(quantity)
        data.quantity = quantity
        database.child(StaticValues.transactionTable).child(name).child(data.code+data.number)
            .setValue(data)
    }

    override suspend fun getTransactions(username: String): Flow<List<TransactionModel>> {
        return callbackFlow {
            database.child(StaticValues.transactionTable).child(username).get()
                .addOnSuccessListener { snapshot ->
                    val result = snapshot.children.mapNotNull {
                        it.getValue(TransactionModel::class.java)
                    }
                    setTransactionHeader(username, result)
                    trySend(result)
                }
            awaitClose{}
        }.flowOn(Dispatchers.IO)
    }

    override fun setTransactionHeader(name: String, data: List<TransactionModel>) {
        var totalPrice: Long = 0
        data.forEach {
            totalPrice = totalPrice.plus(it.subTotal)
        }
        val result = TransactionHeader(
            code = "TRXHEADER",
            number = Date().time,
            username = name,
            total = totalPrice,
            unit = "PCS"
        )

        database.child(StaticValues.transactionHeaderTable).child(name)
            .setValue(
                result
            )
    }

    override suspend fun getTransactionHeader(username: String): Flow<String> {
        return callbackFlow {
            val listener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(TransactionHeader::class.java)
                    trySend(data?.total?.priceFormat ?: "Rp 0")
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            database.child(StaticValues.transactionHeaderTable)
                .child(username)
                .addValueEventListener(listener)

            awaitClose {}
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun changeQty(
        username: String,
        data: TransactionModel,
        quantity: Int
    ): Flow<Boolean> {
        return callbackFlow {
            val listener = object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val snapshotItem = snapshot.child(snapshot.children.first().key ?: "")
                        val currentItem = snapshotItem.getValue(TransactionModel::class.java)
                        currentItem?.let { transactionModel ->
                            setTransaction(username, transactionModel, quantity)
                            trySend(true)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(false)
                }

            }
            database.child(StaticValues.transactionTable).child(username)
                .orderByChild("productCode")
                .equalTo(data.productCode)
                .addListenerForSingleValueEvent(listener)
            awaitClose {
                database.removeEventListener(listener)
            }
        }.flowOn(Dispatchers.IO)
    }
}
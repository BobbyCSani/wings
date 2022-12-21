package com.store.wings.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TransactionModel(
    var code: String = "",
    var number: Long = 0,
    var productCode: String = "",
    var productImage: String = "",
    var productPrice: Long = 0,
    var productName: String = "",
    var quantity: Int = 0,
    var unit: String = "",
    var subTotal: Long = 0,
    var currency: String = "IDR"
)


@IgnoreExtraProperties
data class TransactionHeader(
    var code: String = "",
    var number: Long = 0,
    var username: String = "",
    var total: Long = 0,
    var unit: String = ""
)

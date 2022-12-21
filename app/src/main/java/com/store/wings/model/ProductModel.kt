package com.store.wings.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize


@IgnoreExtraProperties
@Parcelize
data class ProductModel(
    var code: String = "",
    var name: String = "",
    var price: Long = 0,
    var currency: String = "IDR",
    var discount: Int = 0,
    var dimension: String = "",
    var unit: String = "",
    val image :String = ""
): Parcelable
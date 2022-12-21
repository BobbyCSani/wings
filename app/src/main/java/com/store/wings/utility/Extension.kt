package com.store.wings

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.text.NumberFormat
import java.util.*

val Long.priceFormat: String
    get() {
        try {
            if(this <= 0) return "Rp 0"
            NumberFormat.getCurrencyInstance().apply {
                maximumFractionDigits = 0
                currency = Currency.getInstance("IDR")
                return when (Locale.getDefault().language){
                    "in" -> format(this@priceFormat).replace("Rp", "Rp ").removeSuffix(".00")
                    else -> format(this@priceFormat).replace("IDR", "Rp ").removeSuffix(".00")
                }
            }
        }catch (ex: Exception){
            return "Rp 0"
        }
    }

fun Long.discountPrice(discount: Int) = this.minus(this.div(100).times(discount))

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}
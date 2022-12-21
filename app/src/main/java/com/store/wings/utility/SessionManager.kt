package com.store.wings

import android.content.Context
import com.store.wings.utility.StaticValues

val Context.username: String?
    get() {
        val sharedPreferences = this.getSharedPreferences(StaticValues.CACHE, Context.MODE_PRIVATE)
        return sharedPreferences.getString(StaticValues.KEY_USERNAME, null) ?: return null
    }

fun Context.saveUsername(username: String){
    val sharedPreferences = this.getSharedPreferences(StaticValues.CACHE, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(StaticValues.KEY_USERNAME, username)
    editor.apply()
}
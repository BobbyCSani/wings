package com.store.wings.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.store.wings.product.ListProductActivity
import com.store.wings.databinding.ActivitySplashBinding
import com.store.wings.username

class SplashScreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySplashBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        if (username.isNullOrEmpty())
            startActivity(Intent(this, LoginActivity::class.java))
        else
            startActivity(Intent(this, ListProductActivity::class.java))
    }
}
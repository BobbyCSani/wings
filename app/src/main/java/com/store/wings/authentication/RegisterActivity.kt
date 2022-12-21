package com.store.wings.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.store.wings.product.ListProductActivity
import com.store.wings.model.User
import com.store.wings.databinding.ActivityMainBinding
import com.store.wings.saveUsername
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthenticationViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        setupObserver()
    }

    private fun setListener(){
        binding.register.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            viewModel.register(username, password)
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObserver(){
        viewModel.authLiveData.observe(this){ username ->
            if (username != null){
                saveUsername(username)
                startActivity(Intent(this, ListProductActivity::class.java))
                Toast.makeText(this, "congratulation, you are now registered", Toast.LENGTH_LONG)
                    .show()
                finishAfterTransition()
            } else Toast.makeText(this@RegisterActivity, "sorry, please use different username", Toast.LENGTH_LONG).show()
        }
    }
}
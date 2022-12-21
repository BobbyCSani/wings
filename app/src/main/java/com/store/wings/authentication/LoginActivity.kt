package com.store.wings.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.store.wings.product.ListProductActivity
import com.store.wings.model.User
import com.store.wings.databinding.ActivityLoginBinding
import com.store.wings.saveUsername
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthenticationViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        setupObserver()
    }

    private fun setListener(){
        binding.login.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            viewModel.login(username, password)
        }
        binding.register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupObserver(){
        viewModel.authLiveData.observe(this){ username ->
            if (username != null){
                saveUsername(username)
                startActivity(Intent(this, ListProductActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                })
            } else Toast.makeText(this@LoginActivity, "sorry, wrong username or password", Toast.LENGTH_LONG).show()
        }
    }
}
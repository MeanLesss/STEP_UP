package com.example.stepupandroid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.stepupandroid.databinding.ActivityHomeBinding
import com.example.stepupandroid.databinding.ActivityLoginBinding
import com.example.stepupandroid.viewmodel.LoginViewModel
import com.google.gson.Gson
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LoginViewModel(this)

        if(intent.hasExtra("from")){
            from = intent.getStringExtra("from").toString()
        }

        binding.loginBtn.setOnClickListener {
            val body = HashMap<String, String>()
            body["email"] = binding.email.text.toString()
            body["password"] = binding.password.text.toString()

            viewModel.login(body)
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }

        viewModel.loginResultState.observe(this){
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("from", from)
            startActivity(intent)
            finishAffinity()
        }

        viewModel.errorResultState.observe(this){
            Log.d("bug test", it.toString())
        }

    }
}
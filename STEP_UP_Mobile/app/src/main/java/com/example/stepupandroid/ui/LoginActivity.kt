package com.example.stepupandroid.ui

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LoginViewModel(this)


        binding.login.setOnClickListener {
            val body = HashMap<String, String>()
            body["email"] = binding.email.text.toString()
            body["password"] = binding.password.text.toString()

            viewModel.login(body)
        }

        viewModel.loginResultState.observe(this){
            Log.d("bug test", it.toString())
            binding.response.text = Gson().toJson(it)
        }

        viewModel.errorResultState.observe(this){
            Log.d("bug test", it.toString())
            binding.response.text = Gson().toJson(it)
        }

    }
}
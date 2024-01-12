package com.example.stepupandroid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stepupandroid.databinding.ActivityLoginBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.CustomDialog
import com.example.stepupandroid.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LoginViewModel(this)

        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from").toString()
        }

        binding.loginBtn.setOnClickListener {
            val body = HashMap<String, String>()
            body["email"] = binding.email.text.toString()
            body["password"] = binding.password.text.toString()

            viewModel.login(body)
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        viewModel.loginResultState.observe(this) {
            val intent = Intent(this, HomeActivity::class.java)
            if (from.isNotEmpty()) {
                intent.putExtra("from", from)
            }
            startActivity(intent)
            finishAffinity()
        }

        viewModel.errorResultState.observe(this) {
            CustomDialog("", it, Constants.Error).show(supportFragmentManager, "CustomDialog")
        }

    }

}
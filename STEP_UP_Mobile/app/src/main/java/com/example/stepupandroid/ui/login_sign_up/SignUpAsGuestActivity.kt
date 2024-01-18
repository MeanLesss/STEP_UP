package com.example.stepupandroid.ui.login_sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stepupandroid.databinding.ActivitySignUpAsGuestBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.model.param.SignUpAsGuestParam
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.viewmodel.SignUpViewModel

class SignUpAsGuestActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpAsGuestBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpAsGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val from = intent.getStringExtra("from").orEmpty()

        viewModel = SignUpViewModel(this)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.continueBtn.setOnClickListener {
            val body = SignUpAsGuestParam(
                guest = true,
                freelancer = false,
                name = binding.username.text.toString()
            )
            viewModel.signUpAsGuest(body)
        }

        viewModel.signUpResultState.observe(this) {
            val intent = Intent(this, HomeActivity::class.java)
            if (from.isNotEmpty()) {
                intent.putExtra("from", from)
            }
            startActivity(intent)
        }

        viewModel.errorResultState.observe(this) {
            CustomDialog("", it, Constants.Error).show(supportFragmentManager, "CustomDialog")
        }
    }
}
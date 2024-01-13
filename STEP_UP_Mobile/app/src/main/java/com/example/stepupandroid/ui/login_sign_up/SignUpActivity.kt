package com.example.stepupandroid.ui.login_sign_up

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var from = ""
    private var job = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        from = intent.getStringExtra("from").orEmpty()
        job = intent.getStringExtra("job").orEmpty()

        binding.job.text = job

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
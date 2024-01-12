package com.example.stepupandroid.ui.my_service

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stepupandroid.databinding.ActivityCreateServiceSuccessBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.HomeActivity

class CreateServiceSuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateServiceSuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateServiceSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.okayBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("from", Constants.MyService)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        return
    }
}
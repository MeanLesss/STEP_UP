package com.example.stepupandroid.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivityTopUpBalanceBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.ui.HomeActivity

class TopUpBalanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopUpBalanceBinding

    private var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopUpBalanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            if (isUpdate) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("from", Constants.Profile)
                startActivity(intent)
                finish()
            } else {
                finish()
            }
        }
    }
}
package com.example.stepupandroid.ui.service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivityTermAndAgreementBinding
import com.example.stepupandroid.ui.HomeActivity

class TermAndAgreementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermAndAgreementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermAndAgreementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceId = intent.getIntExtra("serviceId", 0)

        binding.nextBtn.isEnabled = false

        binding.checkBoxLayout.setOnClickListener {
            binding.checkBox.isChecked = !binding.checkBox.isChecked
        }

        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            binding.nextBtn.isEnabled = isChecked
        }

        binding.cancelBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.nextBtn.setOnClickListener {
            val intent = Intent(this, OrderServiceActivity::class.java)
            intent.putExtra("serviceId", serviceId)
            startActivity(intent)
        }

        val inputStream = resources.openRawResource(R.raw.agreement)
        val terms = inputStream.bufferedReader().use { it.readText() }
        binding.termAndAgreement.text = terms
    }
}
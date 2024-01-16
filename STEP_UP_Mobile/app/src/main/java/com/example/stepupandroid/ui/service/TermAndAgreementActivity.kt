package com.example.stepupandroid.ui.service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivityTermAndAgreementBinding

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

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.nextBtn.setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        }

        val inputStream = resources.openRawResource(R.raw.agreement)
        val terms = inputStream.bufferedReader().use { it.readText() }
        binding.termAndAgreement.text = terms
    }
}
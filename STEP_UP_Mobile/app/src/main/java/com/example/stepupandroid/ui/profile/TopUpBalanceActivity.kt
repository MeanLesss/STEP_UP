package com.example.stepupandroid.ui.profile

import android.content.Intent
import android.graphics.drawable.RotateDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.RotateAnimation
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.ActivityTopUpBalanceBinding
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.ui.HomeActivity
import com.example.stepupandroid.ui.dialog.CustomDialog
import com.example.stepupandroid.viewmodel.TopUpBalanceViewModel

class TopUpBalanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopUpBalanceBinding
    private lateinit var viewModel: TopUpBalanceViewModel

    private var isUpdate = false
    private var isUserInteraction = false

    private var amount = ""
    private val topUpOption by lazy {
        listOf(
            binding.option1,
            binding.option2,
            binding.option3,
            binding.option4,
            binding.option5,
            binding.option6
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopUpBalanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = TopUpBalanceViewModel(this)
        initViewModel()

        binding.topUpBtn.isEnabled = false

        binding.backBtn.setOnClickListener {
            if (isUpdate) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("from", Constants.Profile)
                startActivity(intent)
                finishAffinity()
            } else {
                finish()
            }
        }

        binding.topUpBtn.setOnClickListener {
            val body = HashMap<String, String>()
            body["balance"] = amount
            viewModel.topUp(body)
        }

        topUpOption.forEach { textView ->
            textView.setOnClickListener {
                updateSelection(it as TextView)
            }
        }

        binding.slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (isUserInteraction) {
                    clearSelection()
                }
                binding.topUpBtn.isEnabled = progress >= 5

                amount = progress.toString()
                binding.amount.text = Util.formatCurrency(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isUserInteraction = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isUserInteraction = false
            }
        })
    }

    private fun initViewModel(){
        viewModel.topUpResultState.observe(this) {
            isUpdate = true
            val customDialog = CustomDialog("", it, Constants.Success)
            customDialog.onDismissListener = {
                binding.backBtn.performClick()
            }

            customDialog.show(supportFragmentManager, "CustomDialog")
        }

        viewModel.errorResultState.observe(this) {
            val customDialog = CustomDialog("", it, Constants.Warning)

            customDialog.show(supportFragmentManager, "CustomDialog")
        }
    }
    private fun updateSelection(selectedOption: TextView) {
        val defaultDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.primary_color_border_drawable, null)
        val selectedDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)

        topUpOption.forEach { it.background = defaultDrawable }
        selectedOption.background = selectedDrawable

        isUserInteraction = false
        binding.slider.progress = selectedOption.text.toString().filter { it.isDigit() }.toInt()
    }

    private fun clearSelection(){
        val defaultDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.primary_color_border_drawable, null)
        topUpOption.forEach { it.background = defaultDrawable }
    }
}
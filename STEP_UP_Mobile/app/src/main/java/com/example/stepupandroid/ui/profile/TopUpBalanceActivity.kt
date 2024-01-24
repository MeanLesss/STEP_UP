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

class TopUpBalanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopUpBalanceBinding

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
                val rotationAngle = progress.toFloat() / binding.slider.max * 360
                binding.slider.thumb.mutate()  // Mutate the drawable to not share its state with any other drawable

                // Manually applying the rotation
                binding.slider.thumb.level = 10000  // Required for RotateDrawable
                (binding.slider.thumb as? RotateDrawable)?.apply {
                    bounds = binding.slider.thumb.bounds
                    this.drawable?.level = 10000
                    this.toDegrees = rotationAngle
                }

                val actualValue = progress + 5  // Adjusting the value
                amount = actualValue.toString()
                binding.amount.text = Util.formatCurrency(actualValue.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isUserInteraction = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isUserInteraction = false
            }
        })
    }

    private fun updateSelection(selectedOption: TextView) {
        val defaultDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.primary_color_border_drawable, null)
        val selectedDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.logo_color_border_drawable, null)

        topUpOption.forEach { it.background = defaultDrawable }
        selectedOption.background = selectedDrawable

        isUserInteraction = false
        binding.slider.progress = selectedOption.text.toString().filter { it.isDigit() }.toInt() - 5
    }

    private fun clearSelection(){
        val defaultDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.primary_color_border_drawable, null)
        topUpOption.forEach { it.background = defaultDrawable }
    }
}
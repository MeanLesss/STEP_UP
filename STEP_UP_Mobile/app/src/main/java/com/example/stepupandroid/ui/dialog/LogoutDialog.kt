package com.example.stepupandroid.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.stepupandroid.R
import com.example.stepupandroid.databinding.FragmentLogoutDialogBinding
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.SharedPreferenceUtil
import com.example.stepupandroid.ui.WelcomeActivity

class LogoutDialog : DialogFragment() {
    private lateinit var binding: FragmentLogoutDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutDialogBinding.inflate(layoutInflater)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutBtn.setOnClickListener {
            SharedPreferenceUtil().removeFromSp(ApiKey.SharedPreferenceKey.token)
            Constants.UserRole = 0
            val intent = Intent(requireActivity(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }

        binding.cancelBtn.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }
}
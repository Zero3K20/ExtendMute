package com.extendmute

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity
import com.extendmute.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenAccessibilitySettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        updateServiceStatus()
    }

    private fun updateServiceStatus() {
        val enabled = isAccessibilityServiceEnabled()
        if (enabled) {
            binding.tvStatus.setText(R.string.status_enabled)
            binding.tvStatus.setTextColor(getColor(android.R.color.holo_green_dark))
            binding.tvInstructions.setText(R.string.instructions_enabled)
        } else {
            binding.tvStatus.setText(R.string.status_disabled)
            binding.tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
            binding.tvInstructions.setText(R.string.instructions_disabled)
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val am = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        return am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .any {
                it.resolveInfo.serviceInfo.packageName == packageName &&
                    it.resolveInfo.serviceInfo.name == MuteToggleService::class.java.name
            }
    }
}

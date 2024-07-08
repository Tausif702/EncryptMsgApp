package com.tausif702.cryptomessenger

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tausif702.cryptomessenger.databinding.ActivityPrivacyandPolicyBinding

class PrivacyandPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyandPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPrivacyandPolicyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }
}
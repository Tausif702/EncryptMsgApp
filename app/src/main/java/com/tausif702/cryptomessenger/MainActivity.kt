package com.tausif702.cryptomessenger

import ViewPagerAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent

import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.tausif702.cryptomessenger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //binding view pager with tab layout and fragment
        val viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        val tabLayout = binding.tabLayout
        tabLayout.setupWithViewPager(viewPager)

        //binding btn privacy and setup
        binding.privacy.setOnClickListener {
            startActivity(Intent(this, PrivacyandPolicyActivity::class.java))
        }

    }

    //control back press
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
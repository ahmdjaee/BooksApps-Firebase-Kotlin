package com.example.imagepickerfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imagepickerfirebase.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToProfile.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }
        binding.btnToAddFile.setOnClickListener {
            startActivity(Intent(this, AddBooksActivity::class.java))
        }
    }
}
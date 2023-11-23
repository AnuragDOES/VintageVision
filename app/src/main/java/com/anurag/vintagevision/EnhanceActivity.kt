package com.anurag.vintagevision

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anurag.vintagevision.databinding.ActivityEnhanceBinding

class EnhanceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEnhanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnhanceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        
    }
}
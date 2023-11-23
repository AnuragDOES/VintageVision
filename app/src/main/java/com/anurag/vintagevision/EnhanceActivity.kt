package com.anurag.vintagevision

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anurag.vintagevision.databinding.ActivityEnhanceBinding
import com.anurag.vintagevision.ml.AutoModel1

class EnhanceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEnhanceBinding
    private lateinit var image : Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnhanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        image = getImage()
//        binding.imageView.setImageBitmap(image)
//
//
//
//        binding.btnEnhance.setOnClickListener() {
//            image = enhanceImage()
//            binding.imageView.setImageBitmap(image)
//        }

    }

//    private fun enhanceImage(): Bitmap {
//
//    }
//
//    private fun getImage(): Bitmap {
//        TODO("Extract image from here")
//    }
}
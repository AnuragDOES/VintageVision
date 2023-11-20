package com.anurag.vintagevision

import android.graphics.Bitmap
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.anurag.vintagevision.databinding.ActivityModelPreviewBinding

class ModelPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModelPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModelPreviewBinding.inflate(layoutInflater)



        val imageBitmap = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("imageBitmap", Bitmap::class.java)
        } else {
            intent.getParcelableExtra<Bitmap>("imageBitmap")
        }

//        if (imageBitmap != null) {
//            binding.tempImage.setImageBitmap(imageBitmap)
//        } else {
//            // Log an error and handle the case where imageBitmap is null
//            // This may indicate an issue in how it's being passed or received
//            Log.e("ModelPreviewActivity", "Received imageBitmap is null.")
//            Toast.makeText(this, "Error: Received image is null.", Toast.LENGTH_SHORT).show()
//        }
        setContentView(binding.root)
    }
}
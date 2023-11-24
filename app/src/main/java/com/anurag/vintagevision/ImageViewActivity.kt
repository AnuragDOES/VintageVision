package com.anurag.vintagevision
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anurag.vintagevision.databinding.ActivityImageViewBinding


class ImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uriString = intent.getStringExtra("outputPath")
        binding.ivDemo.setImageURI(Uri.parse(uriString))

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
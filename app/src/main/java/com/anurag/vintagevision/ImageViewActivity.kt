package com.anurag.vintagevision
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anurag.vintagevision.databinding.ActivityImageViewBinding


class ImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewBinding
    private lateinit var oldBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val oldPicUri: Uri? = null

//        val cameraUriString = intent.getStringExtra("cameraPic")
//        if (cameraUriString != null){
//            val oldPicUri = Uri.parse(cameraUriString)
//        }
        val cameraBytes = intent.getByteArrayExtra("cameraBytes")
        if(cameraBytes!=null) {val oldBitmap = BitmapFactory.decodeByteArray(cameraBytes, 0, cameraBytes.size)}

        binding.ivDemo.setImageBitmap(oldBitmap)


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
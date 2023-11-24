package com.anurag.vintagevision
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anurag.vintagevision.databinding.ActivityImageViewBinding


class ImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewBinding
    private var cameraUriString: String? = null
    private var galleryUriString: String? = null
    private var oldPicUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val oldPicUri: Uri? = null

        cameraUriString = intent.getStringExtra("cameraUri")
        galleryUriString = intent.getStringExtra("galleryUri")

        if (cameraUriString != null){
            oldPicUri = Uri.parse(cameraUriString)
        }
        if (galleryUriString != null){
            oldPicUri = Uri.parse(galleryUriString)
        }

        binding.ivDemo.setImageURI(oldPicUri)
        binding.btnHome.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        //        binding.btnEnhance.setOnClickListener() {
//            image = enhanceImage()
//            binding.imageView.setImageBitmap(image)
//        }

    }

    override fun onStop() {
        super.onStop()

        if(cameraUriString != null) {
            Toast.makeText(this,"pic deleted",Toast.LENGTH_LONG).show()
            val contentResolver: ContentResolver = applicationContext.contentResolver
            contentResolver.delete(Uri.parse(cameraUriString),null,null)
        }
        cameraUriString = null
        galleryUriString = null
    }

    //    private fun enhanceImage(): Bitmap {
//
//    }
//
//    private fun getImage(): Bitmap {
//        TODO("Extract image from here")
//    }




}
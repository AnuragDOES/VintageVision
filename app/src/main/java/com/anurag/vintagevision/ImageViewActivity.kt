package com.anurag.vintagevision
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.ImageBitmap
import com.anurag.vintagevision.databinding.ActivityImageViewBinding


class ImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewBinding
    private var imageBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var oldPicUri: Uri? = null

        val cameraUriString = intent.getStringExtra("cameraPic")
        if (cameraUriString != null){
            oldPicUri = Uri.parse(cameraUriString)
        }
        imageBitmap = getBitmapFromURI(this, oldPicUri)
        binding.ivDemo.setImageBitmap(imageBitmap)
        binding.btnEnhance.setOnClickListener {
            val newImage = processImage()
            if (newImage != null) {
                binding.ivDemo.setImageBitmap(newImage)
            }
        }


        //        binding.btnEnhance.setOnClickListener() {
//            image = enhanceImage()
//            binding.imageView.setImageBitmap(image)
//        }

    }

    private fun getBitmapFromURI(context: Context, oldPicUri: Uri?): Bitmap? {
        val contextResolver: ContentResolver = context.contentResolver
        try {
            return MediaStore.Images.Media.getBitmap(contentResolver, oldPicUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun processImage(): Bitmap? {
        //TODO("The model functionality will be here")
        var scaledImage : Bitmap? = null
        if (imageBitmap != null) {
            scaledImage = scaleBitmap()
        }
        return scaledImage
    }

    private fun scaleBitmap(): Bitmap? {
        val scaleWidthFactor : Float
        val scaleHeightFactor : Float
        val ratio = imageBitmap!!.height.toFloat() / imageBitmap!!.width.toFloat()
        if (imageBitmap != null) {
            scaleWidthFactor = (200).toFloat()
            scaleHeightFactor = scaleWidthFactor * ratio
        } else {
            return null
        }
        return Bitmap.createScaledBitmap(imageBitmap!!,
            (scaleWidthFactor).toInt(),
            (scaleHeightFactor).toInt(), true)
    }
    //    private fun enhanceImage(): Bitmap {
//
//    }
//
//    private fun getImage(): Bitmap {
//        TODO("Extract image from here")
//    }




}
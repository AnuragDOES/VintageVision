package com.anurag.vintagevision
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anurag.vintagevision.databinding.ActivityImageViewBinding


class ImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewBinding
    
    private var cameraUriString: String? = null
    private var galleryUriString: String? = null
    private var oldPicUri: Uri? = null
    private var imageBitmap: Bitmap? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
       

        cameraUriString = intent.getStringExtra("cameraUri")
        galleryUriString = intent.getStringExtra("galleryUri")

        if (cameraUriString != null){
            oldPicUri = Uri.parse(cameraUriString)
        }
        if (galleryUriString != null){
            oldPicUri = Uri.parse(galleryUriString)
        }
        imageBitmap = getBitmapFromURI(this, oldPicUri)
        binding.ivDemo.setImageBitmap(imageBitmap)
        binding.btnEnhance.setOnClickListener {
            val newImage = processImage()
            if (newImage != null) {
                binding.ivDemo.setImageBitmap(newImage)
            }
        }
        binding.btnHome.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
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
}
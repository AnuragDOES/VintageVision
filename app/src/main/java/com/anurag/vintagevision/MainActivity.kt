package com.anurag.vintagevision

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.anurag.vintagevision.databinding.ActivityMainBinding
import android.Manifest
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var imageBitmap : Bitmap
    private lateinit var binding : ActivityMainBinding
    val photoFile : File? = null
    //val photoFile : File = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VintageVision/image.jpg")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val cameraIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
            if(result.resultCode == RESULT_OK){
                //TODO("This is to be implemented in a different way")
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    val photoFile = try {
                        createImageFile()
                    } finally {
                        Toast.makeText(this, "done", Toast.LENGTH_LONG).show()
                    }
                }
                Toast.makeText(this, "The capture was successful", Toast.LENGTH_LONG).show()
                //binding.ivTest.setImageBitmap(imageBitmap)
                //navigateToModelPreview()
            } else {
                Toast.makeText(this, "Maybe you forgot to click a picture?", Toast.LENGTH_LONG).show()
            }
        }
        val galleryIntent = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                try {
                    TODO("This is to be implemented in a different way")
                    navigateToModelPreview()
                } catch (e: IOException) {
                    Log.e("MainActivity", "Error decoding bitmap from gallery: ${e.message}")
                    Toast.makeText(this, "Error decoding image from gallery", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No Image? 👉👈", Toast.LENGTH_SHORT).show()
            }
        }


        val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
        {isGranted:Boolean->
            if(isGranted){
                cameraIntent.launch(
                    Intent(
                        "android.media.action.IMAGE_CAPTURE"
                    )
                )
            }else{
                Toast.makeText(this,"Camera Permission Required.",Toast.LENGTH_LONG).show()
            }
        }
        val galleryPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
        {isGranted:Boolean->
            if(isGranted){
                galleryIntent.launch("image/*")
            }else{
                Toast.makeText(this,"Gallery Permission Required.",Toast.LENGTH_LONG).show()
            }
        }


        binding.btnCamera.setOnClickListener {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.btnGallery.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    }

    private fun navigateToModelPreview() {
        val intent = Intent(this, ModelPreviewActivity::class.java)
        intent.putExtra("imageBitmap", imageBitmap)
        startActivity(intent)
    }

    private fun createImageFile(): Any {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        return File.createTempFile(
            "VintageVision/JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }


}
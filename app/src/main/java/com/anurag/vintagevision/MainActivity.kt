package com.anurag.vintagevision

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.anurag.vintagevision.databinding.ActivityMainBinding
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCamera.setOnClickListener() {
            navigateToModelPreview()
        }
        val galleryIntent = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                try {
                    photoPicker(result)
                } catch (e: IOException) {
                    Log.e("MainActivity", "Error decoding bitmap from gallery: ${e.message}")
                    Toast.makeText(this, "Error decoding image from gallery", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No Image? 👉👈", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnGallery.setOnClickListener {
            galleryIntent.launch("image/*")
            }
        if (!allPermissionsGranted()) {
            requestPermission()
        }

    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            }
        }
    private fun photoPicker(uri: Uri?){
                val intent = Intent(this,ImageViewActivity::class.java)
                intent.putExtra("galleryUri",uri.toString())
                startActivity(intent)
        }




    private fun requestPermission() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }.toTypedArray()
    }

    private fun navigateToModelPreview(imageStuff: Bitmap? = null) {
        val intent = Intent(this, ModelPreviewActivity::class.java)
        if (imageStuff != null) {
            intent.putExtra("imageStuff", imageStuff)
        }
        startActivity(intent)
    }




}
package com.anurag.vintagevision

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.anurag.vintagevision.databinding.ActivityMainBinding
import android.Manifest
import android.graphics.ImageDecoder
import android.os.Build
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    private lateinit var imageBitmap : Bitmap
    private lateinit var binding : ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val cameraIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
            if(result.resultCode == RESULT_OK){
                val bundle : Bundle = result.data!!.extras!!
                imageBitmap = bundle.get("data") as Bitmap
                binding.ivTest.setImageBitmap(imageBitmap)
            }
        }
        val galleryIntent = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {result->
            val source = ImageDecoder.createSource(contentResolver, result!!)
            imageBitmap = ImageDecoder.decodeBitmap(source)
            binding.ivTest.setImageBitmap(imageBitmap)
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
}
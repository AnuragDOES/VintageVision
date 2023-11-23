package com.anurag.vintagevision

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.anurag.vintagevision.databinding.ActivityModelPreviewBinding
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ModelPreviewActivity : AppCompatActivity() {

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var binding: ActivityModelPreviewBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModelPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startCamera()
        binding.capturePhoto.setOnClickListener {
            takePhoto()


        }

        // TODO("Implement a double tap feature here")
        cameraExecutor = Executors.newSingleThreadExecutor()

    }
    private fun createBitmapFromByteBuffer(byteBuffer: ByteBuffer): Bitmap {
        val byteArray = ByteArray(byteBuffer.remaining())
        byteBuffer.get(byteArray)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return


        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    //imageProxy to byteBuffer
                    val planeBuffer = imageProxy.planes[0].buffer
                    val bytes = ByteArray(planeBuffer.remaining())
                    planeBuffer.get(bytes)
                    val byteBuffer = ByteBuffer.wrap(bytes)

                    imageProxy.close()

//                    intent to next activity

//                    intent.putExtra("imageData", byteBuffer.array())
                    Toast.makeText(baseContext,"yaha",Toast.LENGTH_LONG).show()
//                    navigateToIVactivity()
//                    cameraProvider.unbind(preview)
//                    val bitmap = createBitmapFromByteBuffer(byteBuffer)
//                    binding.ivDemo2.setImageBitmap(bitmap)

                }
            }
        )


    }

    private fun navigateToIVactivity() {
        val intent = Intent(this, ImageViewActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY).build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

//    companion object {
//        private const val TAG = "VintageVision"
//        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
//    }
}
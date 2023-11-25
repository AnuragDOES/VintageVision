package com.anurag.vintagevision
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.content.Intent
import android.graphics.Color.rgb
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anurag.vintagevision.databinding.ActivityImageViewBinding
import com.anurag.vintagevision.ml.AutoModel1
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.math.roundToInt


class ImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewBinding
    
    private var cameraUriString: String? = null
    private var galleryUriString: String? = null
    private var oldPicUri: Uri? = null
    private var imageBitmap: Bitmap? = null
    lateinit var imageProcessor: ImageProcessor
    
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
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(50, 50, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }

    private fun getBitmapFromURI(context: Context, oldPicUri: Uri?): Bitmap? {
        val contentResolver: ContentResolver = context.contentResolver
        try {
            @Suppress("DEPRECATION")
            return MediaStore.Images.Media.getBitmap(contentResolver, oldPicUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun processImage(): Bitmap? {
        //TODO("Model")

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(imageBitmap)
        tensorImage = imageProcessor.process(tensorImage)

        val model = AutoModel1.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 50, 50, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
        model.close()
        imageBitmap = floatArrayToBitmap(outputFeature0)
        Toast.makeText(this, outputFeature0.size.toString(), Toast.LENGTH_LONG).show()

        return imageBitmap

    }

    private fun floatArrayToBitmap(floatArray: FloatArray) : Bitmap {
        val width = 200
        val height = 200
        // Create empty bitmap in ARGB format
        val bmp: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(width * height * 4)

        // mapping smallest value to 0 and largest value to 255
        val maxValue = floatArray.max() //?: 1.0f
        val minValue = floatArray.min() //?: -1.0f
        val delta = maxValue-minValue

        // Define if float min..max will be mapped to 0..255 or 255..0
        val conversion = { v: Float -> ((v-minValue)/delta*255.0f).roundToInt()}

        // copy each value from float array to RGB channels
        for (i in 0 until width * height) {
            val r = conversion(floatArray[i])
            val g = conversion(floatArray[i+width*height])
            val b = conversion(floatArray[i+2*width*height])
            pixels[i] = rgb(r, g, b) // you might need to import for rgb()
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height)

        return bmp
    }

//    private fun scaleBitmap(): Bitmap? {
//        val scaleWidthFactor : Float
//        val scaleHeightFactor : Float
//        val ratio = imageBitmap!!.height.toFloat() / imageBitmap!!.width.toFloat()
//        if (imageBitmap != null) {
//            scaleWidthFactor = (50).toFloat()
//            scaleHeightFactor = (50).toFloat() //scaleWidthFactor * ratio
//        } else {
//            return null
//        }
//        return Bitmap.createScaledBitmap(imageBitmap!!,
//            (scaleWidthFactor).toInt(),
//            (scaleHeightFactor).toInt(), true)
//    }

    override fun onStop() {
        super.onStop()

        if(cameraUriString != null) {
            val contentResolver: ContentResolver = applicationContext.contentResolver
            contentResolver.delete(Uri.parse(cameraUriString),null,null)
            Toast.makeText(this,"pic deleted",Toast.LENGTH_LONG).show()
        }
        cameraUriString = null
        galleryUriString = null
    }
}
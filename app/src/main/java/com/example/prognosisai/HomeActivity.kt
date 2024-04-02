package com.example.prognosisai

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.prognosisai.databinding.ActivityHomeBinding
import com.example.prognosisai.ml.MobilenetV110224Quantprog
import com.example.prognosisai.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val rotateopen: Animation by lazy { AnimationUtils. loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateclose: Animation by lazy { AnimationUtils. loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils. loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils. loadAnimation(this, R.anim.to_bottom_anim) }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bitmap : Bitmap
    private var clicked = false
    private lateinit var imageUri: Uri

    @Inject
    lateinit var tokenManager : TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // setting navigation View null
        binding.navView.background=null
        binding.navView.menu.getItem(1).isEnabled = false


        //Controlling files fragment and account fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.NavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)


       // handling floating action button
        binding.fab.setOnClickListener {
            onAddButtonClicked()
        }

        //to upload pic in image view
        binding.fabUpload.setOnClickListener {
            binding.uploadPhoto.root.isVisible = true
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,100)
        }

        //to take pic using camera
        binding.fabCam.setOnClickListener {
            binding.uploadPhoto.root.isVisible = true
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                ){
                imageUri= createImageUri()!!
                imageUri?.let { uri ->
                    contract.launch(uri)
                }
            }
            else{
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }

        }

        val labels = application.assets.open("TypesOfSkinCancer.txt").bufferedReader().readLines()

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(28,28, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        binding.uploadPhoto.predButton.setOnClickListener {

            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)

            tensorImage= imageProcessor.process(tensorImage)

            val model = MobilenetV110224Quantprog.newInstance(this)

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 28, 28, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

            var maxIdx = 0
            outputFeature0.forEachIndexed { index, fl ->
                if(outputFeature0[maxIdx]<fl){
                    maxIdx=index
                }
            }
            tokenManager.saveToken(labels[maxIdx])
            // Releases model resources if no longer used.
            model.close()

            startActivity(Intent(this,PatientDetailsActivity::class.java))
        }
    }
    //CAMERA PERMISSION
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){granted ->
        if(granted){
            imageUri= createImageUri()!!
            imageUri?.let { uri ->
                contract.launch(uri)
            }
        }
        else{
            Toast.makeText(this, "Permission Denied!! Try Again!!",Toast.LENGTH_SHORT).show()
        }
    }

    //For Upload pic
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100){
            val uri = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
            binding.uploadPhoto.predimage.setImageBitmap(bitmap)
            binding.uploadPhoto.predimage.background=null
        }
    }
//FOR CAMERA
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        bitmap = uriToBitmap(imageUri)
        binding.uploadPhoto.predimage.setImageBitmap(bitmap)
        binding.uploadPhoto.predimage.rotation = 90f
        binding.uploadPhoto.predimage.background=null
    }
    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir,"camera_photo_prog.png")
        return FileProvider.getUriForFile(applicationContext,
            "com.example.prognosisai.fileProvider",
            image)
    }
    private fun uriToBitmap(uri: Uri): Bitmap {
        val contentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bitmaps = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        return bitmaps
    }





    private fun onAddButtonClicked() {
        setVisibilty(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked=!clicked
    }
    private fun setClickable(clicked: Boolean) {
        if(!clicked){
            binding.fabCam.isClickable=true
            binding.fabUpload.isClickable=true
        }
        else{
            binding.fabCam.isClickable=false
            binding.fabUpload.isClickable=false
        }
    }
    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            binding.fabCam.visibility = View.VISIBLE
            binding.fabUpload.visibility = View.VISIBLE
        }else{
            binding.fabCam.visibility = View.INVISIBLE
            binding.fabUpload.visibility = View.INVISIBLE
            binding.uploadPhoto.root.isVisible = false
        }
    }
    private fun setVisibilty(clicked: Boolean) {
        if(!clicked){
            binding.fabCam.startAnimation(fromBottom)
            binding.fabUpload.startAnimation(fromBottom)
            binding.fab.startAnimation(rotateopen)
        }
        else{
            binding.fabCam.startAnimation(toBottom)
            binding.fabUpload.startAnimation(toBottom)
            binding.fab.startAnimation(rotateclose)
        }
    }
}


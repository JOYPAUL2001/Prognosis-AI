package com.example.prognosisai

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.prognosisai.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val rotateopen: Animation by lazy { AnimationUtils. loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateclose: Animation by lazy { AnimationUtils. loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils. loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils. loadAnimation(this, R.anim.to_bottom_anim) }

    private lateinit var binding: ActivityHomeBinding
    private var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.navView.background=null
        binding.navView.menu.getItem(1).isEnabled = false


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.NavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)


        binding.fab.setOnClickListener {
            onAddButtonClicked()
        }
        binding.fabCam.setOnClickListener {
            Toast.makeText(this,"Camera Section", Toast.LENGTH_SHORT).show()
        }

        binding.fabUpload.setOnClickListener {
            Toast.makeText(this,"Gallery Section", Toast.LENGTH_SHORT).show()
        }
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
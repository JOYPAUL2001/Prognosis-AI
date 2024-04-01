package com.example.prognosisai

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prognosisai.databinding.ActivityPatientDetailsBinding
import com.example.prognosisai.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PatientDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientDetailsBinding

    @Inject
    lateinit var tokenManager : TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val value = "Prediction Result - "
        binding.predResult.text =tokenManager.getToken()
//        binding.predResult.setText(value + tokenManager.getToken())

    }
}
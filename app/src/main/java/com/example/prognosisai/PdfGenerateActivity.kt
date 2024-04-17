package com.example.prognosisai

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prognosisai.databinding.ActivityPdfGenerateBinding
import com.example.prognosisai.utils.TokenManager
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PdfGenerateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfGenerateBinding

    @Inject
    lateinit var providesRealTimeDatabaseInstance: DatabaseReference

    @Inject
    lateinit var tokenManager : TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        if(bundle!=null){
            binding.pdfName.text = bundle.getString("Patient Name")
            binding.pdfAge.text = bundle.getString("Age")
            binding.pdfDOB.text = bundle.getString("Date Of Birth")
            binding.pdfResult.text = bundle.getString("Prediction Type")
            binding.pdflocation.text = bundle.getString("Location")
            binding.pdfID.text = bundle.getString("ID")
            binding.pdfGender.text = bundle.getString("Gender")
        }
        else{
            Toast.makeText(this@PdfGenerateActivity,"Failed to fetch Details",Toast.LENGTH_SHORT).show()
        }

    }


}
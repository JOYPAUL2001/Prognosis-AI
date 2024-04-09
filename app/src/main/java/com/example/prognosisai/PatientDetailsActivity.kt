package com.example.prognosisai

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.prognosisai.ViewModel.AuthViewModel
import com.example.prognosisai.data.Patient
import com.example.prognosisai.databinding.ActivityPatientDetailsBinding
import com.example.prognosisai.utils.NetworkResource
import com.example.prognosisai.utils.TokenManager
import com.example.prognosisai.utils.inputValidationHelper
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class PatientDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientDetailsBinding

//    private val calendar = Calendar.getInstance()

    @Inject
    lateinit var calendar: Calendar

    @Inject
    lateinit var tokenManager : TokenManager

    @Inject
    lateinit var providesRealTimeDatabaseInstance: DatabaseReference

    private val authViewModel by viewModels<AuthViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.predResult.text =tokenManager.getToken()


        binding.savePtDetails.setOnClickListener {
            val validationResult = patientValidation()
            if(validationResult.first){
                val id = generateUniqueId()
                binding.uniqueId.root.isVisible = true
                binding.uniqueId.actualId.text = id
            }else{
                Toast.makeText(this@PatientDetailsActivity, validationResult.second, Toast.LENGTH_SHORT).show()
            }
        }

        binding.uniqueId.saveId.setOnClickListener {
            val userReq = setUserRequest()
            binding.uniqueId.root.isVisible = false
            lifecycleScope.launch {
                authViewModel.storePatientData(userReq)
                startActivity(Intent(this@PatientDetailsActivity, HomeActivity::class.java))
                finish()
            }
        }
        binding.datePicker.setOnClickListener {
            showDatePicker()
        }

        bindObserver()

    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(this,{DatePicker, year: Int,monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = calendar
            selectedDate.set(year, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate: String = dateFormat.format(selectedDate.time)
            binding.ptDOB.text= formattedDate
            val currYear = Calendar.getInstance().get(Calendar.YEAR)
            val age = currYear - year
            binding.ptAge.text = age.toString()
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
            )
        datePickerDialog.show()
    }

    fun generateUniqueId(): String {
        val timestamp = System.currentTimeMillis()
        val random = (0..10000).random() // Adjust range as per your requirement
        return "$timestamp$random"
    }



    private fun bindObserver() {
        authViewModel.storingPatientDetails.observe(this, Observer {
            when (it) {
                is NetworkResource.Success -> {
                    Log.d("Live Data Patient", "bindObserver: ${it.data}")
                    startActivity(Intent(this@PatientDetailsActivity, HomeActivity::class.java))
                }
                is NetworkResource.Error -> {
                    Log.d("Live Data Patient", "bindObserver: ${it.message}")
                    Toast.makeText(this.applicationContext, "" + it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setUserRequest() : Patient {
        val result = binding.predResult.text.toString()
        val Name = binding.ptName.text.toString()
        val DOB = binding.ptDOB.text.toString()
        val gender = binding.ptGender.text.toString()
        val age = binding.ptAge.text.toString()
        val city = binding.ptCity.text.toString()
        val state = binding.ptState.text.toString()
        val id = binding.uniqueId.actualId.text.toString()
        val uniqueId = providesRealTimeDatabaseInstance?.push()?.key
        return Patient(prediction = result, pName = Name, dob = DOB, gender = gender, age = age, city = city, state = state, ptId = id,ptUniqueId= uniqueId)
    }

    private fun patientValidation(): Pair<Boolean, String> {
        val userRequest = setUserRequest()
        return inputValidationHelper().patientDetailsValidation(userRequest.pName, userRequest.dob, userRequest.gender, userRequest.age, userRequest.city, userRequest.state)
    }
}
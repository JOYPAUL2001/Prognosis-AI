package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.prognosisai.R
import com.example.prognosisai.ViewModel.AuthViewModel
import com.example.prognosisai.data.Hospital
import com.example.prognosisai.databinding.FragmentRegistrationBinding

import com.example.prognosisai.di.AppModule
import com.example.prognosisai.utils.NetworkResource
import com.example.prognosisai.utils.TokenManager
import com.example.prognosisai.utils.inputValidationHelper
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null



    @Inject
    lateinit var providesRealTimeDatabaseInstance: DatabaseReference

    @Inject
    lateinit var tokenManager : TokenManager

    private val binding get() = _binding!!

    @Inject
    lateinit var calendar: Calendar

    private val authViewModel by viewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.SignUpCreateAcc.setOnClickListener {

           val validationResult = hospitalValidation()
           if(validationResult.first){
               val userReq = setUserRequest()
               lifecycleScope.launch {
                   authViewModel.emailVerificationUsingEmail()
                   authViewModel.storeHospitalData(userReq)
               }
           }else{
               Toast.makeText(this.context, validationResult.second, Toast.LENGTH_SHORT).show()
           }
       }
        bindObserver()
    }

    private fun bindObserver() {

        authViewModel.emailVerfResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResource.Success -> {
                    Toast.makeText(requireContext(),"Verify your Email",Toast.LENGTH_LONG).show()
//                    findNavController().navigate(R.id.action_registrationFragment_to_signInFragment)
                }
                is NetworkResource.Error -> {
                    Toast.makeText(this.context, "" + it.message, Toast.LENGTH_SHORT).show()
                }

            }
        })

        authViewModel.storingHospitalsDetails.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResource.Success -> {
                    Log.d("Live Data", "bindObserver: ${it.data}")
                    //Toast.makeText(requireContext(),"Verify your Email",Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        delay(600)
                        findNavController().navigate(R.id.action_registrationFragment_to_signInFragment)
                    }
                }
                is NetworkResource.Error -> {
                    Toast.makeText(this.context, "" + it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun setUserRequest() : Hospital{
        val Email = tokenManager.getEmail("Hospital_Email")
        val Name = binding.SignUpOrgname.text.toString()
        val Id = binding.SignUpOrgID.text.toString()
        val Address = binding.SignUpAdd.text.toString()
        val pincode = binding.SignUpPin.text.toString()
        val contactNumber = binding.SignUpCNumber.text.toString()
        val uniqueId = providesRealTimeDatabaseInstance?.push()?.key
        val Date = getDate()
        return Hospital(email = Email, name = Name, id = Id, address = Address, pinCode = pincode, contactNumber = contactNumber, uniqueId = uniqueId, date = Date)
    }

    private fun getDate(): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)

        val selectedDate = calendar
        selectedDate.set(year, month, date)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate: String = dateFormat.format(selectedDate.time)

        return formattedDate
    }

    private fun hospitalValidation(): Pair<Boolean, String> {
        val userRequest = setUserRequest()
        return inputValidationHelper().hospitalDetailsValidation(userRequest.name, userRequest.id, userRequest.address, userRequest.pinCode, userRequest.contactNumber)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
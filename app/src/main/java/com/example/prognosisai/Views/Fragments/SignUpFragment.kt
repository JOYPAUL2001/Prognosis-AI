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
import com.example.prognosisai.databinding.FragmentSignUpBinding
import com.example.prognosisai.utils.NetworkResource
import com.example.prognosisai.utils.TokenManager
import com.example.prognosisai.utils.inputValidationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding ? = null

    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding =  FragmentSignUpBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Changes to be done here
        binding.SignUpGetStart.setOnClickListener {

            val validationResult = userValidation()
            if (validationResult.first){
                val userReq = setUserRequest()
                lifecycleScope.launch {
                    authViewModel.signupUsingEmailAndPassword(userReq)
                }
            }
            else{
                Toast.makeText(this.context, validationResult.second, Toast.LENGTH_SHORT).show()
            }
        }

        binding.SignUpLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        bindObserver()
    }

    private fun setUserRequest() : Hospital{
        val Email = binding.SignUpEmail.text.toString()
        val password = binding.SignUpPass.text.toString()
        return Hospital(email = Email, password = password)
    }

    private fun userValidation(): Pair<Boolean, String> {
        val userRequest = setUserRequest()
        return inputValidationHelper().userInputValidation(userRequest.email, userRequest.password, false)
    }


    private fun bindObserver() {
        authViewModel.userSignupResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResource.Success -> {
                    findNavController().navigate(R.id.action_signUpFragment_to_registrationFragment)
                }

                is NetworkResource.Error -> {
                    Toast.makeText(this.context, "" + it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
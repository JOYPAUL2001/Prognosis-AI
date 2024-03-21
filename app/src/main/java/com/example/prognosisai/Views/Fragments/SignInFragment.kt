package com.example.prognosisai.Views.Fragments

import android.os.Bundle
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
import com.example.prognosisai.databinding.FragmentSignInBinding
import com.example.prognosisai.utils.NetworkResource
import com.example.prognosisai.utils.inputValidationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding ? = null

    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentSignInBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.LogInGetStart.setOnClickListener {
            val validationResult = userValidation()
            if (validationResult.first){
                val userReq = setUserRequest()
                lifecycleScope.launch {
                    val check = authViewModel.checkMailVerificationUsingEmail()
                    if(check){
                        authViewModel.signInusingEmailAndPassword(userReq)
                    }
                    else{
                        Toast.makeText(requireContext(),"Email is not verified! verify it first",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this.context, validationResult.second, Toast.LENGTH_SHORT).show()
            }
        }



        binding.LoginSignup.setOnClickListener{
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.loginForget.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgetPassFragment)
        }


        bindObserver()
    }

    private fun bindObserver() {

        authViewModel.userSignInResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResource.Success -> {
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                }

                is NetworkResource.Error -> {
                    Toast.makeText(this.context, "" + it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun setUserRequest() : Hospital {
        val Email = binding.LogInEmail.text.toString()
        val password = binding.LogInPass.text.toString()
        return Hospital(email = Email, password = password)
    }

    private fun userValidation(): Pair<Boolean, String> {
        val userRequest = setUserRequest()
        return inputValidationHelper().userInputValidation(userRequest.email!!, userRequest.password, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
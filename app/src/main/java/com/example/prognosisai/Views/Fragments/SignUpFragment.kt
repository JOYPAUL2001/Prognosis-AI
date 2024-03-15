package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.prognosisai.R
import com.example.prognosisai.ViewModel.AuthViewModel
import com.example.prognosisai.data.User
import com.example.prognosisai.databinding.FragmentSignUpBinding
import com.example.prognosisai.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            lifecycleScope.launch{
                authViewModel.signupUsingEmailAndPassword(User(email = "chiragpc6@gmail.com", password = "123chirag123"))
            }
            findNavController().navigate(R.id.action_signUpFragment_to_registrationFragment)
        }

        binding.SignUpLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }


    }

//    private fun setUserRequest() : User{
//        val Email = "chiragofficial04@gmail.com"
//        val password = "123chirag123"
//        return User(email = Email, password = password)
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
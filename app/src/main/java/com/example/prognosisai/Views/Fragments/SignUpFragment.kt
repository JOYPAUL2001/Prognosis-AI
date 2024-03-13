package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.prognosisai.R
import com.example.prognosisai.databinding.FragmentSignUpBinding
import com.example.prognosisai.databinding.FragmentStartBinding

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSignUpBinding.inflate(inflater, container, false)

        binding.SignUpGetStart.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_registrationFragment)
        }

        binding.SignUpLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        return binding.root
    }

}
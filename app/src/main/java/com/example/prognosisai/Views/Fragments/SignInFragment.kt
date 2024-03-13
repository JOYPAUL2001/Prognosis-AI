package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.prognosisai.R
import com.example.prognosisai.databinding.FragmentSignInBinding
import com.example.prognosisai.databinding.FragmentSignUpBinding

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSignInBinding.inflate(inflater, container, false)


       binding.LoginSignup.setOnClickListener{
           findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
       }

        return binding.root
    }

}
package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.prognosisai.R
import com.example.prognosisai.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentStartBinding.inflate(inflater, container, false)



        // Going to SignUp fragment
        binding.startSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_signUpFragment)
        }
        // Going to SignIn fragment
        binding.startSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_signInFragment)
        }



        return binding.root
    }

    


}
package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.prognosisai.R
import com.example.prognosisai.ViewModel.AuthViewModel
import com.example.prognosisai.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentStartBinding.inflate(inflater, container, false)






        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        lifecycleScope.launch {
//            val user = authViewModel.alreadyLogedInUsingEmail()
//            if(user){
//                findNavController().navigate(R.id.action_startFragment_to_homeActivity)
//            }
//        }


        // Going to SignUp fragment
        binding.startSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_signUpFragment)
        }
        // Going to SignIn fragment
        binding.startSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_signInFragment)
        }

    }

    


}
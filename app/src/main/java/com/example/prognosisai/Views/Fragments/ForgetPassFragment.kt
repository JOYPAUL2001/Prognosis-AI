package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.prognosisai.R
import com.example.prognosisai.databinding.FragmentForgetPassBinding
import com.example.prognosisai.databinding.FragmentSignInBinding


class ForgetPassFragment : Fragment() {

    private lateinit var binding: FragmentForgetPassBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentForgetPassBinding.inflate(inflater, container, false)

        binding.fgsubmit.setOnClickListener {
            findNavController().navigate(R.id.action_forgetPassFragment_to_signInFragment)
        }

        return binding.root
    }


}
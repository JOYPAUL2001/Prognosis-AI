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
import com.example.prognosisai.databinding.FragmentForgetPassBinding
import com.example.prognosisai.databinding.FragmentRegistrationBinding
import com.example.prognosisai.databinding.FragmentSignInBinding
import com.example.prognosisai.utils.NetworkResource
import com.example.prognosisai.utils.inputValidationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPassFragment : Fragment() {

    private var _binding: FragmentForgetPassBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentForgetPassBinding.inflate(inflater, container, false)

        binding.fgsubmit.setOnClickListener {
            findNavController().navigate(R.id.action_forgetPassFragment_to_signInFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fgsubmit.setOnClickListener {
            val validationResult = userValidation()
            if (validationResult.first){
                val userReq = setUserRequest()
                lifecycleScope.launch {
                    authViewModel.forgetPasswordUsingEmail(userReq)
                }
            }
            else{
                Toast.makeText(this.context, validationResult.second, Toast.LENGTH_SHORT).show()
            }
        }
        bindObserver()
    }

    private fun bindObserver() {

        authViewModel.forgetpassResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResource.Success -> {
                    Toast.makeText(requireContext(),"Check your Email",Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_forgetPassFragment_to_signInFragment)
                }
                is NetworkResource.Error -> {
                    Toast.makeText(this.context, "" + it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setUserRequest() : Hospital {
        val Email = binding.fgEmail.text.toString()
        return Hospital(email = Email)
    }

    private fun userValidation(): Pair<Boolean, String> {
        val userRequest = setUserRequest()
        return inputValidationHelper().forgetPassInputValidation(userRequest.email,  false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
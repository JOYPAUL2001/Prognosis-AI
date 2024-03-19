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
import com.example.prognosisai.databinding.FragmentRegistrationBinding
import com.example.prognosisai.databinding.FragmentSignInBinding
import com.example.prognosisai.utils.NetworkResource
import com.example.prognosisai.utils.TokenManager
import com.example.prognosisai.utils.inputValidationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null

    private val binding get() = _binding!!

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

               lifecycleScope.launch {
                   authViewModel.emailVerificationUsingEmail()
               }
       }
        bindObserver()

    }

    private fun bindObserver() {

        authViewModel.emailVerfResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResource.Success -> {
                    Toast.makeText(requireContext(),"Verify your Email",Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registrationFragment_to_signInFragment)
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
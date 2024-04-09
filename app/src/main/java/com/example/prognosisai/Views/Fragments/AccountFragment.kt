package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.prognosisai.R
import com.example.prognosisai.databinding.FragmentAccountBinding
import com.example.prognosisai.databinding.FragmentSignInBinding
import com.example.prognosisai.utils.TokenManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var providesRealTimeDatabaseInstance: DatabaseReference

    @Inject
    lateinit var tokenManager : TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentAccountBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = tokenManager.getId("Hospital_Id")

        if(user!!.isNotEmpty()){

            readData(user)
        }else{
            Toast.makeText(requireContext(),"No Hospital Details are there!",Toast.LENGTH_SHORT).show()
        }

    }

    private fun readData(user: String) {

        providesRealTimeDatabaseInstance = FirebaseDatabase.getInstance().getReference("Hospital Info")
        providesRealTimeDatabaseInstance.child(user).get().addOnSuccessListener {
            if(it.exists()){
                val hos_name = it.child("name").value
                val hos_id = it.child("id").value
                val hos_email = it.child("email").value
                val hos_reg = it.child("date").value
                val hos_ph = it.child("contactNumber").value
                Toast.makeText(requireContext(),"Fetching the data",Toast.LENGTH_SHORT).show()

                binding.accountEmail.text = hos_email.toString()
                binding.accountPhone.text = hos_ph.toString()
                binding.hospitalName.text = hos_name.toString()
                binding.hospitalId.text = hos_id.toString()
                binding.accountCreation.text = hos_reg.toString()

            }else{
                Toast.makeText(requireContext(),"No Hospital Details are there!",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
                Toast.makeText(requireContext(),"Unable to read the data!",Toast.LENGTH_SHORT).show()
        }
    }

}
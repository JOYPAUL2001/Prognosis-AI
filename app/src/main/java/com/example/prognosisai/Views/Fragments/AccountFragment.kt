package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.prognosisai.R
import com.example.prognosisai.databinding.FragmentAccountBinding
import com.example.prognosisai.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment(){

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var providesRealTimeDatabaseInstance: DatabaseReference

    @Inject
    lateinit var providesFirebaseAuthInstance: FirebaseAuth

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

        binding.accountpatients.text = tokenManager.getCount("Patient count")

        if(user!!.isNotEmpty()){
            readData(user)
        }else{
            Toast.makeText(requireContext(),"No Hospital Details are there!",Toast.LENGTH_SHORT).show()
        }

        binding.editDetails.setOnClickListener {
            visibility1()
        }

        binding.saveUpdate.setOnClickListener {
            val parameter1 = binding.accountEditEmail.text.toString()
            val parameter2 = binding.accountEditPhone.text.toString()
            updateData(parameter1,parameter2)
            visibility2()

        }

        binding.logOut.setOnClickListener {
            signOut()
        }

    }

    private fun signOut() {
        providesFirebaseAuthInstance.signOut()
        findNavController().navigate(R.id.action_AccountFragment_to_welcomeActivity)
    }

    private fun visibility2() {
        binding.accountEmail.isVisible = true
        binding.accountPhone.isVisible = true
        binding.accountEditEmail.isVisible = false
        binding.accountEditPhone.isVisible = false
        binding.saveUpdate.isVisible = false
    }

    private fun visibility1() {
        binding.accountEmail.isVisible = false
        binding.accountPhone.isVisible = false
        binding.accountEditEmail.isVisible = true
        binding.accountEditPhone.isVisible = true
        binding.saveUpdate.isVisible = true
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

                //Toast.makeText(requireContext(),"Fetching the data",Toast.LENGTH_SHORT).show()

                binding.accountEmail.text = hos_email.toString()
                binding.accountPhone.text = hos_ph.toString()
                binding.hospitalName.text = hos_name.toString()
                binding.hospitalId.text = hos_id.toString()
                binding.accountCreation.text = hos_reg.toString()
                binding.accountEditEmail.setText(hos_email.toString())
                binding.accountEditPhone.setText(hos_ph.toString())

            }else{
                Toast.makeText(requireContext(),"No Hospital Details are there!",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
                Toast.makeText(requireContext(),"Unable to read the data!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateData(email: String, phNumber: String){
        val hospitaldata = mapOf<String,String>("email" to email, "contactNumber" to phNumber)
        providesRealTimeDatabaseInstance.child(tokenManager.getId("Hospital_Id")!!).updateChildren(hospitaldata)
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"Updated",Toast.LENGTH_SHORT).show()
                refreshUI()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Failed to Update",Toast.LENGTH_SHORT).show()
            }
    }
    private fun refreshUI() {
        readData(tokenManager.getId("Hospital_Id")!!) // Read data again and update UI
    }


}
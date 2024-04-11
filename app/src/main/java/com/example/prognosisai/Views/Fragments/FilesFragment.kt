package com.example.prognosisai.Views.Fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prognosisai.Adapter.ListAdapter
import com.example.prognosisai.data.Patient
import com.example.prognosisai.databinding.FragmentFilesBinding
import com.example.prognosisai.utils.TokenManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilesFragment : Fragment() {


    private var _binding: FragmentFilesBinding? = null

    private val binding get() = _binding!!

    private lateinit var dialog: ProgressDialog

    private lateinit var ptList : ArrayList<Patient>

    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var databasereference: DatabaseReference

    @Inject
    lateinit var tokenManager : TokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFilesBinding.inflate(inflater, container, false)


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvDetails

        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)

        ptList = arrayListOf()

        databasereference.child(tokenManager.getId("Hospital_Id")!!).child("Patient").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(dataSnapshot in snapshot.children){
                        val pt = dataSnapshot.getValue(Patient::class.java)
                        if(!ptList.contains(pt)){
                            ptList.add(pt!!)
                        }
                    }
                    recyclerView.adapter = ListAdapter(ptList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"No Patient Details are there!",Toast.LENGTH_SHORT).show()
            }
        })


//      binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//          override fun onQueryTextSubmit(p0: String?): Boolean {
//              TODO("Not yet implemented")
//          }
//
//          override fun onQueryTextChange(p0: String?): Boolean {
//              TODO("Not yet implemented")
//          }
//
//      })


    }

}
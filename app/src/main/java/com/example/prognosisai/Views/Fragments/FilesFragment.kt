package com.example.prognosisai.Views.Fragments

import android.os.Bundle
import android.util.Log
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
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class FilesFragment : Fragment() {


    private var _binding: FragmentFilesBinding? = null

    private val binding get() = _binding!!

    private lateinit var useradapter : ListAdapter


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

        ptList = ArrayList()

        useradapter = ListAdapter(ptList,requireContext())






        databasereference.child(tokenManager.getId("Hospital_Id")!!).child("Patient")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val newList = ArrayList<Patient>()
                        for (dataSnapshot in snapshot.children) {
                            val pt = dataSnapshot.getValue(Patient::class.java)
                            newList.add(pt!!)
                        }
                        // Update adapter only after data retrieval
                        useradapter.updateList(newList)
                        recyclerView.adapter = useradapter
                        tokenManager.saveCount("Patient count", "${useradapter.itemCount}")
                    } else {
                        Toast.makeText(requireContext(),"Unable to fetch data!",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "No Patient Details are there!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


       // Log.d("Nesha", "onViewCreated: ${useradapter.itemCount}")

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchView.clearFocus()
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
               filterList(newText)
               return true
            }
        })
    }

    private fun filterList(query: String) {
        val filteredList = ArrayList<Patient>()
        if (query.isEmpty()) {
            // Reset to original list if search query is empty
            filteredList.addAll(ptList)
        } else {
            for (i in ptList) {
                if (i.ptId?.lowercase()?.contains(query.lowercase()) == true
                ) {
                    filteredList.add(i)
                }
            }
        }
            useradapter.setfilteredList(filteredList)
    }
}
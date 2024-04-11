package com.example.prognosisai.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prognosisai.data.Patient
import com.example.prognosisai.databinding.ActivityHomeBinding
import com.example.prognosisai.databinding.PatientNodeBinding

class ListAdapter(private val list: ArrayList<Patient>): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: PatientNodeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(node: Patient) {
            binding.listName.text = "Name: " + node.pname
            binding.listDate.text = node.dob
            binding.listGender.text = node.gender
            binding.listId.text = "ID: "+node.ptId
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
     val binding = PatientNodeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val node = list[position]
        holder.bind(node)
    }
}
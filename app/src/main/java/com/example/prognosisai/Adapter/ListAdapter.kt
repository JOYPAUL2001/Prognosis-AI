package com.example.prognosisai.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prognosisai.PdfGenerateActivity
import com.example.prognosisai.data.Patient
import com.example.prognosisai.databinding.PatientNodeBinding

class ListAdapter(private var list: ArrayList<Patient>, private val context: Context): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: PatientNodeBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(node: Patient) {
            binding.listName.text = "Name: " + node.pname
            binding.listDate.text = node.dob
            binding.listGender.text = node.gender
            binding.listId.text = "ID: "+node.ptId
        }

        fun cardBind(node: Patient){
            binding.recCard.setOnClickListener {
                val intent = Intent(context,PdfGenerateActivity::class.java)
                intent.putExtra("Patient Name",node.pname)
                intent.putExtra("Prediction Type",node.prediction)
                intent.putExtra("Date Of Birth",node.dob)
                intent.putExtra("Age",node.age)
                intent.putExtra("Gender",node.gender)
                intent.putExtra("ID",node.ptId)
                intent.putExtra("Location",node.city+", "+node.state)
                context.startActivity(intent)
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
     val binding = PatientNodeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        val binding2 = ActivityPdfGenerateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding,context)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val node = list[position]
        holder.bind(node)

        holder.cardBind(node)

    }

    fun setfilteredList(ptList: ArrayList<Patient>){
        list = ptList
        notifyDataSetChanged()
    }

    fun updateList(newList: ArrayList<Patient>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }


}
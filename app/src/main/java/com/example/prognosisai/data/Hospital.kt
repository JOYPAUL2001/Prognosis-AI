package com.example.prognosisai.data

data class Hospital(
    val email: String? = null,
    val password: String? = null,
    val id: String? = null,
    val name: String? = null,
    val address: String? = null,
    val pinCode: String? = null,
    val contactNumber: String? = null,
    val uniqueId: String? = null,
    val date: String? = null,
    val patient: MutableList<Patient>? = null
)

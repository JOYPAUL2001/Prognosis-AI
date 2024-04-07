package com.example.prognosisai.Repository

import com.example.prognosisai.data.Hospital
import com.example.prognosisai.data.Patient

interface AuthRepository {
    suspend fun signUpWithEmail(hospital : Hospital)
    suspend fun signInWithEmail(hospital: Hospital)
    suspend fun forgotPassword(hospital: Hospital)
    suspend fun emailVerification()

    suspend fun checkEmailVerification() : Boolean

    suspend fun alReadyLogedIn() : Boolean

    suspend fun storingHospitalDetailsRDB(hospital: Hospital)

    suspend fun storingPatientDetailsRDB(patient: Patient)
}
package com.example.prognosisai.Repository

import com.example.prognosisai.data.Hospital

interface AuthRepository {
    suspend fun signUpWithEmail(hospital : Hospital)
    suspend fun signInWithEmail(hospital: Hospital)
    suspend fun forgotPassword(hospital: Hospital)
    suspend fun emailVerification()

    suspend fun checkEmailVerification() : Boolean
}
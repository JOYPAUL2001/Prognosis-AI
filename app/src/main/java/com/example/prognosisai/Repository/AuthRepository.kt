package com.example.prognosisai.Repository

import com.example.prognosisai.data.User

interface AuthRepository {
    suspend fun signUpWithEmail(user : User)
    suspend fun signInWithEmail(user: User)
    suspend fun forgotPassword(user: User)
}
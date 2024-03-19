package com.example.prognosisai.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prognosisai.Repository.AuthRepositoryImpl
import com.example.prognosisai.data.Hospital
import com.example.prognosisai.utils.NetworkResource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepositoryImpl, private val firebaseAuth: FirebaseAuth) : ViewModel() {
    val userSignupResponseLiveData: LiveData<NetworkResource<String>>
        get() = repository.userSignupResponseLiveData

    val userSignInResponseLiveData: LiveData<NetworkResource<String>>
        get() = repository.userSignInResponseLiveData

    val forgetpassResponseLiveData: LiveData<NetworkResource<String>>
        get() = repository.forgetpassResponseLiveData

    val emailVerfResponseLiveData: LiveData<NetworkResource<String>>
        get() = repository.emailVerfResponseLiveData

    suspend fun signupUsingEmailAndPassword(hospital: Hospital) {
        viewModelScope.launch {
            repository.signUpWithEmail(hospital)
        }
    }
    suspend fun signInusingEmailAndPassword(hospital: Hospital){
        viewModelScope.launch {
            repository.signInWithEmail(hospital)
        }
    }

    suspend fun forgetPasswordUsingEmail(hospital: Hospital){
        viewModelScope.launch {
            repository.forgotPassword(hospital)
        }
    }

    suspend fun emailVerificationUsingEmail(){
        viewModelScope.launch {
            repository.emailVerification()
        }
    }

    suspend fun checkMailVerificationUsingEmail(): Boolean {
        firebaseAuth.currentUser!!.reload()
        val str = viewModelScope.async {
            repository.checkEmailVerification()
        }
        return str.await()
    }

}
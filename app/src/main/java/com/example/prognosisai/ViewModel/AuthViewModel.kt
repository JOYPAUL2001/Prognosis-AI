package com.example.prognosisai.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prognosisai.Repository.AuthRepositoryImpl
import com.example.prognosisai.data.User
import com.example.prognosisai.utils.NetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepositoryImpl) : ViewModel() {
    val userSignupResponseLiveData: LiveData<NetworkResource<String>>
        get() = repository.userSignupResponseLiveData

    suspend fun signupUsingEmailAndPassword(user: User) {
        viewModelScope.launch {
            repository.signUpWithEmail(user)
        }
    }
}
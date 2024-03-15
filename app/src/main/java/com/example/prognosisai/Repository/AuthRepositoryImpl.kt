package com.example.prognosisai.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.prognosisai.data.User
import com.example.prognosisai.utils.NetworkResource
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthRepository {

    private val _userSignupResponseLiveData = MutableLiveData<NetworkResource<String>>()
    val userSignupResponseLiveData: LiveData<NetworkResource<String>>
        get() = _userSignupResponseLiveData
    override suspend fun signUpWithEmail(user: User) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password!!)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        _userSignupResponseLiveData.postValue(NetworkResource.Success(it.toString()))
                    }
                    else{
                        _userSignupResponseLiveData.postValue(NetworkResource.Error(it.toString()))
                    }
                }
        }
        catch (e : Exception){
            _userSignupResponseLiveData.postValue(NetworkResource.Error(e.message!!))
        }
    }

    override suspend fun signInWithEmail(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun forgotPassword(user: User) {
        TODO("Not yet implemented")
    }

}
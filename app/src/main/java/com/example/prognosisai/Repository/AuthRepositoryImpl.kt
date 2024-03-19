package com.example.prognosisai.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.prognosisai.data.Hospital
import com.example.prognosisai.utils.Constant.TAG
import com.example.prognosisai.utils.NetworkResource
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthRepository {

    private val _userSignupResponseLiveData = MutableLiveData<NetworkResource<String>>()
    val userSignupResponseLiveData: LiveData<NetworkResource<String>>
        get() = _userSignupResponseLiveData

    private val _userSignInResponseLiveData = MutableLiveData<NetworkResource<String>>()
    val userSignInResponseLiveData: LiveData<NetworkResource<String>>
        get() = _userSignInResponseLiveData

    private val _forgetpassResponseLiveData = MutableLiveData<NetworkResource<String>>()
    val forgetpassResponseLiveData: LiveData<NetworkResource<String>>
        get() = _forgetpassResponseLiveData

    private val _emailVerfResponseLiveData = MutableLiveData<NetworkResource<String>>()
    val emailVerfResponseLiveData: LiveData<NetworkResource<String>>
        get() = _emailVerfResponseLiveData

//    private val checkEmailLiveData: MutableLiveData<NetworkResource<Boolean>>()
//            get() = _checkEmailLiveData







    override suspend fun signUpWithEmail(hospital: Hospital) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(hospital.email, hospital.password!!)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        _userSignupResponseLiveData.postValue(NetworkResource.Success(it.toString()))
                    }
                    else{
                        _userSignupResponseLiveData.postValue(NetworkResource.Error(message = it.toString()))
                    }
                }
        }
        catch (e : Exception){
            _userSignupResponseLiveData.postValue(NetworkResource.Error(message = e.message!!))
        }
    }


    override suspend fun signInWithEmail(hospital: Hospital) {

        try {
            firebaseAuth.signInWithEmailAndPassword(hospital.email, hospital.password!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        _userSignInResponseLiveData.postValue(NetworkResource.Success(task.toString()))
                    }else{
                        _userSignInResponseLiveData.postValue(NetworkResource.Error(message = task.toString()))
                    }
                }
        }
        catch (e : Exception){
            _userSignInResponseLiveData.postValue(NetworkResource.Error(message = e.message!!))
        }
    }



    override suspend fun forgotPassword(hospital: Hospital) {
        try {
            firebaseAuth.sendPasswordResetEmail(hospital.email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){

                        _forgetpassResponseLiveData.postValue(NetworkResource.Success(task.toString()))
                    }else{
                        _forgetpassResponseLiveData.postValue(NetworkResource.Error(message = task.toString()))
                    }
                }
        }
        catch (e : Exception){
            _forgetpassResponseLiveData.postValue(NetworkResource.Error(message = e.message!!))
        }
    }



    override suspend fun emailVerification() {
        val user = firebaseAuth.currentUser
        Log.d(TAG, "emailVerification: ${user.toString()}")
        try {
            user?.sendEmailVerification()?.addOnCompleteListener { task ->

                if(task.isSuccessful){
                    _emailVerfResponseLiveData.postValue(NetworkResource.Success(task.toString()))
                }else{
                    _emailVerfResponseLiveData.postValue(NetworkResource.Error(message = task.toString()))
                }
            }
        }
        catch (e: Exception){
            _emailVerfResponseLiveData.postValue(NetworkResource.Error(message = e.message!!))
        }


    }

    override suspend fun checkEmailVerification() : Boolean {
        firebaseAuth.currentUser!!.reload()
        Log.d(TAG, "checkEmailVerification: ${firebaseAuth.currentUser!!.isEmailVerified}")
        return firebaseAuth.currentUser!!.isEmailVerified

    }

}
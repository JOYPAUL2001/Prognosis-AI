package com.example.prognosisai.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.prognosisai.data.Hospital
import com.example.prognosisai.data.Patient
import com.example.prognosisai.utils.Constant.TAG
import com.example.prognosisai.utils.NetworkResource
import com.example.prognosisai.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth, private val providesRealTimeDatabaseInstance: DatabaseReference) : AuthRepository {

    @Inject
    lateinit var tokenManager : TokenManager

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




    private val _storingHospitalsDetails = MutableLiveData<NetworkResource<String>>()
    val storingHospitalsDetails: LiveData<NetworkResource<String>>
        get() = _storingHospitalsDetails



    private val _storingPatientDetails = MutableLiveData<NetworkResource<String>>()
    val storingPatientDetails : LiveData<NetworkResource<String>>
        get() = _storingPatientDetails





    override suspend fun signUpWithEmail(hospital: Hospital) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(hospital.email!!, hospital.password!!)
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
            firebaseAuth.signInWithEmailAndPassword(hospital.email!!, hospital.password!!)
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
            firebaseAuth.sendPasswordResetEmail(hospital.email!!)
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
       // Log.d(TAG, "checkEmailVerification: ${firebaseAuth.currentUser!!.isEmailVerified}")
        return firebaseAuth.currentUser!!.isEmailVerified

    }

    override suspend fun alReadyLogedIn(): Boolean {
        return firebaseAuth.currentUser!=null
    }


    override suspend fun storingHospitalDetailsRDB(hospital: Hospital) {


            val hospitalDetails = Hospital(hospital.email,hospital.password,hospital.id,hospital.name,hospital.address,hospital.pinCode,hospital.contactNumber,hospital.uniqueId,hospital.patient)
            providesRealTimeDatabaseInstance.child(hospital.uniqueId!!).setValue(hospitalDetails).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    tokenManager.saveId("Hospital_Id",hospital.uniqueId)
                    providesRealTimeDatabaseInstance.child(tokenManager.getId("Hospital_Id")!!).child("Patient")
                    _storingHospitalsDetails.postValue(NetworkResource.Success("Hospital details stored successfully")) // Provide a clear success message
                } else {
                    _storingHospitalsDetails.postValue(NetworkResource.Error(message = "Failed to store hospital details"))
                }
            }
    }

    override suspend fun storingPatientDetailsRDB(patient: Patient) {
        val patientDetails = Patient(patient.prediction, patient.pName, patient.dob, patient.gender, patient.age, patient.city, patient.state, patient.ptId, patient.ptUniqueId)
        providesRealTimeDatabaseInstance.child(tokenManager.getId("Hospital_Id")!!).child("Patient").child(patient.ptUniqueId!!).setValue(patientDetails).addOnCompleteListener { task->
            if (task.isSuccessful) {
                _storingHospitalsDetails.postValue(NetworkResource.Success("Patient details stored successfully")) // Provide a clear success message
            } else {
                _storingHospitalsDetails.postValue(NetworkResource.Error(message = "Failed to store patient details"))
            }
        }
    }

}



// _storingHospitalsDetails.postValue(NetworkResource.Success(task.toString()))

//_storingHospitalsDetails.postValue(NetworkResource.Error(message = task.toString()))
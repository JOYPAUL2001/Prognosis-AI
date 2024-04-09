package com.example.prognosisai.utils

import android.text.TextUtils
import android.util.Patterns

class inputValidationHelper {
    fun userInputValidation(email: String, password: String?, isLogin: Boolean) : Pair<Boolean, String>{
        var result = Pair(true, "")
        if (isLogin  || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please fill up all the details")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide valid email address")
        }
        else if (password!!.length <=5){
            result = Pair(false, "Password must be greater than 5")
        }
        return result
    }

    fun forgetPassInputValidation(email: String, isLogin: Boolean) : Pair<Boolean, String>{
        var result = Pair(true, "")
        if (isLogin  || TextUtils.isEmpty(email) ){
            result = Pair(false, "Please fill up all the details!")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide valid email address!")
        }
        return result
    }
    fun patientDetailsValidation(name: String?, dob: String?, gender: String?, age: String?, city: String?, state: String?) : Pair<Boolean, String>{
        var result = Pair(true, "")
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(age) || TextUtils.isEmpty(city) || TextUtils.isEmpty(state)){
            result = Pair(false, "Please fill up all the details")
        }
        else if(age!!.toInt()<1){
            result = Pair(false, "Age should be greater than zero! Check your Date of birth.")
        }
        return result
    }

    fun hospitalDetailsValidation(name: String?, id: String?, address: String?, pinCode: String?, contactNumber: String?): Pair<Boolean, String>{
        var result = Pair(true, "")

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(id) || TextUtils.isEmpty(address) || TextUtils.isEmpty(pinCode) || TextUtils.isEmpty(contactNumber)){
            result = Pair(false, "Please fill up all the details!")
        }
        else if(id!!.length<6){
            result = Pair(false, "Hospital Id size should be greater than 5!")
        }
        else if(pinCode!!.length!=6){
            result = Pair(false, "PIN Code should be 6 digit!")
        }
        else if(contactNumber!!.length!=10){
            result = Pair(false, "Contact number should be 10 digit!")
        }
        return result
    }
}
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
            result = Pair(false, "Please fill up all the details")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide valid email address")
        }
        return result
    }
}
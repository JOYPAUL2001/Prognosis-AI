package com.example.prognosisai.utils

import android.content.Context
import com.example.prognosisai.utils.Constant.PREFS_TOKEN_FILE
import com.example.prognosisai.utils.Constant.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token : String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken() : String? {
        return prefs.getString(USER_TOKEN,null)
    }

    fun saveId(id: String,token: String){
        val editor = prefs.edit()
        editor.putString(id, token)
        editor.apply()
    }

    fun getId(id: String): String? {
        return prefs.getString(id,null)
    }

    fun saveEmail(id: String, token: String){
        val editor = prefs.edit()
        editor.putString(id, token)
        editor.apply()
    }

    fun getEmail(id: String): String? {
        return prefs.getString(id,null)
    }

}
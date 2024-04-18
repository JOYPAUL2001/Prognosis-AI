package com.example.prognosisai.di

import android.content.Context
import android.os.Environment
import com.example.prognosisai.R
import java.io.File

object Common {
    fun getAppPath(context: Context): String{
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                + File.separator
                +context.resources.getString(R.string.app_name)
                + File.separator)
        if(!dir.exists())
            dir.mkdir()
        return dir.path+ File.separator
    }
}
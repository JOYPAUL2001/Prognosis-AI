package com.example.prognosisai.di

import com.example.prognosisai.data.Hospital
import com.example.prognosisai.data.Patient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Calendar
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuthInstance() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providesRealTimeDatabaseInstance() : DatabaseReference{
        return FirebaseDatabase.getInstance().getReference("Hospital Info")
    }

    @Provides
    @Singleton
    fun providesCalenderInstance() : Calendar{
        return Calendar.getInstance()
    }

}
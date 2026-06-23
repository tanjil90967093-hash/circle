package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.repository.MockVideoRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.VideoRepository

interface AppContainer {
    val authRepository: AuthRepository
    val videoRepository: VideoRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    
    // We would use real Supabase APIs here if keys were provided.
    // For now we setup the offline cache logic and mock the network responses.
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "circle_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    override val authRepository: AuthRepository by lazy {
        com.example.data.repository.SupabaseAuthRepositoryImpl(supabase)
    }

    override val videoRepository: VideoRepository by lazy {
        MockVideoRepositoryImpl(database.videoDao())
    }
}

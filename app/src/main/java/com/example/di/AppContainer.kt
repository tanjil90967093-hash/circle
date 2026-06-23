package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.BuildConfig
import com.example.data.local.AppDatabase
import com.example.data.remote.SupabaseEdgeApi
import com.example.data.repository.MockVideoRepositoryImpl
import com.example.data.repository.UploadRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UploadRepository
import com.example.domain.repository.VideoRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface AppContainer {
    val authRepository: AuthRepository
    val videoRepository: VideoRepository
    val uploadRepository: UploadRepository
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

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply { 
            level = HttpLoggingInterceptor.Level.BODY 
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL + "/") // Add trailing slash just in case
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val supabaseEdgeApi: SupabaseEdgeApi by lazy {
        retrofit.create(SupabaseEdgeApi::class.java)
    }

    override val uploadRepository: UploadRepository by lazy {
        UploadRepositoryImpl(context, supabaseEdgeApi, okHttpClient)
    }
}


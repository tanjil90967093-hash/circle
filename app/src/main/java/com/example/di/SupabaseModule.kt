package com.example.di

import com.example.util.GenesisMatrix
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth

val supabase = createSupabaseClient(
    supabaseUrl = GenesisMatrix.endpointUrl,
    supabaseKey = GenesisMatrix.publicNodeKey
) {
    install(Auth)
}

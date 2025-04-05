package com.example.fitnessapp.ui.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Response


object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://localhost:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authService: AuthService = retrofit.create(AuthService::class.java)
}

interface AuthService {
    @POST("api/auth/sync")
    suspend fun syncUser(
        @Header("Authorization") idToken: String
    ): Response<Void>
}


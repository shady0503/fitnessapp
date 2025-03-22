package com.example.fitnessapp.Backend.Models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String
)
